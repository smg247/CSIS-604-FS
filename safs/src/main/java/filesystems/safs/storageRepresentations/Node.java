package filesystems.safs.storageRepresentations;


import filesystems.safs.master.Controller;

public class Node implements Comparable<Node> {
    private String address;
    private int port;
    private Directory homeDirectory;


    public Node(String addressAndPort) {
        String[] arguments = addressAndPort.split(":");
        if (arguments.length == 2) {
            address = arguments[0];
            port = Integer.parseInt(arguments[1]);
            String directoryName = Controller.CONTROLLER.isTestEnvironment() ? ("dir" + port) : "dir";
            homeDirectory = new Directory(directoryName);
        } else {
            throw new IllegalArgumentException("Improperly formatted addressAndPort.");
        }

    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void addFile(String fullyQualifiedPath) {
        addFile(fullyQualifiedPath, 0);
    }

    public void addFile(String fullyQualifiedPath, long sizeInBytes) {
        homeDirectory.addFile(fullyQualifiedPath, sizeInBytes);
    }

    public void addDirectory(String fullyQualifiedPath) {
        homeDirectory.addDirectory(fullyQualifiedPath);
    }

    public Directory getHomeDirectory() {
        return homeDirectory;
    }

    public String getFullyQualifiedHomeDirectoryName() {
        return homeDirectory.getName() + java.io.File.separator;
    }

    public boolean hasFile(String fileName) {
        return homeDirectory.containsFile(fileName);
    }

    public boolean hasDirectory(String fullyQualifiedPath) {
        return homeDirectory.containsDirectory(fullyQualifiedPath);
    }

    public void removeFile(String fileName) {
        homeDirectory.removeFile(fileName);
    }

    public void removeDirectory(String fullyQualifiedPath) {
        homeDirectory.removeDirectory(fullyQualifiedPath);
    }

    public File getFile(String fileName) {
        return homeDirectory.getFile(fileName);
    }

    @Override
    public int compareTo(Node o) {
        int differenceInBytesStored = homeDirectory.getBytesStored() - o.homeDirectory.getBytesStored();
        if (differenceInBytesStored == 0) {
            return homeDirectory.getNumberOfIncludedFiles() - o.homeDirectory.getNumberOfIncludedFiles();
        } else {
            return differenceInBytesStored;
        }
    }

    @Override
    public String toString() {
        return address;
    }
}
