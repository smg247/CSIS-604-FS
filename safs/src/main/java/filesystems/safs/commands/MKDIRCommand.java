package filesystems.safs.commands;

import filesystems.safs.storageRepresentations.Node;

import java.io.IOException;
import java.util.List;

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
    String getName() {
        return "mkdir";
    }

    @Override
    String getDescription() {
        return "Creates a new directory with the given name";
    }

    @Override
    String getUsageDirections() {
        return "Simply supply the directory name to be created. ex: mkdir directory";
    }

    @Override
    boolean hasValidArguments(String... arguments) {
        return arguments != null && arguments.length == 1;
    }
}
