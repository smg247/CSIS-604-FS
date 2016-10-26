package filesystems.safs.commands;

import filesystems.safs.NodeType;

class CPCommand implements Command {
    public void execute(NodeType nodeType, String... arguments) {
        //TODO
    }

    public String getName() {
        return "cp";
    }

    public String getDescription() {
        return "Copies a file to a new location";
    }

    public String getUsageDirections() {
        return "Supply the file name of the file to be copied, and the location of which to copy it. ex: cp test.txt testCopy.txt";
    }
}
