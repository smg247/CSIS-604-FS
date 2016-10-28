package filesystems.safs.commands;

import com.sun.tools.javac.util.Pair;
import filesystems.safs.storageRepresentations.Node;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static filesystems.safs.commands.CommandResult.error;
import static filesystems.safs.commands.CommandResult.success;

class LSCommand extends Command {
    @Override
    CommandResult executeOnMaster(String... arguments) throws IOException {
        if (!controller.isContainsUpToDateFileInformation()) {
            for (Node node : controller.getNodes()) {
                String directory = "";
                if (arguments != null && arguments.length == 1) {
                    directory = arguments[0];
                }

                String message = CommandType.ls.getName() + " " + node.getFullyQualifiedHomeDirectoryName() + directory;
                List<String> result = sendMessageToSlaveNode(node, Arrays.asList(message));
                if (result != null) {
                    for (String line : result) {
                        Pair<String, Long> fileNameAndSize = deserializeFileNameAndSize(line);
                        node.addFile(fileNameAndSize.fst, fileNameAndSize.snd); // Attempt to add the file to the node just in case we didn't already know about it
                    }
                } else {
                    return error;
                }
            }

            controller.setContainsUpToDateFileInformation(true); // We will no longer need to reach out to the slaves in order to perform an ls or know what files they contain
        }

        String directory = "";
        if (arguments != null && arguments.length == 1) {
            directory = arguments[0];
        }

        controller.prettyPrint(directory);

        return success;
    }

    @Override
    CommandResult executeOnSlave(String... arguments) throws IOException {
        File directory = new File(arguments[0]);
        Map<String, Long> fileNames = new HashMap<>();
        if (directory.isDirectory()) {
            determineFilesInDirectory(directory, fileNames);
            List<String> relativeFileNames = convertAllFileNamesToRelativeNamesAndPrepareForMessage(directory.getName(), fileNames);
            CommandResult commandResult = success;
            commandResult.setMultiLinedMessage(relativeFileNames);
            return commandResult;
        } else {
            CommandResult commandResult = error;
            commandResult.setSimpleMessage("Directory was not provided.");
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
    String getName() {
        return "ls";
    }

    @Override
    String getDescription() {
        return "Lists the contents of the current directory";
    }

    @Override
    String getUsageDirections() {
        return "Used by simply typing ls";
    }

    @Override
    boolean hasValidArguments(String... arguments) {
        return arguments == null || arguments.length <= 1;
    }
}
