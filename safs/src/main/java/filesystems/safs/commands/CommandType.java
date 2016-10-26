package filesystems.safs.commands;


import com.sun.istack.internal.Nullable;
import filesystems.safs.NodeType;

public enum CommandType {
    touch(new TouchCommand()),
    mv(new MVCommand()),
    cp(new CPCommand());


    @Nullable
    public static CommandType getCommandTypeByName(String name) {
        for (CommandType commandType : values()) {
            if (name.equals(commandType.getName())) {
                return commandType;
            }
        }

        return null;
    }


    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public void execute(NodeType nodeType, String... arguments) {
        command.execute(nodeType, arguments);
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
