package filesystems.safs.commands;

import filesystems.safs.storageRepresentations.Node;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class TouchCommand extends Command {
    @Override
    public String executeOnMaster(String... arguments) throws IOException {
        Node node = controller.determineNodeToReceiveNewFile();
        String fileName = arguments[0];
        System.out.println("Sending " + fileName + " to " + node.toString() + ":" + node.getPort());

        Socket socket = new Socket(node.getAddress(), node.getPort());
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(CommandType.touch.getName() + " " + fileName);
        printWriter.println(".");

        String line = inputReader.readLine(); //todo: maybe its not getting this result in time?
        if (SUCCESS.equals(line)) { // Notify the Controller that this node contains the new file
            node.addFile(fileName);
        } else {
            System.out.println("Something went wrong while attempting to create the file remotely.");
        }

        return line;
    }

    @Override
    public String executeOnSlave(String... arguments) throws IOException {
        String fileName = arguments[0];
        Path path = Paths.get(fileName);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.createFile(path);

        return SUCCESS;
    }

    @Override
    public String getName() {
        return "touch";
    }

    @Override
    public String getDescription() {
        return "Creates a new file";
    }

    @Override
    public String getUsageDirections() {
        return "Supply the name of the file to be created. ex: touch test.txt";
    }

    @Override
    public boolean hasValidArguments(String... arguments) {
        return arguments != null && arguments.length == 1;
    }
}
