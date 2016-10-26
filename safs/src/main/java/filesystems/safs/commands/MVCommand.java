package filesystems.safs.commands;

import filesystems.safs.NodeType;

class MVCommand implements Command {
    public void execute(NodeType nodeType, String... arguments) {
        //TODO
    }

    public String getName() {
        return "mv";
    }

    public String getDescription() {
        return "Moves a file to a new location";
    }

    public String getUsageDirections() {
        return "Supply the file name of the file to be moved, and the location of which to move it. ex: mv test.txt renamed.txt";
    }
}
