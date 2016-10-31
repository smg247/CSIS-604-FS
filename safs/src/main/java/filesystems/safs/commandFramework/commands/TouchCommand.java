package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.storageRepresentations.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


class TouchCommand extends CreationCommand {

    @Override
    public CommandResult executeOnMaster() throws IOException {
        CommandResult commandResult = CommandResult.forError();
        if (!controller.containsFile(path)) {
            commandResult = super.executeOnMaster();
        } else {
            commandResult.addSingleMessage("File already exists!");
        }

        return commandResult;
    }

    @Override
    public CommandResult executeOnSlave() throws IOException {
        Path p = Paths.get(path);
        if (p.getParent() != null) {
            Files.createDirectories(p.getParent());
        }
        Files.createFile(p);

        return CommandResult.forSuccess();
    }

    @Override
    public CommandResult validateSpecificArguments(List<String> arguments) {
        String filePath = arguments.get(0);
        if (arguments.size() == 1 && filePath.contains(".")) { // Filenames must contain a dot
            return CommandResult.forSuccess();
        } else {
            return CommandResult.forError();
        }
    }

    @Override
    protected CommandType getSpecificCommandType() {
        return CommandType.touch;
    }

    @Override
    protected void notifyRespectiveNodeOfObjectAddition(Node node) {
        node.addFile(path);
    }
}
