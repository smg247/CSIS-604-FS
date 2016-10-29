package filesystems.safs.commands;

import java.io.IOException;

public class MKDIRCommand extends Command {
    @Override
    CommandResult executeOnMaster(String... arguments) throws IOException {
        //TODO
        return null;
    }

    @Override
    CommandResult executeOnSlave(String... arguments) throws IOException {
        //TODO
        return null;
    }

    @Override
    boolean hasValidArguments(String... arguments) {
        return arguments != null && arguments.length == 1;
    }
}
