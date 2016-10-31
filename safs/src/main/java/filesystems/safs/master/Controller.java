package filesystems.safs.master;

import filesystems.safs.commandFramework.DashedCommandArgument;
import filesystems.safs.commandFramework.commands.CommandType;
import filesystems.safs.storageRepresentations.Directory;
import filesystems.safs.storageRepresentations.File;
import filesystems.safs.storageRepresentations.Node;

import java.util.*;

public class Controller {
    public static final Controller CONTROLLER = new Controller();
    private List<Node> nodes;
    private boolean redundantMode;
    private boolean testEnvironment;


    public void initialize(boolean testEnvironment) {
        if (nodes == null) {
            ResourceBundle resourceBundle;
            if (testEnvironment) {
                resourceBundle = ResourceBundle.getBundle("LocalDev");
            } else {
                resourceBundle = ResourceBundle.getBundle("Production");
            }
            this.testEnvironment = testEnvironment;
            redundantMode = Boolean.parseBoolean(resourceBundle.getString("redundantMode"));
            String[] nodeLocations = resourceBundle.getString("nodeLocations").split(",");
            if (nodeLocations.length >= 1) {
                nodes = new ArrayList<>();
                for (String nodeLocation : nodeLocations) {
                    nodes.add(new Node(nodeLocation));
                }

                obtainUpdatedFileInformation();
            } else {
                throw new IllegalArgumentException("No Node locations were provided when initializing the Controller.");
            }
        } else {
            throw new IllegalStateException("Attempted to initialize the Controller when it was already initialized.");
        }
    }

    public List<Node> determineNodesToReceiveNewFileOrDirectory() {
        if (redundantMode) {
            return nodes;
        } else {
            Collections.sort(nodes);
            return Arrays.asList(nodes.get(0));
        }
    }

    public List<Node> getNodesContainingFile(String fileName) {
        List<Node> nodesContainingFile = new ArrayList<>();
        for (Node node : nodes) {
            if (node.hasFile(fileName)) {
                nodesContainingFile.add(node);
            }
        }

        return nodesContainingFile;
    }

    public List<Node> getNodesContainingDirectory(String fullyQualifiedPath) {
        List<Node> nodesContainingDirectory = new ArrayList<>();
        for (Node node : nodes) {
            if (node.hasDirectory(fullyQualifiedPath)) {
                nodesContainingDirectory.add(node);
            }
        }

        return nodesContainingDirectory;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void prettyPrint(String fullyQualifiedDirectoryName) {
        if (fullyQualifiedDirectoryName.endsWith("/")) {
            fullyQualifiedDirectoryName = fullyQualifiedDirectoryName.substring(0, fullyQualifiedDirectoryName.length() - 1);
        }
        Directory unifiedHomeDirectory = getUnifiedHomeDirectory();
        unifiedHomeDirectory.getDirectory(fullyQualifiedDirectoryName).prettyPrint();
    }

    private Directory getUnifiedHomeDirectory() {
        Directory unifiedHomeDirectory = new Directory("");
        for (Node node : nodes) {
            unifyFiles(node.getHomeDirectory(), unifiedHomeDirectory);
        }
        return unifiedHomeDirectory;
    }

    private void unifyFiles(Directory fromDirectory, Directory intoDirectory) {
        for (File file : fromDirectory.getFiles()) {
            intoDirectory.addFile(file.getFileName(), file.getSizeInBytes());
        }

        for (Directory fromSubDirectory : fromDirectory.getDirectories()) {
            Directory intoSubDirectory = intoDirectory.addDirectory(fromSubDirectory.getName());
            unifyFiles(fromSubDirectory, intoSubDirectory);
        }
    }

    public boolean containsDirectory(String fullyQualifiedDirectoryName) {
        return getUnifiedHomeDirectory().getDirectory(fullyQualifiedDirectoryName) != null;
    }

    public boolean containsFile(String fileName) {
        return getUnifiedHomeDirectory().containsFile(fileName);
    }

    public void obtainUpdatedFileInformation() {
        // By running an ls with no output and forcing it to get the file info from the slaves we can update the controllers info behind the scenes
        CommandType.ls.executeOnMaster(DashedCommandArgument.n.getNameWithDash(), DashedCommandArgument.u.getNameWithDash()); // By performing an ls here we populate all of the nodes with their initial file info
    }

    public boolean isTestEnvironment() {
        return testEnvironment;
    }
}
