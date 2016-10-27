package filesystems.safs.commands;

import filesystems.safs.Controller;

import java.io.IOException;

abstract class Command {
    protected final static String SUCCESS = "SUCCESS"; // todo: this really needs to be an enum
    protected final static String ERROR = "ERROR";
    protected Controller controller = Controller.CONTROLLER;

    abstract String executeOnMaster(String... arguments) throws IOException;
    abstract String executeOnSlave(String... arguments) throws IOException;
    abstract String getName();
    abstract String getDescription();
    abstract String getUsageDirections();
    abstract boolean hasValidArguments(String... arguments);
}
