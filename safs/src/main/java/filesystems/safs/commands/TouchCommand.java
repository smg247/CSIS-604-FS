package filesystems.safs.commands;

import filesystems.safs.NodeType;

class TouchCommand implements Command {

    public void execute(NodeType nodeType, String... arguments) {
        //TODO
    }

    public String getName() {
        return "touch";
    }

    public String getDescription() {
        return "Creates a new file";
    }

    public String getUsageDirections() {
        return "Supply the name of the file to be created. ex: touch test.txt";
    }
}
