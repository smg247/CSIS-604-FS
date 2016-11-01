package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.storageRepresentations.Node;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

abstract class CreationCommand extends Command {
    protected String path;

    @Override
    public CommandResult executeOnMaster() throws IOException {
        CommandResult commandResult = checkIfObjectAlreadyExists();
        if (commandResult.isSuccessful()) {
            List<Node> nodes = controller.determineNodesToReceiveNewFileOrDirectory();
            for (Node node : nodes) {
                String pathNameForSlave = node.getFullyQualifiedHomeDirectoryName() + path;
                System.out.println("Sending " + path + " to " + node.toString() + ":" + node.getPort());

                List<String> response = sendMessageToSlaveNode(node, Arrays.asList(commandType.name() + " " + pathNameForSlave));
                if (response != null) {
                    notifyRespectiveNodeOfObjectAddition(node);
                } else {
                    return CommandResult.forError();
                }
            }

            return CommandResult.forSuccess();
        } else {
            return commandResult;
        }
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        path = arguments.get(0);
    }

    protected abstract void notifyRespectiveNodeOfObjectAddition(Node node);
    protected abstract CommandResult checkIfObjectAlreadyExists();
}
