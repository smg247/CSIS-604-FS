package filesystems.safs.commands;

import java.io.IOException;

public class LSCommand extends Command {
    @Override
    CommandResult executeOnMaster(String... arguments) throws IOException {
        return null;
    }

    @Override
    CommandResult executeOnSlave(String... arguments) throws IOException {
        return null;
    }

    @Override
    String getName() {
        return "ls";
    }

    @Override
    String getDescription() {
        return "Lists the contents of the current directory";
    }

    @Override
    String getUsageDirections() {
        return "Used by simply typing ls";
    }

    @Override
    boolean hasValidArguments(String... arguments) {
        return true;
    }
}
