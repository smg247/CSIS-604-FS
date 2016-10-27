package filesystems.safs.commands;

import java.util.List;

public enum CommandResult {
    success,
    error;

    private String simpleMessage;
    private List<String> multiLinedMessage;

    public String getSimpleMessage() {
        return simpleMessage;
    }

    public void setSimpleMessage(String simpleMessage) {
        this.simpleMessage = simpleMessage;
    }

    public List<String> getMultiLinedMessage() {
        return multiLinedMessage;
    }

    public void setMultiLinedMessage(List<String> multiLinedMessage) {
        this.multiLinedMessage = multiLinedMessage;
    }
}
