package filesystems.safs.storageRepresentations;

class File {
    private String fullyQualifiedPath;
    private int sizeInBytes;


    File(String fullyQualifiedPath, int sizeInBytes) {
        this.fullyQualifiedPath = fullyQualifiedPath;
        this.sizeInBytes = sizeInBytes;
    }

    public void setFullyQualifiedPath(String fullyQualifiedPath) {
        this.fullyQualifiedPath = fullyQualifiedPath;
    }

    public String getFullyQualifiedPath() {
        return fullyQualifiedPath;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }
}
