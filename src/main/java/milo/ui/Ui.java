package milo.ui;

import milo.task.Task;
import milo.task.TaskList;

import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER =
            " __  __   _       \n"
            + "|  \\/  (_) | ___  \n"
            + "| |\\/| | | |/ _ \\ \n"
            + "| |  | | | | (_) |\n"
            + "|_|  |_|_|_|\\___/\n";
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
    }

    public void showLoading() {
        System.out.println(LINE);
        System.out.println("Retrieving old tasks...");
        System.out.println(LINE);
    }

    public void showGreeting() {
        System.out.println("Hey there! My name is Milo.");
        System.out.println("How can I help you today?");
        System.out.println(LINE);
    }

    public void showResponse(String string) {
        System.out.println(LINE);
        System.out.println("    " + string);
        System.out.println(LINE);
    }

    public void showList(TaskList tasks) {
        String message = "Here is your to-do list:";
        for (int i = 0; i < tasks.size(); ++i) {
            message += String.format("\n    %d. %s", i + 1, tasks.get(i));
        }
        showResponse(message);
    }

    public void showTaskAdded(Task task, int taskCount) {
        showResponse(String.format(
                "Ok, I've added the following task:\n" +
                "      %s\n" +
                "    You've got %d tasks in your list!", task, taskCount));
    }

    public void showTaskMarked(Task task, boolean markDone) {
        String message = markDone
                ? "Yay! I've marked this task as done!\n      " + task
                : "Hmm.... Why was it marked as done in the first place?\n      " + task;
        showResponse(message);
    }

    public void showTasksDeleted(String message) {
        showResponse(message);
    }

    public void showGoodbye() {
        showResponse("Bye bye. Hope to see you soon!");
    }

    /** Displays matching tasks or a message when no tasks match. */
    public void showSearchResults(TaskList tasks) {
        String message = "";
        if (tasks.size() == 0) {
            message = "You don't have any matching tasks :(";
        } else {
            message = "Here are the tasks I found:";
            for (int i = 0; i < tasks.size(); ++i) {
                message += String.format("\n    %d. %s", i + 1, tasks.get(i));
            }
        }
        showResponse(message);
    }

    public void close() {
        scanner.close();
    }
}
