package filesystems.safs.master;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.commandFramework.commands.CommandType;

import java.util.List;
import java.util.Scanner;

public class MasterDriver {
    public static void main(String[] args)
    {
        boolean testEnvironment = Boolean.parseBoolean(args[0]);
        Controller.CONTROLLER.initialize(testEnvironment);

        System.out.println("SAFS up and running, please input your command:");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String rawCommand = scanner.nextLine();
            CommandResult commandResult = CommandType.executeCommand(rawCommand, true);
            List<String> messages = commandResult.getMessages();
            for (String message : messages) {
                System.out.println(message);
            }
        }
    }


}
