package filesystems.safs.storageRepresentations;

public class File {
    private String fileName;
    private long sizeInBytes;


    File(String fileName, long sizeInBytes) {
        this.fileName = fileName;
        this.sizeInBytes = sizeInBytes;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public void prettyPrint(String spacesToPrepend) {
        StringBuilder spaceBuilder = new StringBuilder();
        for (int i = fileName.length() + spacesToPrepend.length(); i < 50; i++) {
            spaceBuilder.append(" ");
        }
        System.out.println(spacesToPrepend + fileName + spaceBuilder.toString() + sizeInBytes + " bytes");
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
