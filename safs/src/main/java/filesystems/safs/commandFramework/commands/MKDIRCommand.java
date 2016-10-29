package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.commandFramework.commands.Command;

import java.io.IOException;
import java.util.List;

class MKDIRCommand extends Command {
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
    protected boolean validateSpecificArguments(List<String> arguments) {
        return arguments.size() == 1;
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        //TODO
    }
}
