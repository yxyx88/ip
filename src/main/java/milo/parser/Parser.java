package milo.parser;

import milo.task.Task;
import milo.task.ToDo;
import milo.task.Deadline;
import milo.task.Event;
import milo.MiloException;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Parser {
    private static final Pattern DEADLINE_PATTERN =
            Pattern.compile("^deadline\\s+(.+?)\\s*/by\\s+(.+)$");
    private static final Pattern EVENT_PATTERN =
            Pattern.compile("^event\\s+(.+?)\\s*/from\\s+(.+?)\\s*/to\\s+(.+)$");

    private Parser() {
        // Utility class; do not instantiate.
    }

    public static Task parseTask(String s) throws MiloException {
        if (s.startsWith("todo")) {
            if (s.equals("todo")) {
                throw new MiloException("An empty todo? What is that for? Doomscrolling?!?");
            }

            String taskDescription = s.substring(4).trim();
            if (taskDescription.equals("")) {
                throw new MiloException("An empty todo? What is that for? Doomscrolling?!?");
            }
            return new ToDo(taskDescription);
        }

        if (s.startsWith("deadline")) {
            if (s.equals("deadline")) {
                throw new MiloException("An empty deadline? What is that for? Doomscrolling?!?");
            }

            Matcher matcher = DEADLINE_PATTERN.matcher(s);
            if (!matcher.matches()) {
                String remainder = s.substring(8).trim();
                if (remainder.isEmpty()) {
                    throw new MiloException("An empty deadline? What is that for? Doomscrolling?!?");
                }
                if (!remainder.contains("/")) {
                    throw new MiloException("A deadline without a deadline isn't really a deadline is\n    it...");
                }
                if (!remainder.contains("/by")) {
                    throw new MiloException("Follow the format for deadlines: deadline description /by yyyy-MM-dd HHmm");
                }
                throw invalidDateMessage();
            }

            String taskDescription = matcher.group(1).trim();
            String date = matcher.group(2).trim();
            if (taskDescription.equals("")) {
                throw new MiloException("An empty deadline? What is that for? Doomscrolling?!?");
            }
            try {
                return new Deadline(taskDescription, date);
            } catch (DateTimeParseException e) {
                throw invalidDateMessage();
            }
        }

        if (s.startsWith("event")) {
            if (s.equals("event")) {
                throw new MiloException("An empty event? What is that for? Doomscrolling?!?");
            }

            Matcher matcher = EVENT_PATTERN.matcher(s);
            if (!matcher.matches()) {
                String remainder = s.substring(5).trim();
                if (remainder.isEmpty()) {
                    throw new MiloException("An empty event? What is that for? Doomscrolling?!?");
                }
                if (!remainder.contains("/from") || !remainder.contains("/to")) {
                    throw new MiloException("Erm... An even has to start and end...");
                }
                throw new MiloException("Follow the format for events: event description /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
            }

            String taskDescription = matcher.group(1).trim();
            String startDate = matcher.group(2).trim();
            String endDate = matcher.group(3).trim();
            if (taskDescription.equals("")) {
                throw new MiloException("An empty event? What is that for? Doomscrolling?!?");
            }
            try {
                return new Event(taskDescription, startDate, endDate);
            } catch (DateTimeParseException e) {
                throw invalidDateMessage();
            }
        }

        throw new MiloException("Erm... I don't know what you mean...");
    }

    private static MiloException invalidDateMessage() {
        return new MiloException("I couldn't understand that date. Use yyyy-MM-dd or yyyy-MM-dd HHmm (for example, 2019-10-15 1800), or d/M/yyyy HHmm.");
    }
}
