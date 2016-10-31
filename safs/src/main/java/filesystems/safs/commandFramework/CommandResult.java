package filesystems.safs.commandFramework;

import java.util.ArrayList;
import java.util.List;

public class CommandResult {
    private CommandStatus commandStatus;
    private List<String> messages;


    public static CommandResult forSuccess() {
        return new CommandResult(CommandStatus.success);
    }

    public static CommandResult forError() {
        return new CommandResult(CommandStatus.error);
    }


    public CommandResult(String commandStatus) {
        this(CommandStatus.valueOf(commandStatus));
    }

    private CommandResult(CommandStatus commandStatus) {
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

    public void addMessages(List<String> messages) {
        this.messages.addAll(messages);
    }

    public CommandStatus getCommandStatus() {
        return commandStatus;
    }

    public boolean isSuccessful() {
        return commandStatus == CommandStatus.success;
    }

    public enum CommandStatus {
        success,
        error;
    }
}
