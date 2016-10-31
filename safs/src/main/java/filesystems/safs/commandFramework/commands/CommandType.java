package filesystems.safs.commandFramework.commands;


import com.sun.istack.internal.Nullable;
import filesystems.safs.commandFramework.CommandResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CommandType {
    help(HelpCommand.class, "Usage information", "Simply use the command to receive all possible commands and their usages"),
    ls(LSCommand.class, "Lists the contents of the current directory", "Used by simply typing ls"),
    touch(TouchCommand.class, "Creates a new file", "Supply the name of the file to be created. ex: touch test.txt"),
    mkdir(MKDIRCommand.class, "Creates a new directory", "Simply supply the directory name to be created. ex: mkdir directory"),
    mv(MVCommand.class, "Moves a file to a new location", "Supply the file name of the file to be moved, and the location of which to move it. ex: mv test.txt renamed.txt"),
    cp(CPCommand.class, "Copies a file to a new location", "Supply the file name of the file to be copied, and the location of which to copy it. ex: cp test.txt testCopy.txt"),
    rm(RMCommand.class, "Removes the file or directory supplied", "Supply the file name or directory name to remove. ex: rm sample.txt");


    @Nullable
    private static CommandType getCommandTypeByName(String name) {
        for (CommandType commandType : values()) {
            if (name.equals(commandType.name())) {
                return commandType;
            }
        }

        return null;
    }

    public static CommandResult executeCommand(String rawCommand, boolean isMaster) {
        return executeCommand(rawCommand, isMaster, new ArrayList<>());
    }

    public static CommandResult executeCommand(String rawCommand, boolean isMaster, List<String> additionalInformationForCommand) {
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
            return commandType.executeOnSlave(additionalInformationForCommand, arguments);
        }
    }


    private Class <? extends Command> commandClass;
    private String description;
    private String usageDirections;

    CommandType(Class<? extends Command> commandClass, String description, String usageDirections) {
        this.commandClass = commandClass;
        this.description = description;
        this.usageDirections = usageDirections;
    }

    public CommandResult executeOnMaster(String... arguments) {
        CommandResult commandResult = CommandResult.forError(); // Erroneous until proven successful
        try {
            Command command = commandClass.newInstance();
            commandResult = command.validateAndInitializeArguments(arguments);
            if (commandResult.isSuccessful()) {
                try {
                    return command.executeOnMaster();
                } catch (IOException e) {
                    e.printStackTrace();
                    return CommandResult.forError();
                }
            } else {
                commandResult.addMessages(Arrays.asList("Invalid Arguments for command!", usageDirections));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return commandResult;
    }

    public CommandResult executeOnSlave(List<String> additionalInformation, String... arguments) {
        CommandResult commandResult = CommandResult.forError(); // Erroneous until proven successful
        try {
            Command command = commandClass.newInstance();
            commandResult = command.validateAndInitializeArguments(arguments);
            if (commandResult.isSuccessful()) {
                try {
                    return command.executeOnSlave(additionalInformation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return commandResult;
    }

    public String getDescription() {
        return description;
    }

    public String getUsageDirections() {
        return usageDirections;
    }
}
