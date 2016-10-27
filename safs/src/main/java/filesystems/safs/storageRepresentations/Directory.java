package filesystems.safs.storageRepresentations;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Directory {
    private String name;
    private Set<Directory> directories;
    private Set<File> files;


    public Directory(String name) {
        this.name = name;
        directories = new HashSet<>();
        files = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Directory addDirectory(String directoryName) {
        Directory directory = null;
        for (Directory dir : directories) {
            if (directoryName.equals(dir.getName())) {
                directory = dir;
            }
        }

        if (directory == null) {
            directory = new Directory(directoryName);
            directories.add(directory);
        }

        return directory;
    }

    public Set<Directory> getDirectories() {
        return directories;
    }

    public void addFile(String fileName, int sizeInBytes) {
        if (fileName.contains("/")) {
            String directoryName = fileName.substring(0, fileName.indexOf("/"));
            String restOfFilePath = fileName.substring(fileName.indexOf("/") + 1);
            Directory directory = addDirectory(directoryName);
            directory.addFile(restOfFilePath, sizeInBytes);
        } else {
            files.add(new File(fileName, sizeInBytes));
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
        for (File file : files) {
            if (fileName.equals(file.getFileName())) {
                return true;
            }
        }

        for (Directory directory : directories) {
            if (directory.containsFile(fileName)) {
                return true;
            }
        }

        return false;
    }

    public int getNumberOfIncludedFiles() {
        int size = files.size();

        for (Directory directory : directories) {
            size += directory.getNumberOfIncludedFiles();
        }

        return size;
    }

    public void prettyPrint() {
        prettyPrint(0);
    }

    private void prettyPrint(int nestingLevel) {
        System.out.println(determineSpacesFromNestingLevel(nestingLevel) + name + "/");
        nestingLevel++;

        for (File file : files) {
            System.out.println(determineSpacesFromNestingLevel(nestingLevel) + file.getFileName());
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
}
