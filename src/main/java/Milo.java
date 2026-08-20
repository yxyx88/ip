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
                Task task = new Task(input);
                tasks.add(task);
                printResponse("added: " + input);
            }
        }

        scanner.close();

    }

}
