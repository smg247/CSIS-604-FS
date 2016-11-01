package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.commandFramework.DashedCommandArgument;
import filesystems.safs.storageRepresentations.Node;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CPCommand extends Command {
    private String originalPath;
    private String destinationPath;

    @Override
    public CommandResult executeOnMaster() throws IOException {
        boolean destinationPathAlreadyExists;
        if (workingWithFiles(originalPath, destinationPath)) {
            destinationPathAlreadyExists = controller.containsFile(destinationPath);
        } else {
            destinationPathAlreadyExists = controller.containsDirectory(destinationPath);
        }
        if (!destinationPathAlreadyExists) {
            CommandResult commandResult;
            if (dashedCommandArguments.contains(DashedCommandArgument.l) || !dashedCommandArguments.contains(DashedCommandArgument.r)) {
                commandResult = copyWithinFileSystem();
            } else {
                commandResult = copyFromOutsideFileSystem();
            }
            if (commandResult != null && commandResult.isSuccessful()) {
                controller.obtainUpdatedFileInformation();
            }
            return commandResult;
        } else {
            CommandResult commandResult = CommandResult.forError();
            commandResult.addSingleMessage("Destination already exists!");
            return commandResult;
        }
    }

    private CommandResult copyWithinFileSystem() throws IOException {
        List<Node> nodesContainingObject;
        boolean workingWithFiles = workingWithFiles(originalPath, destinationPath);
        if (workingWithFiles) {
            nodesContainingObject = controller.getNodesContainingFile(originalPath);
        } else {
            nodesContainingObject = controller.getNodesContainingDirectory(originalPath);
        }

        if (!nodesContainingObject.isEmpty()) {
            for (Node node : nodesContainingObject) {
                String originalPathForSlave = node.getFullyQualifiedHomeDirectoryName() + originalPath;
                String destinationPathForSlave = node.getFullyQualifiedHomeDirectoryName() + destinationPath;
                List<String> response = sendMessageToSlaveNode(node, Arrays.asList(CommandType.cp.name() + " " + originalPathForSlave + " " + destinationPathForSlave));
                if (response == null) {
                    return CommandResult.forError();
                }
            }
        } else {
            CommandResult commandResult = CommandResult.forError();
            commandResult.addSingleMessage((workingWithFiles ? "File" : "Directory") + " does not exist!");
            return commandResult;
        }

        return CommandResult.forSuccess();
    }

    private CommandResult copyFromOutsideFileSystem() throws IOException {
        if  (workingWithFiles(originalPath, destinationPath)) {
            return copyFileFromOutsideFileSystem(originalPath, destinationPath);
        } else {
            return copyDirectoryFromOutsideFileSystem(originalPath);
        }
    }
    
    private CommandResult copyDirectoryFromOutsideFileSystem(String directoryPath) throws IOException {
        File directoryFile = new File(directoryPath);

        File[] filesInDirectory = directoryFile.listFiles();
        if (filesInDirectory != null && filesInDirectory.length > 0) {
            for (File file : filesInDirectory) {
                if (file.isDirectory()) {
                    copyDirectoryFromOutsideFileSystem(file.getPath());
                } else {
                    String destinationFilePath = destinationPath + file.getPath().substring(file.getPath().indexOf("/"));
                    copyFileFromOutsideFileSystem(file.getPath(), destinationFilePath);
                }
            }
        } else {
            // As there are no files or sub directories in this directory we must copy it directly
            String destinationDirectoryPath = destinationPath + directoryFile.getPath().substring(directoryFile.getPath().indexOf("/"));
            return sendMessageContainingRemoteFileOrDirectoryInformation(directoryFile.getPath(), destinationDirectoryPath, new ArrayList<>());
        }
        return CommandResult.forSuccess();
    }

    private CommandResult copyFileFromOutsideFileSystem(String fileName, String destinationFileName) throws IOException {
        byte[] bytesOfFile = Files.readAllBytes(Paths.get(fileName));
        List<String> byteList = new ArrayList<>();
        if (bytesOfFile != null) {
            for (byte byteOfFile : bytesOfFile){
                byteList.add(Byte.toString(byteOfFile));
            }
        }

        return sendMessageContainingRemoteFileOrDirectoryInformation(fileName, destinationFileName, byteList);
    }

    private CommandResult sendMessageContainingRemoteFileOrDirectoryInformation(String fileOrDirectoryName, String destinationFileOrDirectoryName, List<String> byteList) throws IOException {
        List<Node> nodes = controller.determineNodesToReceiveNewFileOrDirectory();
        for (Node node : nodes) {
            String pathNameForSlave = node.getFullyQualifiedHomeDirectoryName() + destinationFileOrDirectoryName;
            List<String> messageForSlaveNodes = new ArrayList<>();
            messageForSlaveNodes.add(CommandType.cp.name() + " " + DashedCommandArgument.r.getNameWithDash() + " " + fileOrDirectoryName + " " + pathNameForSlave);
            messageForSlaveNodes.addAll(byteList);
            List<String> response = sendMessageToSlaveNode(node, messageForSlaveNodes);
            if (response == null) {
                return CommandResult.forError();
            }
        }

        return CommandResult.forSuccess();
    }

    @Override
    public CommandResult executeOnSlave(List<String> additionalInformation) throws IOException {
        if (dashedCommandArguments.contains(DashedCommandArgument.l) || !dashedCommandArguments.contains(DashedCommandArgument.r)) {
            return copyFromWithinFileSystemOnSlave();
        } else {
            return copyFromOutsideFileSystemOnSlave(additionalInformation);
        }
    }

    private CommandResult copyFromWithinFileSystemOnSlave() throws IOException {
        File originalFileOrDirectory = new File(originalPath);
        File destinationFileOrDirectory = new File(destinationPath);
        if (originalFileOrDirectory.isFile()) {
            FileUtils.copyFile(originalFileOrDirectory, destinationFileOrDirectory);
        } else if (originalFileOrDirectory.isDirectory()) {
            FileUtils.copyDirectory(originalFileOrDirectory, destinationFileOrDirectory);
        } else {
            return CommandResult.forError();
        }
        return CommandResult.forSuccess();
    }

    private CommandResult copyFromOutsideFileSystemOnSlave(List<String> fileBytes) throws IOException {
        File file = new File(destinationPath);
        byte[] bytes = new byte[fileBytes.size()];
        for (int i = 0; i < fileBytes.size(); i++) {
            bytes[i] = new Byte(fileBytes.get(i));
        }

        if (file.getParent() != null) {
            Files.createDirectories(Paths.get(file.getParent()));
        }

        FileUtils.writeByteArrayToFile(file, bytes);
        return CommandResult.forSuccess();
    }

    @Override
    public CommandResult validateSpecificArguments(List<String> arguments) {
        if (arguments.size() == 2) {
            String path = arguments.get(0);
            String newPath = arguments.get(1);
            // Must verify that both arguments are either files or directories
            if (workingWithFiles(path, newPath) || workingWithDirectories(path, newPath)) {
                return CommandResult.forSuccess();
            } else {
                return CommandResult.forError();
            }
        } else {
            return CommandResult.forError();
        }
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        originalPath = arguments.get(0);
        destinationPath = arguments.get(1);
    }

    private boolean workingWithFiles(String path, String newPath) {
        return path.contains(".") && newPath.contains(".");
    }

    private boolean workingWithDirectories(String path, String newPath) {
        return !path.contains(".") && !newPath.contains(".");
    }
}
