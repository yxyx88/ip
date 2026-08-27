public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String storageString() {
        return String.format("T | %s", super.storageString());
    }

    @Override
    public String toString() {
        return String.format("[T] %s", super.toString());
    }
}