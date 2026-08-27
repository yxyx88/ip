public abstract class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public String storageString() {
        return String.format("%s | %s", isDone ? "1" : "0", this.description);
    }

    @Override
    public String toString() {
        String marker = isDone ? "X" : " ";
        return String.format("[%s] %s", marker, description);
    }
}
