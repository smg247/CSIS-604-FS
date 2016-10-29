package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.storageRepresentations.Node;

import java.io.IOException;
import java.util.List;

class RMCommand extends Command {
    @Override
    protected List<String> sendMessageToSlaveNode(Node slaveNode, List<String> linesOfMessage) throws IOException {
        return super.sendMessageToSlaveNode(slaveNode, linesOfMessage);
    }

    @Override
    CommandResult executeOnMaster() throws IOException {
        //TODO
        return null;
    }

    @Override
    CommandResult executeOnSlave() throws IOException {
        //TODO
        return null;
    }

    @Override
    protected boolean validateSpecificArguments(List<String> arguments) {
        return arguments.size() == 1;
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {

    }
}
