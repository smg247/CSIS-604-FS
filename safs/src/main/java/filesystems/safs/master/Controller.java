package filesystems.safs.master;

import filesystems.safs.commandFramework.commands.CommandType;
import filesystems.safs.commandFramework.DashedCommandArgument;
import filesystems.safs.storageRepresentations.Directory;
import filesystems.safs.storageRepresentations.File;
import filesystems.safs.storageRepresentations.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    public static final Controller CONTROLLER = new Controller();
    private List<Node> nodes;
    private boolean containsUpToDateFileInformation;
    private boolean testEnvironment;


    public void initialize(boolean testEnvironment, String... nodeLocations) {
        if (nodes == null) {
            this.testEnvironment = testEnvironment;
            containsUpToDateFileInformation = false;
            if (nodeLocations != null && nodeLocations.length >= 1) {
                nodes = new ArrayList<>();
                for (String nodeLocation : nodeLocations) {
                    nodes.add(new Node(nodeLocation));
                }

                CommandType.ls.executeOnMaster(DashedCommandArgument.n.getNameWithDash()); // By performing an ls here we populate all of the nodes with their initial file info
            } else {
                throw new IllegalArgumentException("No Node locations were provided when initializing the Controller.");
            }
        } else {
            throw new IllegalStateException("Attempted to initialize the Controller when it was already initialized.");
        }
    }

    public Node determineNodeToReceiveNewFileOrDirectory() {
        Collections.sort(nodes);
        return nodes.get(0);
    }

    public boolean doesFileExist(String fileName) {
        for (Node node : nodes) {
            if (node.hasFile(fileName)) {
                return true;
            }
        }

        return false;
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
        return getUnifiedHomeDirectory().getFile(fileName) != null;
    }

    public boolean isContainsUpToDateFileInformation() {
        return containsUpToDateFileInformation;
    }

    public void setContainsUpToDateFileInformation(boolean containsUpToDateFileInformation) {
        this.containsUpToDateFileInformation = containsUpToDateFileInformation;
    }

    public boolean isTestEnvironment() {
        return testEnvironment;
    }
}
