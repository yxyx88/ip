import java.time.LocalDateTime;

public class Deadline extends Task {
    private final LocalDateTime deadlineDate;

    public Deadline(String description, String deadlineDate) {
        super(description);
        this.deadlineDate = DateTimeParser.parse(deadlineDate);
    }

    public Deadline(String description, LocalDateTime deadlineDate) {
        super(description);
        this.deadlineDate = deadlineDate;
    }

    public LocalDateTime getDeadlineDate() {
        return deadlineDate;
    }

    @Override
    public String storageString() {
        return String.format("D | %s | %s", super.storageString(),
                DateTimeParser.formatForStorage(deadlineDate));
    }

    @Override
    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(),
                DateTimeParser.formatForDisplay(deadlineDate));
    }
}
