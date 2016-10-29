package filesystems.safs.commands;


import java.io.IOException;
import java.util.List;

import static filesystems.safs.commands.CommandResult.error;
import static filesystems.safs.commands.CommandResult.success;

class HelpCommand extends Command {
    @Override
    public CommandResult executeOnMaster() throws IOException {
        for (CommandType commandType : CommandType.values()) {
            System.out.println(commandType.name() + " - " + commandType.getDescription() + " - Usage: " + commandType.getUsageDirections());
        }

        return success;
    }

    @Override
    public CommandResult executeOnSlave() throws IOException {
        // Not valid for execution on slave
        return error;
    }

    @Override
    public boolean validateSpecificArguments(List<String> arguments) {
        return true;
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        // No arguments exist
    }
}
