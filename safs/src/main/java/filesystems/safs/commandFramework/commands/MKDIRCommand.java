package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.commandFramework.commands.Command;
import filesystems.safs.storageRepresentations.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class MKDIRCommand extends CreationCommand {

    @Override
    public CommandResult executeOnMaster() throws IOException {
        CommandResult commandResult = CommandResult.forError();
        if (!controller.containsDirectory(path)) {
            commandResult = super.executeOnMaster();
        } else {
            commandResult.addSingleMessage("Directory already exists!");
        }

        return commandResult;
    }

    @Override
    public CommandResult executeOnSlave() throws IOException {
        Path p = Paths.get(path);
        Files.createDirectories(p);
        return CommandResult.forSuccess();
    }

    @Override
    protected CommandResult validateSpecificArguments(List<String> arguments) {
        String directoryPath = arguments.get(0);
        if (arguments.size() == 1 && !directoryPath.contains(".")) { // Directory names must not contain a dot
            return CommandResult.forSuccess();
        } else {
            return CommandResult.forError();
        }
    }

    @Override
    protected CommandType getSpecificCommandType() {
        return CommandType.mkdir;
    }

    @Override
    protected void notifyRespectiveNodeOfObjectAddition(Node node) {
        node.addDirectory(path);
    }
}
