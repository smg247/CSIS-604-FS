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
    protected List<DashedCommandArgument> dashedCommandArguments = new ArrayList<>();

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

        CommandResult commandResult = new CommandResult(inputReader.readLine());
        if (CommandResult.CommandStatus.success == commandResult.getCommandStatus()) {
            List<String> linesOfResponse = new ArrayList<>();
            String line;
            while ((line = inputReader.readLine()) != null && !".".equals(line)) {
                linesOfResponse.add(line);
            }

            socket.close();

            return linesOfResponse;
        } else {
            System.out.println("ERROR"); //TODO: better error messaging
            return null;
        }
    }

    boolean validateAndInitializeArguments(String... arguments) {
        List<String> regularArguments = new ArrayList<>();
        if (arguments != null) {
            for (String argument : arguments) {
                if (argument.contains("-")) {
                    DashedCommandArgument dashedCommandArgument = DashedCommandArgument.valueOf(argument.substring(1));
                    dashedCommandArguments.add(dashedCommandArgument);
                } else {
                    regularArguments.add(argument);
                }
            }
        }

        if (validateSpecificArguments(regularArguments)) {
            initializeSpecificArguments(regularArguments);
            return true;
        } else {
            return false;
        }
    }

    abstract CommandResult executeOnMaster() throws IOException;
    abstract CommandResult executeOnSlave() throws IOException;
    protected abstract boolean validateSpecificArguments(List<String> arguments);
    protected abstract void initializeSpecificArguments(List<String> arguments);

}
