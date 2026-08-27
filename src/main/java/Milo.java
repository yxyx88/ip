import java.util.Scanner;
import java.util.ArrayList;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Milo {
    private static String LINE = "____________________________________________________________";
    private static String BANNER =
            " __  __   _       \n"
            + "|  \\/  (_) | ___  \n"
            + "| |\\/| | | |/ _ \\ \n"
            + "| |  | | | | (_) |\n"
            + "|_|  |_|_|_|\\___/\n";
    private static ArrayList<Task> tasks = new ArrayList<>(100);
    private static final Pattern DEADLINE_PATTERN =
            Pattern.compile("^deadline\\s+(.+?)\\s*/by\\s+(.+)$");
    private static final Pattern EVENT_PATTERN =
            Pattern.compile("^event\\s+(.+?)\\s*/from\\s+(.+?)\\s*/to\\s+(.+)$");

    public static void printResponse(String string) {
        System.out.println(LINE + "\n" + "    " + string + "\n" + LINE + "\n");
    }

    public static void printList() {
        String message = "Here is your to-do list:";
        for (int i = 0; i < tasks.size(); ++i) {
            String nextLine = String.format("    %d. %s", i + 1, tasks.get(i));
            message = message + "\n" + nextLine;
        }
        printResponse(message);
    }

    public static void printTaskAdded(Task task) {
        String message = String.format(
                "Ok, I've added the following task:\n" +
                "      %s\n" +
                "    You've got %d tasks in your list!", task.toString(), tasks.size());
        printResponse(message);
    }

    public static void handleTask(String s) throws MiloException {
        Task task;

        if (s.startsWith("todo")) {
            if (s.equals("todo")) {
                throw new MiloException("An empty todo? What is that for? Doomscrolling?!?");
            }

            String taskDescription = s.substring(4).trim();

            if (taskDescription.equals("")) {
                throw new MiloException("An empty todo? What is that for? Doomscrolling?!?");
            }

            task = new ToDo(taskDescription);

        } else if (s.startsWith("deadline")) {
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
                    // Keep the original message for a description with no deadline part.
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
                task = new Deadline(taskDescription, date);
            } catch (DateTimeParseException e) {
                throw invalidDateMessage();
            }

        } else if (s.startsWith("event")) {
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
                    // Keep the original message when the event has no complete range.
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
                task = new Event(taskDescription, startDate, endDate);
            } catch (DateTimeParseException e) {
                throw invalidDateMessage();
            }

        } else {
            throw new MiloException("Erm... I don't know what you mean...");
        }
        tasks.add(task);
        Storage.saveTasks(tasks);
        printTaskAdded(task);
    }

    private static MiloException invalidDateMessage() {
        return new MiloException("I couldn't understand that date. Use yyyy-MM-dd or yyyy-MM-dd HHmm (for example, 2019-10-15 1800), or d/M/yyyy HHmm.");
    }

    public static void handleMark(String s, boolean markDone) throws MiloException, NumberFormatException {
        s = s.trim();
        if (s.equals("")) {
            throw new MiloException("Am I supposed to read your mind?");
        }

        int idx = Integer.parseInt(s) - 1;

        if (idx < 0) {
            throw new MiloException("There can't be a negative task number!");
        } else if (idx >= tasks.size()) {
            throw new MiloException("You don't even have that many tasks!");
        } else {
            Task task = tasks.get(idx);
            if (markDone) {
                task.markAsDone();
                Storage.saveTasks(tasks);
                printResponse("Yay! I've marked this task as done!\n" +
                        "      " + task.toString());
            } else {
                task.markAsUndone();
                Storage.saveTasks(tasks);
                printResponse("Hmm.... Why was it marked as done in the first place?\n" +
                        "      " + task.toString());
            }
        }
    }

    public static void handleDeletion(String s) throws MiloException, NumberFormatException {
        s = s.trim();
        String message;
        if (s.equals("")) {
            throw new MiloException("Ok, deleting nothing!");
        } else if (s.equals("all")) {
            tasks = new ArrayList<>();
            Storage.saveTasks(tasks);
            message = String.format("POOOOOFFF\n");
            message += "    Your to-do list is gone! Sure hope you meant that!";
        } else {
            int idx = Integer.parseInt(s) - 1;

            if (idx < 0) {
                throw new MiloException("There can't be a negative task number!");
            } else if (idx >= tasks.size()) {
                throw new MiloException("You don't even have that many tasks!");
            } else {
                Task task = tasks.get(idx);
                tasks.remove(idx);
                Storage.saveTasks(tasks);
                message = String.format("Ok, I've removed this task:\n", idx);
                message += String.format("      %s\n", task.toString());
                message += String.format("    You've got %d tasks in your list!", tasks.size());
            }
        }
        printResponse(message);
    }

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(BANNER);

        try {
            tasks = Storage.loadTasks();
            System.out.println(LINE);
            System.out.println("Retrieving old tasks...");
            System.out.println(LINE);
        } catch (MiloException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Hey there! My name is Milo.\n"
                + "How can I help you today?\n"
                + LINE
                + "\n");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            try {
                if (input.equals("bye")) {
                    printResponse("Bye bye. Hope to see you soon!");
                    break;
                } else if (input.equals("list")) {
                    printList();
                } else if (input.startsWith("mark")) {
                    handleMark(input.substring(4), true);
                } else if (input.startsWith("unmark")) {
                    handleMark(input.substring(6), false);
                } else if (input.startsWith("delete")) {
                    handleDeletion(input.substring(6));
                } else {
                    handleTask(input);
                }
            } catch (MiloException e) {
                printResponse(e.getMessage());
            } catch (NumberFormatException e) {
                printResponse("Give me a valid task number!");
            }
        }

        scanner.close();

    }

}
