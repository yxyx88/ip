public class Deadline extends Task {
    private String deadlineDate;

    public Deadline(String description, String deadlineDate) {
        super(description);
        this.deadlineDate = deadlineDate;
    }

    @Override
    public String storageString() {
        return String.format("D | %s | %s", super.storageString(), this.deadlineDate);
    }

    @Override
    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(), this.deadlineDate);
    }
 }