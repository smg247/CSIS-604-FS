package filesystems.safs.commands;

import java.io.IOException;

import static filesystems.safs.commands.CommandResult.error;
import static filesystems.safs.commands.CommandResult.success;

class MVCommand extends Command {
    @Override
    public CommandResult executeOnMaster(String... arguments) throws IOException {
        // Move is really just a copy followed by removing the original file
        CommandResult commandResult = CommandType.cp.executeOnMaster(arguments);
        if (success == commandResult) {
            CommandResult rmResult = CommandType.rm.executeOnMaster(arguments[0]);
            return rmResult;
        } else {
            return commandResult;
        }
    }

    @Override
    public CommandResult executeOnSlave(String... arguments) throws IOException {
        // Not valid for execution on slave
        return error;
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
