package milo;

import milo.ui.Ui;
import milo.task.Task;
import milo.task.TaskList;
import milo.parser.Parser;
import milo.storage.Storage;

public class Milo {
    private static TaskList tasks = new TaskList();
    private static Ui ui = new Ui();

    public static void printResponse(String string) {
        ui.showResponse(string);
    }

    public static void printList() {
        ui.showList(tasks);
    }

    public static void printTaskAdded(Task task) {
        ui.showTaskAdded(task, tasks.size());
    }

    public static void handleTask(String s) throws MiloException {
        Task task = Parser.parseTask(s);
        tasks.add(task);
        Storage.saveTasks(tasks);
        printTaskAdded(task);
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
                ui.showTaskMarked(task, true);
            } else {
                task.markAsUndone();
                Storage.saveTasks(tasks);
                ui.showTaskMarked(task, false);
            }
        }
    }

    public static void handleDeletion(String s) throws MiloException, NumberFormatException {
        s = s.trim();
        String message;
        if (s.equals("")) {
            throw new MiloException("Ok, deleting nothing!");
        } else if (s.equals("all")) {
            tasks.clear();
            Storage.saveTasks(tasks);
            message = "POOOOOFFF\n";
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
                message = "Ok, I've removed this task:\n";
                message += String.format("      %s\n", task);
                message += String.format("    You've got %d tasks in your list!", tasks.size());
            }
        }
        ui.showTasksDeleted(message);
    }

    public static void main(String[] args) {
        ui.showWelcome();

        try {
            tasks = Storage.loadTasks();
            ui.showLoading();
        } catch (MiloException e) {
            System.out.println(e.getMessage());
        }

        ui.showGreeting();

        while (true) {
            String input = ui.readCommand();

            try {
                if (input.equals("bye")) {
                    ui.showGoodbye();
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

        ui.close();
    }
}
