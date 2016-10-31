package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.commandFramework.DashedCommandArgument;

import java.io.IOException;
import java.util.List;


class MVCommand extends Command {
    private String fromFileName;
    private String toFileName;

    @Override
    public CommandResult executeOnMaster() throws IOException {
        // Move is really just a copy followed by removing the original file
        DashedCommandArgument[] dashedCommandArgumentArray = dashedCommandArguments.toArray(new DashedCommandArgument[0]);
        // TODO be sure to forward the dashedCommandArguments along
        CommandResult commandResult = CommandType.cp.executeOnMaster(new String[]{fromFileName, toFileName});
        if (CommandResult.CommandStatus.success == commandResult.getCommandStatus()) {
            CommandResult rmResult = CommandType.rm.executeOnMaster(fromFileName);
            return rmResult;
        } else {
            return commandResult;
        }
    }

    @Override
    public CommandResult executeOnSlave() throws IOException {
        // Not valid for execution on slave
        return CommandResult.forError();
    }

    @Override
    public CommandResult validateSpecificArguments(List<String> arguments) {
        if (arguments.size() == 2) {
            return CommandResult.forSuccess();
        } else {
            return CommandResult.forError();
        }
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        fromFileName = arguments.get(0);
        toFileName = arguments.get(1);
    }
}
