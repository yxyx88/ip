package milo;

import milo.parser.Parser;
import milo.storage.Storage;
import milo.task.Task;
import milo.task.TaskList;

/**
 * Handles Milo's commands and task data.
 *
 * <p>This class deliberately returns messages instead of printing them. This keeps command
 * processing independent of the console user interface.</p>
 */
public class Logic {
    private final TaskList tasks;
    private final String loadingError;

    /** Loads the saved tasks and creates the command handler for them. */
    public Logic() {
        TaskList loadedTasks;
        String errorMessage = null;

        try {
            loadedTasks = Storage.loadTasks();
        } catch (MiloException e) {
            loadedTasks = new TaskList();
            errorMessage = e.getMessage();
        }

        this.tasks = loadedTasks;
        this.loadingError = errorMessage;
    }

    /** Creates the command handler using the tasks that were already loaded from storage. */
    public Logic(TaskList tasks) {
        this.tasks = tasks;
        this.loadingError = null;
    }

    /** Returns whether task loading failed during construction. */
    public boolean hasLoadingError() {
        return loadingError != null;
    }

    /** Returns the message from a task-loading failure, if one occurred. */
    public String getLoadingError() {
        return loadingError;
    }

    /** Processes one complete user command and returns the message that should be displayed. */
    public String execute(String input) {
        try {
            if (input.equals("bye")) {
                return "Bye bye. Hope to see you soon!";
            } else if (input.equals("list")) {
                return listTasks();
            } else if (input.startsWith("mark")) {
                return markTask(input.substring(4), true);
            } else if (input.startsWith("unmark")) {
                return markTask(input.substring(6), false);
            } else if (input.startsWith("delete")) {
                return deleteTask(input.substring(6));
            } else if (input.startsWith("find")) {
                return findTasks(input.substring(4));
            } else {
                return addTask(input);
            }
        } catch (MiloException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "Give me a valid task number!";
        }
    }

    /** Returns whether the input should end the application after its response is shown. */
    public boolean isExitCommand(String input) {
        return input.equals("bye");
    }

    /** Adds a parsed task, saves it, and returns the existing confirmation wording. */
    private String addTask(String input) throws MiloException {
        Task task = Parser.parseTask(input);
        tasks.add(task);
        Storage.saveTasks(tasks);
        return String.format("Ok, I've added the following task:\n"
                + "      %s\n"
                + "    You've got %d tasks in your list!", task, tasks.size());
    }

    /** Returns the numbered task list using the existing wording. */
    private String listTasks() {
        String message = "Here is your to-do list:";
        for (int i = 0; i < tasks.size(); i++) {
            message += String.format("\n    %d. %s", i + 1, tasks.get(i));
        }
        return message;
    }

    /** Marks or unmarks the selected task, saves it, and returns its confirmation. */
    private String markTask(String input, boolean markDone) throws MiloException {
        Task task = getTask(input, "Am I supposed to read your mind?");
        if (markDone) {
            task.markAsDone();
        } else {
            task.markAsUndone();
        }
        Storage.saveTasks(tasks);
        return markDone
                ? "Yay! I've marked this task as done!\n      " + task
                : "Hmm.... Why was it marked as done in the first place?\n      " + task;
    }

    /** Deletes a selected task or every task, then returns the existing confirmation. */
    private String deleteTask(String input) throws MiloException {
        input = input.trim();
        if (input.isEmpty()) {
            throw new MiloException("Ok, deleting nothing!");
        }
        if (input.equals("all")) {
            tasks.clear();
            Storage.saveTasks(tasks);
            return "POOOOOFFF\n    Your to-do list is gone! Sure hope you meant that!";
        }

        Task task = getTask(input, "Ok, deleting nothing!");
        int index = Integer.parseInt(input) - 1;
        tasks.remove(index);
        Storage.saveTasks(tasks);
        return String.format("Ok, I've removed this task:\n"
                + "      %s\n"
                + "    You've got %d tasks in your list!", task, tasks.size());
    }

    /** Searches descriptions for a keyword and returns the matching-task message. */
    private String findTasks(String keyword) throws MiloException {
        keyword = keyword.trim();
        if (keyword.isEmpty()) {
            throw new MiloException("Hmm... where would this <blank> belong?");
        }

        TaskList results = tasks.find(keyword);
        if (results.size() == 0) {
            return "You don't have any matching tasks :(";
        }

        String message = "Here are the tasks I found:";
        for (int i = 0; i < results.size(); i++) {
            message += String.format("\n    %d. %s", i + 1, results.get(i));
        }
        return message;
    }

    /** Validates a one-based task number and returns its task. */
    private Task getTask(String input, String emptyMessage) throws MiloException {
        input = input.trim();
        if (input.isEmpty()) {
            throw new MiloException(emptyMessage);
        }

        int index = Integer.parseInt(input) - 1;
        if (index < 0) {
            throw new MiloException("There can't be a negative task number!");
        }
        if (index >= tasks.size()) {
            throw new MiloException("You don't even have that many tasks!");
        }
        return tasks.get(index);
    }
}
