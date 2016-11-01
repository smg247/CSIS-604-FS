package filesystems.safs.commandFramework;

import filesystems.safs.commandFramework.commands.CommandType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DashedCommandArgument {
    l("Act within the File System (Default).", CommandType.mv, CommandType.cp), // Local
    r("Act from outside the File System", CommandType.mv, CommandType.cp), // Remote
    u("Force the File System to update its information.", CommandType.ls), // Update file information
    n("No output, this is unadvertised argument."); // No output


    private String information;
    private List<CommandType> validCommandTypes;


    DashedCommandArgument(String information, CommandType... validCommandTypes) {
        this.information = information;
        this.validCommandTypes = Arrays.asList(validCommandTypes);
    }

    public static List<DashedCommandArgument> getAvailableArgumentsForCommandType(CommandType commandType) {
        List<DashedCommandArgument> dashedCommandArguments = new ArrayList<>();
        for (DashedCommandArgument dashedCommandArgument : values()) {
            if (dashedCommandArgument.validCommandTypes.contains(commandType)) {
                dashedCommandArguments.add(dashedCommandArgument);
            }
        }

        return dashedCommandArguments;
    }

    public String getNameWithDash() {
        return "-" + this.name();
    }

    public String getInformation() {
        return information;
    }
}
