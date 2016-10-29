package filesystems.safs.commands;


import java.io.IOException;

import static filesystems.safs.commands.CommandResult.error;
import static filesystems.safs.commands.CommandResult.success;

class HelpCommand extends Command {
    @Override
    public CommandResult executeOnMaster(String... arguments) throws IOException {
        for (CommandType commandType : CommandType.values()) {
            System.out.println(commandType.name() + " - " + commandType.getDescription() + " - Usage: " + commandType.getUsageDirections());
        }

        return success;
    }

    @Override
    public CommandResult executeOnSlave(String... arguments) throws IOException {
        // Not valid for execution on slave
        return error;
    }

    @Override
    public boolean validateAndInitializeArguments(String... arguments) {
        return true;
    }
}
