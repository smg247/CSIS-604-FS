package filesystems.safs.storageRepresentations;


import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    private String address;
    private int port;
    private List<File> storedFiles;
    private int bytesStored;


    public Node(String addressAndPort) {
        String[] arguments = addressAndPort.split(":");
        if (arguments.length == 2) {
            address = arguments[0];
            port = Integer.parseInt(arguments[1]);
        } else {
            throw new IllegalArgumentException("Improperly formatted addressAndPort.");
        }
        storedFiles = new ArrayList<>();
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

    public void addFile(String fullyQualifiedPath, int sizeInBytes) {
        storedFiles.add(new File(fullyQualifiedPath, sizeInBytes));
    }

    public int getBytesStored() {
        return bytesStored;
    }

    public void setBytesStored(int bytesStored) {
        this.bytesStored = bytesStored;
    }

    public boolean hasFile(String fileName) {
        for (File storedFile : storedFiles) {
            if (fileName.equals(storedFile.getFullyQualifiedPath())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int compareTo(Node o) {
        return o.bytesStored - bytesStored;
    }

    @Override
    public String toString() {
        return address;
    }
}
