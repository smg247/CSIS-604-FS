package filesystems.safs.commands;

import filesystems.safs.storageRepresentations.Node;

import java.io.IOException;
import java.util.List;

class RMCommand extends Command {
    @Override
    protected List<String> sendMessageToSlaveNode(Node slaveNode, List<String> linesOfMessage) throws IOException {
        return super.sendMessageToSlaveNode(slaveNode, linesOfMessage);
    }

    @Override
    CommandResult executeOnMaster(String... arguments) throws IOException {
        //TODO
        return null;
    }

    @Override
    CommandResult executeOnSlave(String... arguments) throws IOException {
        //TODO
        return null;
    }

    @Override
    String getName() {
        return "rm";
    }

    @Override
    String getDescription() {
        return "Removes the file or directory supplied";
    }

    @Override
    String getUsageDirections() {
        return "Supply the file name or directory name to remove. ex: rm sample.txt";
    }

    @Override
    boolean hasValidArguments(String... arguments) {
        return arguments != null && arguments.length == 1;
    }
}
