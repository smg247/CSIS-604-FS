package filesystems.safs;

import filesystems.safs.commands.CommandResult;
import filesystems.safs.commands.CommandType;

import java.util.List;
import java.util.Scanner;

public class MasterDriver {
    public static void main(String[] args)
    {
        Controller.CONTROLLER.initialize(true, args); //TODO: this will need to be updated when we deploy to VM's

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
