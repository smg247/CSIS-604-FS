package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.storageRepresentations.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

abstract class CreationCommand extends Command {
    protected String path;

    @Override
    public CommandResult executeOnMaster() throws IOException {
        Node node = controller.determineNodeToReceiveNewFileOrDirectory();
        String pathNameForSlave = node.getFullyQualifiedHomeDirectoryName() + path;
        System.out.println("Sending " + path + " to " + node.toString() + ":" + node.getPort());

        List<String> response = sendMessageToSlaveNode(node, Arrays.asList(getSpecificCommandType().name() + " " + pathNameForSlave));
        if (response != null) {
            notifyRespectiveNodeOfObjectAddition(node);
            return CommandResult.forSuccess();
        } else {
            return CommandResult.forError();
        }
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        path = arguments.get(0);
    }

    protected abstract CommandType getSpecificCommandType();
    protected abstract void notifyRespectiveNodeOfObjectAddition(Node node);
}
