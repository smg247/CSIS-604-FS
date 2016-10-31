package filesystems.safs.commandFramework.commands;

import com.sun.tools.javac.util.Pair;
import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.commandFramework.DashedCommandArgument;
import filesystems.safs.storageRepresentations.Node;

import java.io.File;
import java.io.IOException;
import java.util.*;

class LSCommand extends Command {
    private String directoryName;

    @Override
    CommandResult executeOnMaster() throws IOException {
        if (dashedCommandArguments.contains(DashedCommandArgument.u)) {
            for (Node node : controller.getNodes()) {
                String message = CommandType.ls.name() + " " + node.getFullyQualifiedHomeDirectoryName() + (directoryName != null ? directoryName : "");
                List<String> result = sendMessageToSlaveNode(node, Arrays.asList(message));
                if (result != null) {
                    for (String line : result) {
                        Pair<String, Long> fileNameAndSize = deserializeFileNameAndSize(line);
                        if (fileNameAndSize.fst.contains(".")) { // We know its a file
                            node.addFile(fileNameAndSize.fst, fileNameAndSize.snd); // Attempt to add the file to the node just in case we didn't already know about it
                        } else {
                            node.addDirectory(fileNameAndSize.fst);
                        }
                    }
                } else {
                    return CommandResult.forError();
                }
            }
        }

        CommandResult commandResult;
        if (controller.containsDirectory(directoryName)) {
            if (!dashedCommandArguments.contains(DashedCommandArgument.n)) {
                controller.prettyPrint(directoryName);
            }
            commandResult = CommandResult.forSuccess();
        } else {
            commandResult = CommandResult.forError();
            commandResult.addSingleMessage("Directory does not exist!");
        }

        return commandResult;
    }

    @Override
    CommandResult executeOnSlave(List<String> additionalInformation) throws IOException {
        File directory = new File(directoryName);
        Map<String, Long> fileAndDirectoryNames = new HashMap<>();
        if (directory.isDirectory()) {
            determineFilesAndSubDirectoriesInDirectory(directory, fileAndDirectoryNames);
            List<String> relativeFileNames = convertAllFileAndDirectoryNamesToRelativeNamesAndPrepareForMessage(directory.getName(), fileAndDirectoryNames);
            CommandResult commandResult = CommandResult.forSuccess();
            commandResult.setMessages(relativeFileNames);
            return commandResult;
        } else {
            CommandResult commandResult = CommandResult.forError();
            commandResult.addSingleMessage("Directory was not provided.");
            return commandResult;
        }
    }

    private static void determineFilesAndSubDirectoriesInDirectory(File file, Map<String, Long> fileNameToSizeInBytes) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                fileNameToSizeInBytes.put(f.getAbsolutePath(), 0l); // The size will be ignored as we will not be tracking directory size
                determineFilesAndSubDirectoriesInDirectory(f, fileNameToSizeInBytes);
            } else {
                fileNameToSizeInBytes.put(f.getAbsolutePath(), f.length());
            }
        }
    }

    private static List<String> convertAllFileAndDirectoryNamesToRelativeNamesAndPrepareForMessage(String relativeDirectoryName, Map<String, Long> fileNameToSizeInBytes) {
       List<String> linesInMessage = new ArrayList<>();
        for (String fileName : fileNameToSizeInBytes.keySet()) {
            int relativeDirectoryNameIndex = fileName.indexOf(relativeDirectoryName);
            String relativeFileName = fileName.substring(relativeDirectoryNameIndex + relativeDirectoryName.length() + 1);
            linesInMessage.add(serializeFileNameAndSize(relativeFileName, fileNameToSizeInBytes.get(fileName)));
        }

        return linesInMessage;
    }

    private static String serializeFileNameAndSize(String fileName, long sizeInBytes) {
        return fileName + " " + sizeInBytes;
    }

    private static Pair<String, Long> deserializeFileNameAndSize(String fileNameAndSize) {
        String[] split = fileNameAndSize.split("\\s+");
        return new Pair<>(split[0], Long.parseLong(split[1]));
    }

    @Override
    protected CommandResult validateSpecificArguments(List<String> arguments) {
        if (arguments.size() <= 1) {
            return CommandResult.forSuccess();
        } else {
            return CommandResult.forError();
        }
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        if (arguments.isEmpty()) {
            directoryName = "";
        } else {
            directoryName = arguments.get(0);
        }
    }
}
