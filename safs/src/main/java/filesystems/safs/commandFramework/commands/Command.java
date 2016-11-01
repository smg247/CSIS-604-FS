package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.commandFramework.DashedCommandArgument;
import filesystems.safs.master.Controller;
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
    protected CommandType commandType;


    static Command forCommandType(CommandType commandType) throws Exception {
        Command command = commandType.getCommandClass().newInstance();
        command.setCommandType(commandType);
        return command;
    }

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
        if (commandResult.isSuccessful()) {
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

    CommandResult validateAndInitializeArguments(String... arguments) {
        List<String> regularArguments = new ArrayList<>();
        List<DashedCommandArgument> availableArgumentsForCommandType = DashedCommandArgument.getAvailableArgumentsForCommandType(commandType);
        if (arguments != null) {
            for (String argument : arguments) {
                if (argument.contains("-")) {
                    DashedCommandArgument dashedCommandArgument = DashedCommandArgument.fromString(argument);
                    if (availableArgumentsForCommandType.contains(dashedCommandArgument)) {
                        dashedCommandArguments.add(dashedCommandArgument);
                    }
                } else {
                    regularArguments.add(argument);
                }
            }
        }

        CommandResult commandResult = validateSpecificArguments(regularArguments);
        if (commandResult.isSuccessful()) {
            initializeSpecificArguments(regularArguments);
        }
        return commandResult;
    }

    void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    abstract CommandResult executeOnMaster() throws IOException;
    abstract CommandResult executeOnSlave(List<String> additionalInformation) throws IOException;
    protected abstract CommandResult validateSpecificArguments(List<String> arguments);
    protected abstract void initializeSpecificArguments(List<String> arguments);

}
