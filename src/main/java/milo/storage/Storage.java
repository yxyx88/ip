package milo.storage;

import milo.task.Task;
import milo.task.ToDo;
import milo.task.Deadline;
import milo.task.Event;
import milo.task.TaskList;
import milo.MiloException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

/** Loads tasks from and saves tasks to Milo's data file. */
public class Storage {
    // Built with Paths.get so the separator is correct on Windows, macOS, and Linux
    // instead of hardcoding "/".
    private static final String FILE_PATH = Paths.get(".", "data", "milo.txt").toString();

    /**
     * Loads all valid tasks from the data file.
     *
     * @return the loaded tasks, or an empty list when no data file exists
     * @throws MiloException if the data file cannot be accessed
     */
    public static TaskList loadTasks() throws MiloException {
        TaskList tasks = new TaskList();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return tasks;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                Task task = Storage.restoreTask(scanner.nextLine());

                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            throw new MiloException("-O- Oh no! I couldn't load your old tasks!");
        }

        return tasks;
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks tasks to persist
     * @throws MiloException if the data file cannot be written
     */
    public static void saveTasks(TaskList tasks) throws MiloException {
        try {
            File file = new File(FILE_PATH);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            FileWriter fw = new FileWriter(file);

            for (Task task : tasks.asList()) {
                String line = task.storageString();
                fw.write(line + System.lineSeparator());
            }

            fw.close();
        } catch (IOException e) {
            throw new MiloException("-O- Oh no! I can't save your tasks!");
        }
    }

    /**
     * Restores one task from its pipe-delimited storage representation.
     *
     * @param input serialized task line
     * @return the restored task, or {@code null} for a blank or corrupted line
     */
    public static Task restoreTask(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String[] inputs = input.split("\\s*\\|\\s*");

        if (inputs.length < 3) {
            System.out.println("Hmm... That's wierd... There's a corrupted line, I'm just gna skip it.");
            return null;
        }

        String taskType = inputs[0].trim();
        boolean isDone = inputs[1].trim().equals("1");
        String description = inputs[2].trim();
        Task task;

        try {
            if (taskType.equals("T")) {
                task = new ToDo(description);
            } else if (taskType.equals("D")) {
                task = new Deadline(description, inputs[3]);
            } else if (taskType.equals("E")) {
                task = new Event(description, inputs[3], inputs[4]);
            } else {
                System.out.println("Hmm... That's wierd... There's an unrecognised task-type, I'm just gna skip it.");
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException | DateTimeParseException e) {
            System.out.println("Hmm... That's wierd... There's a corrupted line, I'm just gna skip it.");
            return null;
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}
