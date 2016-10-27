package filesystems.safs.commands;

import filesystems.safs.storageRepresentations.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                    for (String fileName : result) {
                        node.addFile(fileName); // Attempt to add the file to the node just in case we didn't already know about it
                    }
                } else {
                    return error;
                }
            }

            controller.setContainsUpToDateFileInformation(true); // We will no longer need to reach out to the slaves in order to perform an ls or know what files they contain
        }

        controller.prettyPrint();

        return success;
    }

    @Override
    CommandResult executeOnSlave(String... arguments) throws IOException {
        File directory = new File(arguments[0]);
        List<String> fileNames = new ArrayList<>();
        if (directory.isDirectory()) {
            determineFilesInDirectory(directory, fileNames);
            List<String> relativeFileNames = makeRelativeFileNames(directory.getName(), fileNames);
            CommandResult commandResult = success;
            commandResult.setMultiLinedMessage(relativeFileNames);
            return commandResult;
        } else {
            CommandResult commandResult = error;
            commandResult.setSimpleMessage("Directory was not provided.");
            return commandResult;
        }
    }

    private void determineFilesInDirectory(File file, List<String> fileNames) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                determineFilesInDirectory(f, fileNames);
            } else {
                fileNames.add(f.getAbsolutePath());
            }
        }
    }

    private List<String> makeRelativeFileNames(String relativeDirectoryName, List<String> fileNames) {
        List<String> relativeFileNames = new ArrayList<>();
        for (String fileName : fileNames) {
            int relativeDirectoryNameIndex = fileName.indexOf(relativeDirectoryName);
            relativeFileNames.add(fileName.substring(relativeDirectoryNameIndex + relativeDirectoryName.length() + 1));
        }

        return relativeFileNames;
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
