package filesystems.safs.commandFramework.commands;


import filesystems.safs.commandFramework.CommandResult;

import java.io.IOException;
import java.util.List;


class HelpCommand extends Command {
    @Override
    public CommandResult executeOnMaster() throws IOException {
        for (CommandType commandType : CommandType.values()) {
            System.out.println(commandType.createFullUsageDirections());
        }

        return CommandResult.forSuccess();
    }

    @Override
    public CommandResult executeOnSlave(List<String> additionalInformation) throws IOException {
        // Not valid for execution on slave
        return CommandResult.forError();
    }

    @Override
    public CommandResult validateSpecificArguments(List<String> arguments) {
        return CommandResult.forSuccess();
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        // No arguments exist
    }
}
