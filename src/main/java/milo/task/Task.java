package milo.task;

/** Represents a task description and its completed/not-completed state. */
public abstract class Task {
    /** Text describing the work to be done. */
    private String description;
    /** Whether this task has been completed. */
    private boolean isDone;

    /** Creates an incomplete task with the given description. */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsUndone() {
        this.isDone = false;
    }

    /** Returns the common task data used by persistent storage. */
    public String storageString() {
        return String.format("%s | %s", isDone ? "1" : "0", this.description);
    }

    /** Returns the task with its completion marker for user-facing output. */
    @Override
    public String toString() {
        String marker = isDone ? "X" : " ";
        return String.format("[%s] %s", marker, description);
    }
}
