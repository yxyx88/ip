package milo.task;

import milo.parser.DateTimeParser;

import java.time.LocalDateTime;

public class Event extends Task {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Event(String description, String startDate, String endDate) {
        super(description);
        this.startDate = DateTimeParser.parse(startDate);
        this.endDate = DateTimeParser.parse(endDate);
    }

    public Event(String description, LocalDateTime startDate, LocalDateTime endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String storageString() {
        return String.format("E | %s | %s | %s", super.storageString(),
                DateTimeParser.formatForStorage(startDate), DateTimeParser.formatForStorage(endDate));
    }

    @Override
    public String toString() {
        return String.format("[E] %s (from: %s to: %s)", super.toString(),
                DateTimeParser.formatForDisplay(startDate), DateTimeParser.formatForDisplay(endDate));
    }
}
