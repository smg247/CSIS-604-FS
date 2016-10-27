package filesystems.safs.commands;

import filesystems.safs.Controller;
import filesystems.safs.storageRepresentations.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

abstract class Command {
    protected Controller controller = Controller.CONTROLLER;

    /**
     * @param slaveNode         Node to send the message to
     * @param linesOfMessage    The individual lines of the message
     * @return                  The lines of the response from the slaveNode
     * @throws IOException
     */
    protected List<String> sendMessageToSlaveNode(Node slaveNode, List<String> linesOfMessage) throws IOException {
        Socket socket = new Socket(slaveNode.getAddress(), slaveNode.getPort());
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        for (String line : linesOfMessage) {
            printWriter.println(line);
        }

        printWriter.println(".");

        List<String> linesOfResponse = new ArrayList<>();
        String line;
        while((line = inputReader.readLine()) != null && !".".equals(line)) {
            linesOfResponse.add(line);
        }

        socket.close();

        return linesOfResponse;
    }

    abstract CommandResult executeOnMaster(String... arguments) throws IOException;
    abstract CommandResult executeOnSlave(String... arguments) throws IOException;
    abstract String getName();
    abstract String getDescription();
    abstract String getUsageDirections();
    abstract boolean hasValidArguments(String... arguments);

}
