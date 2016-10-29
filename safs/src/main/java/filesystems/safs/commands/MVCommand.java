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
    public boolean validateAndInitializeArguments(String... arguments) {
        return arguments != null && arguments.length == 2;
    }
}
