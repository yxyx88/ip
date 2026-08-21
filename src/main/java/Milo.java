import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class Milo {
    private static String LINE = "____________________________________________________________";
    private static String BANNER =
            " __  __   _       \n"
            + "|  \\/  (_) | ___  \n"
            + "| |\\/| | | |/ _ \\ \n"
            + "| |  | | | | (_) |\n"
            + "|_|  |_|_|_|\\___/\n";
    private static ArrayList<Task> tasks = new ArrayList<>(100);

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

    // assume all inputs are correct and follow the proper format
    public static void handleTask(String s) {
        Task task;
        if (s.startsWith("todo ")) {
            task = new ToDo(s.substring(5).trim());
        } else if (s.startsWith("deadline ")) {
            String inputs[] = s.substring(9).split("/");
            inputs[1] = inputs[1].substring(3);
            task = new Deadline(inputs[0].trim(), inputs[1].trim());
        } else {
            // event case
            String inputs[] = s.substring(6).split("/");
            inputs[1] = inputs[1].substring(5);
            inputs[2] = inputs[2].substring(3);
            task = new Event(inputs[0].trim(), inputs[1].trim(), inputs[2].trim());
        }
        tasks.add(task);
        printTaskAdded(task);
    }

    public static void handleMark(String s, boolean markDone) {
        int idx;
        try {
            idx = Integer.parseInt(s) - 1;
        } catch (NumberFormatException e) {
            printResponse("Give me a valid task number!");
            return;
        }

        if (idx < 0) {
            printResponse("There can't be a negative task number!");
        } else if (idx >= tasks.size()) {
            printResponse("You don't even have that many tasks!");
        } else {
            Task task = tasks.get(idx);
            if (markDone) {
                task.markAsDone();
                printResponse("Yay! I've marked this task as done!\n" +
                        "      " + task.toString());
            } else {
                task.markAsUndone();
                printResponse("Hmm.... Why was it marked as done in the first place?\n" +
                        "      " + task.toString());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hey there! My name is Milo.\n"
                + "How can I help you today?\n"
                + LINE
                + "\n");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            // assume correct inputs/ commands
            if (input.equals("bye")) {
                printResponse("Bye bye. Hope to see you soon!");
                break;
            } else if (input.equals("list")) {
                printList();
            } else if (input.startsWith("mark ")) {
                handleMark(input.substring(5), true);
            } else if (input.startsWith("unmark ")) {
                handleMark(input.substring(7), false);
            } else {
                handleTask(input);
            }
        }

        scanner.close();

    }

}
