package filesystems.safs.commandFramework;

public enum DashedCommandArgument {
    l, // Local
    r, // Remote
    n; // No output

    public String getNameWithDash() {
        return "-" + this.name();
    }
}