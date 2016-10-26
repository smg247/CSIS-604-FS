package filesystems.safs.commands;

import filesystems.safs.NodeType;

interface Command {
    void execute(NodeType nodeType, String... arguments);
    String getName();
    String getDescription();
    String getUsageDirections();
}
