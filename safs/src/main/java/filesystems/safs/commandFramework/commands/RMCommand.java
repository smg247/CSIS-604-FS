package filesystems.safs.commandFramework.commands;

import filesystems.safs.commandFramework.CommandResult;
import filesystems.safs.storageRepresentations.Node;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

class RMCommand extends Command {
    private String path;

    @Override
    CommandResult executeOnMaster() throws IOException {
        boolean removingDirectory;
        List<Node> slaveNodesWhichContainObject;
        if (path.contains(".")) {
            removingDirectory = false;
            slaveNodesWhichContainObject = controller.getNodesContainingFile(path);
        } else {
            removingDirectory = true;
            slaveNodesWhichContainObject = controller.getNodesContainingDirectory(path);
        }

        if (!slaveNodesWhichContainObject.isEmpty()) {
            for (Node node : slaveNodesWhichContainObject) {
                String pathNameForSlave = node.getFullyQualifiedHomeDirectoryName() + path;
                List<String> response = sendMessageToSlaveNode(node, Arrays.asList(CommandType.rm.name() + " " + pathNameForSlave));
                if (response != null) {
                    if (removingDirectory) {
                        node.removeDirectory(path);
                    } else {
                        node.removeFile(path);
                    }
                } else {
                    return CommandResult.forError();
                }
            }
        } else {
            CommandResult commandResult = CommandResult.forError();
            commandResult.addSingleMessage((removingDirectory ? "Directory" : "File") + " does not exist!");
            return commandResult;
        }

        return CommandResult.forSuccess();
    }

    @Override
    CommandResult executeOnSlave(List<String> additionalInformation) throws IOException {
        Path fileOrDirectory = Paths.get(path);
        if (fileOrDirectory.toFile().isDirectory()) {
            Files.walkFileTree(fileOrDirectory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            Files.delete(fileOrDirectory);
        }
        return CommandResult.forSuccess();
    }

    @Override
    protected CommandResult validateSpecificArguments(List<String> arguments) {
        if (arguments.size() == 1) {
            return CommandResult.forSuccess();
        } else {
            return CommandResult.forError();
        }
    }

    @Override
    protected void initializeSpecificArguments(List<String> arguments) {
        path = arguments.get(0);
    }
}
