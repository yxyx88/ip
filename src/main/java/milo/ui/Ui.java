package milo.ui;

import java.util.Scanner;

import milo.task.Task;
import milo.task.TaskList;

/** Handles Milo's console input and output. */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER =
            " __  __   _       \n"
            + "|  \\/  (_) | ___  \n"
            + "| |\\/| | | |/ _ \\ \n"
            + "| |  | | | | (_) |\n"
            + "|_|  |_|_|_|\\___/\n";
    private final Scanner scanner;

    /** Creates a console user interface backed by standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Reads the next command entered by the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays Milo's banner. */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
    }

    /** Displays the message shown while stored tasks are loaded. */
    public void showLoading() {
        System.out.println(LINE);
        System.out.println("Retrieving old tasks...");
        System.out.println(LINE);
    }

    /** Displays Milo's greeting and prompt introduction. */
    public void showGreeting() {
        System.out.println("Hey there! My name is Milo.");
        System.out.println("How can I help you today?");
        System.out.println(LINE);
    }

    /** Displays a response surrounded by Milo's standard separator. */
    public void showResponse(String string) {
        System.out.println(LINE);
        System.out.println("    " + string);
        System.out.println(LINE);
    }

    /** Displays all tasks with one-based list numbers. */
    public void showList(TaskList tasks) {
        String message = "Here is your to-do list:";
        for (int i = 0; i < tasks.size(); ++i) {
            message += String.format("\n    %d. %s", i + 1, tasks.get(i));
        }
        showResponse(message);
    }

    /** Displays confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        showResponse(String.format(
                "Ok, I've added the following task:\n" +
                "      %s\n" +
                "    You've got %d tasks in your list!", task, taskCount));
    }

    /** Displays confirmation that a task was marked or unmarked. */
    public void showTaskMarked(Task task, boolean markDone) {
        String message = markDone
                ? "Yay! I've marked this task as done!\n      " + task
                : "Hmm.... Why was it marked as done in the first place?\n      " + task;
        showResponse(message);
    }

    /** Displays confirmation after deleting tasks. */
    public void showTasksDeleted(String message) {
        showResponse(message);
    }

    /** Displays Milo's goodbye message. */
    public void showGoodbye() {
        showResponse("Bye bye. Hope to see you soon!");
    }

    /** Closes the console input scanner. */
    public void close() {
        scanner.close();
    }
}
