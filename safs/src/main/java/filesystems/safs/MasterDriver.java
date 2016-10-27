package filesystems.safs;

import filesystems.safs.commands.CommandType;

import java.util.Arrays;
import java.util.Scanner;

public class MasterDriver {
    public static void main(String[] args)
    {
        Controller.CONTROLLER.initialize(args);

        System.out.println("SAFS up and running, please input your command:");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String rawCommand = scanner.nextLine();
            CommandType.executeCommand(rawCommand, true);
        }
    }


}
