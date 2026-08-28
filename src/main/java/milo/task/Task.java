package milo.task;

import java.util.Locale;

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

    /**
     * Checks whether this task description contains a keyword, ignoring case.
     *
     * @param keyword text to search for
     * @return {@code true} when the description contains the keyword
     */
    public boolean containsKeyword(String keyword) {
        return description.toLowerCase(Locale.ROOT)
                .contains(keyword.toLowerCase(Locale.ROOT));
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
