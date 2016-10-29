package filesystems.safs.commands;

import java.util.ArrayList;
import java.util.List;

public enum CommandResult {
    success,
    error;

    private List<String> messages = new ArrayList<>();

    public void addSingleMessage(String simpleMessage) {
        messages.clear();
        messages.add(simpleMessage);
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
