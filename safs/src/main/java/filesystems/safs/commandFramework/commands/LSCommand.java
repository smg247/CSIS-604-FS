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
        if (!controller.isContainsUpToDateFileInformation()) {
            for (Node node : controller.getNodes()) {
                String message = CommandType.ls.name() + " " + node.getFullyQualifiedHomeDirectoryName() + (directoryName != null ? directoryName : "");
                List<String> result = sendMessageToSlaveNode(node, Arrays.asList(message));
                if (result != null) {
                    for (String line : result) {
                        Pair<String, Long> fileNameAndSize = deserializeFileNameAndSize(line);
                        node.addFile(fileNameAndSize.fst, fileNameAndSize.snd); // Attempt to add the file to the node just in case we didn't already know about it
                    }
                } else {
                    return new CommandResult(CommandResult.CommandStatus.error);
                }
            }

            controller.setContainsUpToDateFileInformation(true); // We will no longer need to reach out to the slaves in order to perform an ls or know what files they contain
        }

        CommandResult commandResult;
        if (controller.containsDirectory(directoryName)) {
            if (!dashedCommandArguments.contains(DashedCommandArgument.n)) {
                controller.prettyPrint(directoryName);
            }
            commandResult = new CommandResult(CommandResult.CommandStatus.success);
        } else {
            commandResult = new CommandResult(CommandResult.CommandStatus.error);
            commandResult.addSingleMessage("Directory does not exist!");
        }

        return commandResult;
    }

    @Override
    CommandResult executeOnSlave() throws IOException {
        File directory = new File(directoryName);
        Map<String, Long> fileNames = new HashMap<>();
        if (directory.isDirectory()) {
            determineFilesInDirectory(directory, fileNames);
            List<String> relativeFileNames = convertAllFileNamesToRelativeNamesAndPrepareForMessage(directory.getName(), fileNames);
            CommandResult commandResult = new CommandResult(CommandResult.CommandStatus.success);
            commandResult.setMessages(relativeFileNames);
            return commandResult;
        } else {
            CommandResult commandResult = new CommandResult(CommandResult.CommandStatus.error);
            commandResult.addSingleMessage("Directory was not provided.");
            return commandResult;
        }
    }

    private void determineFilesInDirectory(File file, Map<String, Long> fileNameToSizeInBytes) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                determineFilesInDirectory(f, fileNameToSizeInBytes);
            } else {
                fileNameToSizeInBytes.put(f.getAbsolutePath(), f.length());
            }
        }
    }

    private List<String> convertAllFileNamesToRelativeNamesAndPrepareForMessage(String relativeDirectoryName, Map<String, Long> fileNameToSizeInBytes) {
       List<String> linesInMessage = new ArrayList<>();
        for (String fileName : fileNameToSizeInBytes.keySet()) {
            int relativeDirectoryNameIndex = fileName.indexOf(relativeDirectoryName);
            String relativeFileName = fileName.substring(relativeDirectoryNameIndex + relativeDirectoryName.length() + 1);
            linesInMessage.add(serializeFileNameAndSize(relativeFileName, fileNameToSizeInBytes.get(fileName)));
        }

        return linesInMessage;
    }

    private String serializeFileNameAndSize(String fileName, long sizeInBytes) {
        return fileName + " " + sizeInBytes;
    }

    private Pair<String, Long> deserializeFileNameAndSize(String fileNameAndSize) {
        String[] split = fileNameAndSize.split("\\s+");
        return new Pair<>(split[0], Long.parseLong(split[1]));
    }

    @Override
    protected boolean validateSpecificArguments(List<String> arguments) {
        return arguments.size() <= 1;
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
