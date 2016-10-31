package filesystems.safs.slave;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.commandFramework.commands.CommandType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SlaveDriver {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

                    String rawCommand = inputReader.readLine();
                    String line;
                    while ((line = inputReader.readLine()) != null && !".".equals(line)) {
                        //TODO
                    }

                    CommandResult commandResult = CommandType.executeCommand(rawCommand, false);
                    printWriter.println(commandResult.getCommandStatus().name());
                    for (String lineInMessage : commandResult.getMessages()) {
                        printWriter.println(lineInMessage);
                    }
                    printWriter.println(".");
                    System.out.println("Just received a commandResult of " + commandResult.getCommandStatus() + " while attempting to " + rawCommand);

                } finally {
                    System.out.println("Closing socket.");
                    socket.close();
                }
            }
        } finally {
            serverSocket.close();
        }
    }
}