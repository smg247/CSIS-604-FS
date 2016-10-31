package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.storageRepresentations.Node;

import java.io.IOException;
import java.util.List;

class RMCommand extends Command {

    @Override
    CommandResult executeOnMaster() throws IOException {
        //TODO
        return null;
    }

    @Override
    CommandResult executeOnSlave() throws IOException {
        //TODO
        return null;
    }

    @Override
    protected CommandResult validateSpecificArguments(List<String> arguments) {
        if (arguments.size() == 1) {
            return CommandResult.forSuccess();
        } else {
            return CommandResult.forError();
        }
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {

    }
}
