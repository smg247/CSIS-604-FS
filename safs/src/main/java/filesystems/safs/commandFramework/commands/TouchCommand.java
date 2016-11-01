package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.storageRepresentations.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


class TouchCommand extends CreationCommand {

    @Override
    public CommandResult executeOnSlave(List<String> additionalInformation) throws IOException {
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
    protected void notifyRespectiveNodeOfObjectAddition(Node node) {
        node.addFile(path);
    }

    @Override
    protected CommandResult checkIfObjectAlreadyExists() {
        CommandResult commandResult;
        if (!controller.containsFile(path)) {
            commandResult = CommandResult.forSuccess();
        } else {
            commandResult = CommandResult.forError();
            commandResult.addSingleMessage("File already exists!");
        }
        return commandResult;
    }
}
