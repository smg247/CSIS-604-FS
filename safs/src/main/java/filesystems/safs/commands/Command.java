package filesystems.safs.commands;

import filesystems.safs.Controller;

import java.io.IOException;

abstract class Command {
    protected Controller controller = Controller.CONTROLLER;

    abstract CommandResult executeOnMaster(String... arguments) throws IOException;
    abstract CommandResult executeOnSlave(String... arguments) throws IOException;
    abstract String getName();
    abstract String getDescription();
    abstract String getUsageDirections();
    abstract boolean hasValidArguments(String... arguments);

}
