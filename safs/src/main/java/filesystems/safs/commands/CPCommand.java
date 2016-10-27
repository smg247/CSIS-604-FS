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
    public String getName() {
        return "cp";
    }

    @Override
    public String getDescription() {
        return "Copies a file to a new location";
    }

    @Override
    public String getUsageDirections() {
        return "Supply the file name of the file to be copied, and the location of which to copy it. ex: cp test.txt testCopy.txt";
    }

    @Override
    public boolean hasValidArguments(String... arguments) {
        return arguments != null && arguments.length == 2;
    }
}
