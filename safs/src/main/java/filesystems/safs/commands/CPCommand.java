package filesystems.safs.commands;

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
    public boolean validateSpecificArguments(List<String> arguments) {
        return arguments.size() == 2;
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        //TODO
    }
}
