package filesystems.safs.commandFramework;

import java.util.ArrayList;
import java.util.List;

public class CommandResult {
    private CommandStatus commandStatus;
    private List<String> messages;


    public CommandResult(String commandStatus) {
        this(CommandStatus.valueOf(commandStatus));
    }

    public CommandResult(CommandStatus commandStatus) {
        this.commandStatus = commandStatus;
        messages = new ArrayList<>();
    }

    public void addSingleMessage(String simpleMessage) {
        messages.add(simpleMessage);
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public CommandStatus getCommandStatus() {
        return commandStatus;
    }

    public enum CommandStatus {
        success,
        error;
    }
}
