package filesystems.safs;

import filesystems.safs.commands.CommandResult;
import filesystems.safs.commands.CommandType;

import java.util.Arrays;
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
            String message = commandResult.getSimpleMessage();
            if (message != null) {
                System.out.println(message);
            }
        }
    }


}
