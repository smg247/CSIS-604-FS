package filesystems.safs.storageRepresentations;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Directory {
    private String name;
    private Directory parentDirectory; // Will be null if this is the home Directory
    private Set<Directory> directories;
    private Set<File> files;


    public Directory(String name) {
        this(name, null);
    }

    public Directory(String name, Directory parentDirectory) {
        this.name = name;
        this.parentDirectory = parentDirectory;
        directories = new HashSet<>();
        files = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Directory addDirectory(String directoryName) {
        Directory directory = null;
        if (directoryName.endsWith("/")) { // We need to strip off the suffix /
            directoryName = directoryName.substring(0, directoryName.length() - 1);
        }

        String restOfPath = null;
        if (directoryName.contains("/")) {
            String fullDirectoryName = directoryName;
            directoryName = fullDirectoryName.substring(0, fullDirectoryName.indexOf("/"));
            restOfPath = fullDirectoryName.substring(fullDirectoryName.indexOf("/") + 1);
        }

        for (Directory dir : directories) {
            if (directoryName.equals(dir.getName())) {
                directory = dir;
            }
        }

        if (directory == null) {
            directory = new Directory(directoryName, this);
            directories.add(directory);
        }

        if (restOfPath != null) {
            directory = directory.addDirectory(restOfPath);
        }

        return directory;
    }

    public Set<Directory> getDirectories() {
        return directories;
    }

    public void addFile(String fileName, long sizeInBytes) {
        if (fileName.contains("/")) {
            String directoryName = fileName.substring(0, fileName.indexOf("/"));
            String restOfFilePath = fileName.substring(fileName.indexOf("/") + 1);
            Directory directory = addDirectory(directoryName);
            directory.addFile(restOfFilePath, sizeInBytes);
        } else {
            files.add(new File(fileName, this, sizeInBytes));
        }

    }

    public Set<File> getFiles() {
        return files;
    }

    public int getBytesStored() {
        int bytesStored = 0;
        for (File file : files) {
            bytesStored += file.getSizeInBytes();
        }

        for (Directory directory : directories) {
            bytesStored += directory.getBytesStored();
        }

        return bytesStored;
    }

    public boolean containsFile(String fileName) {
        return getFile(fileName) != null;
    }

    public File getFile(String fileName) {
        if (fileName.contains("/")) {
            String directoryName = fileName.substring(0, fileName.indexOf("/"));
            String restOfFilePath = fileName.substring(fileName.indexOf("/") + 1);
            return getDirectory(directoryName).getFile(restOfFilePath);
        } else {
            for (File file : files) {
                if (fileName.equals(file.getFileName())) {
                    return file;
                }
            }
        }

        return null;
    }

    public void removeFile(String fileName) {
        File file = getFile(fileName);
        if (file != null) {
            parentDirectory = file.getParentDirectory();
            if (parentDirectory != null) {
                Iterator<File> iterator = parentDirectory.getFiles().iterator();
                while (iterator.hasNext()) {
                    File fileOfParentDirectory = iterator.next();
                    if (fileOfParentDirectory.equals(file)) {
                        iterator.remove();
                        return;
                    }
                }
            }
        }
    }

    public int getNumberOfIncludedFiles() {
        int size = files.size();

        for (Directory directory : directories) {
            size += directory.getNumberOfIncludedFiles();
        }

        return size;
    }

    public boolean containsDirectory(String fullyQualifiedPath) {
        return getDirectory(fullyQualifiedPath) != null;
    }

    public Directory getDirectory(String fullyQualifiedPath) {
        if ("".equals(fullyQualifiedPath)) {
            return this;
        }
        String directoryName;
        if (fullyQualifiedPath.contains("/")) {
            directoryName = fullyQualifiedPath.substring(0, fullyQualifiedPath.indexOf("/"));
        } else {
            directoryName = fullyQualifiedPath;
        }
        for (Directory directory : directories) {
            if (directoryName.equals(directory.getName())) {
                if (directoryName.equals(fullyQualifiedPath)) {
                    return directory;
                } else {
                    String restOfFilePath = fullyQualifiedPath.substring(fullyQualifiedPath.indexOf("/") + 1);
                    return directory.getDirectory(restOfFilePath);
                }
            }
        }

        return null; // The provided fullyQualifiedPath was not found in this directories descendants
    }

    public void removeDirecotry(String fullyQualifiedPath) {
        Directory directory = getDirectory(fullyQualifiedPath);
        if (directory != null) {
            Directory parentDirectory = directory.getParentDirectory();
            if (parentDirectory != null) {
                Iterator<Directory> iterator = parentDirectory.getDirectories().iterator();
                while (iterator.hasNext()) {
                    Directory parentsSubDirectory = iterator.next();
                    if (parentsSubDirectory.equals(directory)) {
                        iterator.remove();
                        return;
                    }
                }
            }
        }
    }

    public void prettyPrint() {
        prettyPrint(0);
    }

    private void prettyPrint(int nestingLevel) {
        if (nestingLevel > 0) { // We don't really want to print this directory if its the root of our printing
            System.out.println(determineSpacesFromNestingLevel(nestingLevel) + name + "/");
        }
        nestingLevel++;

        for (File file : files) {
            file.prettyPrint(determineSpacesFromNestingLevel(nestingLevel));
        }

        for (Directory directory : directories) {
            directory.prettyPrint(nestingLevel);
        }
    }

    private String determineSpacesFromNestingLevel(int nestingLevel) {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < nestingLevel; i++) {
            spaces.append("  ");
        }

        return spaces.toString();
    }

    public Directory getParentDirectory() {
        return parentDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Directory directory = (Directory) o;

        if (!name.equals(directory.name)) {
            return false;
        }
        return parentDirectory.equals(directory.parentDirectory);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + parentDirectory.hashCode();
        return result;
    }
}
