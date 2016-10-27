package filesystems.safs.storageRepresentations;

public class File {
    private String fileName;
    private int sizeInBytes;


    File(String fileName, int sizeInBytes) {
        this.fileName = fileName;
        this.sizeInBytes = sizeInBytes;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    @Override
        public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        File file = (File) o;

        return fileName.equals(file.fileName);

    }

    @Override
    public int hashCode() {
        return fileName.hashCode();
    }
}
