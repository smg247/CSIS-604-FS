package filesystems.safs;

import filesystems.safs.commands.CommandType;

public class MasterDriver {
    public static void main( String[] args )
    {
        outputDescriptionsAndDirectionsForAllCommands();
    }

    private static void outputDescriptionsAndDirectionsForAllCommands() {
        for (CommandType commandType : CommandType.values()) {
            System.out.println(commandType.getName() + " - " + commandType.getDescription() + " - Usage: " + commandType.getUsageDirections());
        }
    }
}
