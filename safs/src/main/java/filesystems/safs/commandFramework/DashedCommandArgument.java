package filesystems.safs.commandFramework;

public enum DashedCommandArgument {
    l, // Local
    r, // Remote
    n, // No output
    u; // Update file information

    public String getNameWithDash() {
        return "-" + this.name();
    }
}
