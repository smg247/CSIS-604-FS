package filesystems.safs.commands;

import java.io.IOException;

class CPCommand extends Command {
    @Override
    public CommandResult executeOnMaster(String... arguments) throws IOException {
        //TODO
        return null;
    }

    @Override
    public CommandResult executeOnSlave(String... arguments) throws IOException {
        //TODO
        return null;
    }

    @Override
    public boolean validateAndInitializeArguments(String... arguments) {
        return arguments != null && arguments.length == 2;
    }
}
