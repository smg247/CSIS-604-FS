package filesystems.safs.commands;

import java.io.IOException;

class MVCommand extends Command {
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
        return "mv";
    }

    @Override
    public String getDescription() {
        return "Moves a file to a new location";
    }

    @Override
    public String getUsageDirections() {
        return "Supply the file name of the file to be moved, and the location of which to move it. ex: mv test.txt renamed.txt";
    }

    @Override
    public boolean hasValidArguments(String... arguments) {
        return arguments != null && arguments.length == 2;
    }
}
