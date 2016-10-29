package filesystems.safs.commands;

import java.io.IOException;
import java.util.List;

import static filesystems.safs.commands.CommandResult.error;
import static filesystems.safs.commands.CommandResult.success;

class MVCommand extends Command {
    private String fromFileName;
    private String toFileName;

    @Override
    public CommandResult executeOnMaster() throws IOException {
        // Move is really just a copy followed by removing the original file
        CommandResult commandResult = CommandType.cp.executeOnMaster(new String[]{fromFileName, toFileName});
        if (success == commandResult) {
            CommandResult rmResult = CommandType.rm.executeOnMaster(fromFileName);
            return rmResult;
        } else {
            return commandResult;
        }
    }

    @Override
    public CommandResult executeOnSlave() throws IOException {
        // Not valid for execution on slave
        return error;
    }

    @Override
    public boolean validateSpecificArguments(List<String> arguments) {
        return arguments.size() == 2;
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        //TODO
    }
}
