import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;

public class Storage {
    // Built with Paths.get so the separator is correct on Windows, macOS, and Linux
    // instead of hardcoding "/".
    private static final String FILE_PATH = Paths.get(".", "data", "milo.txt").toString();

    public static ArrayList<Task> loadTasks() throws MiloException {
        ArrayList<Task> tasks = new ArrayList<>();
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

    public static void saveTasks(ArrayList<Task> tasks) throws MiloException {
        try {
            File file = new File(FILE_PATH);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            FileWriter fw = new FileWriter(file);

            for (Task task : tasks) {
                String line = task.storageString();
                fw.write(line + System.lineSeparator());
            }

            fw.close();
        } catch (IOException e) {
            throw new MiloException("-O- Oh no! I can't save your tasks!");
        }
    }

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
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Hmm... That's wierd... There's a corrupted line, I'm just gna skip it.");
            return null;
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}