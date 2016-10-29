package filesystems.safs.commands;

import java.io.IOException;
import java.util.List;

public class MKDIRCommand extends Command {
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
