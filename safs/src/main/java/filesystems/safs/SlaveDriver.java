package filesystems.safs;

import filesystems.safs.commands.CommandType;

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

                    String line = inputReader.readLine();
                    String commandResult = CommandType.executeCommand(line, false);

                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.print(commandResult);
                    System.out.println("Just recieved a commandResult of " + commandResult + " while attempting to " + line);

                } finally {
                    socket.close();
                }
            }
        } finally {
            serverSocket.close();
        }
    }
}
