package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;

import java.io.IOException;
import java.util.List;

class CPCommand extends Command {
    @Override
    public CommandResult executeOnMaster() throws IOException {
        //TODO
        return null;
    }

    @Override
    public CommandResult executeOnSlave() throws IOException {
        //TODO
        return null;
    }

    @Override
    public CommandResult validateSpecificArguments(List<String> arguments) {
        if (arguments.size() == 2) {
            return CommandResult.forSuccess();
        } else {
            return CommandResult.forError();
        }
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        //TODO
    }
}
