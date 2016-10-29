package filesystems.safs.commands;

import filesystems.safs.storageRepresentations.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static filesystems.safs.commands.CommandResult.error;
import static filesystems.safs.commands.CommandResult.success;

class TouchCommand extends Command {
    private String fileName;

    @Override
    public CommandResult executeOnMaster(String... arguments) throws IOException {
        if (!controller.doesFileExist(fileName)) {
            Node node = controller.determineNodeToReceiveNewFile();
            String fileNameForSlave = node.getFullyQualifiedHomeDirectoryName() + fileName;
            System.out.println("Sending " + fileName + " to " + node.toString() + ":" + node.getPort());

            List<String> response = sendMessageToSlaveNode(node, Arrays.asList(CommandType.touch.name() + " " + fileNameForSlave));
            if (response != null) { // If we get a non-null response back we know it was successful
                node.addFile(fileName);
                return success;
            } else {
                return error;
            }
        } else {
            CommandResult commandResult = error;
            commandResult.setSimpleMessage("File already exists!");
            return commandResult;
        }
    }

    @Override
    public CommandResult executeOnSlave(String... arguments) throws IOException {
        Path path = Paths.get(fileName);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.createFile(path);

        return success;
    }

    @Override
    public boolean validateAndInitializeArguments(String... arguments) {
        if (arguments != null && arguments.length == 1) {
            fileName = arguments[0];
            return true;
        } else {
            return false;
        }
    }
}
