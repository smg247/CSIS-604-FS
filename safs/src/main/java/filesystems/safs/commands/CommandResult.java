package filesystems.safs.commands;

public enum CommandResult {
    success,
    error;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
