package filesystems.safs.commands;


import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.util.Arrays;

public enum CommandType {
    help(new HelpCommand()),
    ls(new LSCommand()),
    touch(new TouchCommand()),
    mkdir(new MKDIRCommand()),
    mv(new MVCommand()),
    cp(new CPCommand()),
    rm(new RMCommand());


    @Nullable
    private static CommandType getCommandTypeByName(String name) {
        for (CommandType commandType : values()) {
            if (name.equals(commandType.getName())) {
                return commandType;
            }
        }

        return null;
    }

    public static CommandResult executeCommand(String rawCommand, boolean isMaster) {
        String[] commandWithArguments = rawCommand.split("\\s+");
        CommandType commandType = null;
        if (commandWithArguments.length > 0) {
            commandType = getCommandTypeByName(commandWithArguments[0]);
        }
        if (commandType == null) {
            System.out.println("Invalid Command!");
            commandType = help;
        }

        String[] arguments = null;
        if (commandWithArguments.length > 1) {
            arguments = Arrays.copyOfRange(commandWithArguments, 1, commandWithArguments.length);
        }

        if (isMaster) {
            return commandType.executeOnMaster(arguments);
        } else {
            return commandType.executeOnSlave(arguments);
        }
    }


    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public CommandResult executeOnMaster(String... arguments) {
        if (command.hasValidArguments(arguments)) {
            try {
                return command.executeOnMaster(arguments);
            } catch (IOException e) {
                e.printStackTrace();
                return CommandResult.error;
            }
        } else {
            System.out.println("Invalid Command!");
            return help.executeOnMaster();
        }
    }

    public CommandResult executeOnSlave(String... arguments) {
        if (command.hasValidArguments(arguments)) {
            try {
                return command.executeOnSlave(arguments);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return CommandResult.error;
    }

    public String getName() {
        return command.getName();
    }

    public String getDescription() {
        return command.getDescription();
    }

    public String getUsageDirections() {
        return command.getUsageDirections();
    }
}
