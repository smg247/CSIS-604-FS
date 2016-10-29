package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.storageRepresentations.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


class TouchCommand extends Command {
    private String fileName;

    @Override
    public CommandResult executeOnMaster() throws IOException {
        if (!controller.doesFileExist(fileName)) {
            Node node = controller.determineNodeToReceiveNewFile();
            String fileNameForSlave = node.getFullyQualifiedHomeDirectoryName() + fileName;
            System.out.println("Sending " + fileName + " to " + node.toString() + ":" + node.getPort());

            List<String> response = sendMessageToSlaveNode(node, Arrays.asList(CommandType.touch.name() + " " + fileNameForSlave));
            if (response != null) { // If we get a non-null response back we know it was successful
                node.addFile(fileName);
                return new CommandResult(CommandResult.CommandStatus.success);
            } else {
                return new CommandResult(CommandResult.CommandStatus.error);
            }
        } else {
            CommandResult commandResult = new CommandResult(CommandResult.CommandStatus.error);
            commandResult.addSingleMessage("File already exists!");
            return commandResult;
        }
    }

    @Override
    public CommandResult executeOnSlave() throws IOException {
        Path path = Paths.get(fileName);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.createFile(path);

        return new CommandResult(CommandResult.CommandStatus.success);
    }

    @Override
    public boolean validateSpecificArguments(List<String> arguments) {
        return arguments.size() == 1;
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        fileName = arguments.get(0);
    }
}
