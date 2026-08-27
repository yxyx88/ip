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

            String inputs[] = s.substring(8).split("/");

            if (inputs[0].trim().equals("")) {
                throw new MiloException("An empty deadline? What is that for? Doomscrolling?!?");
            } else if (inputs.length != 2){
                throw new MiloException("A deadline without a deadline isn't really a deadline is\n    it...");
            } else if (!inputs[1].trim().startsWith("by ")) {
                throw new MiloException("Follow the format for deadlines!");
            }

            String taskDescription = inputs[0].trim();
            String date = inputs[1].trim().substring(3);
            task = new Deadline(taskDescription, date);

        } else if (s.startsWith("event")) {
            if (s.equals("event")) {
                throw new MiloException("An empty event? What is that for? Doomscrolling?!?");
            }

            String inputs[] = s.substring(5).split("/");

            if (inputs[0].trim().equals("")) {
                throw new MiloException("An empty event? What is that for? Doomscrolling?!?");
            } else if (inputs.length != 3) {
                throw new MiloException("Erm... An even has to start and end...");
            } else if (!inputs[1].trim().startsWith("from ") || !inputs[2].trim().startsWith("to ")) {
                throw new MiloException("Follow the format for events!");
            }

            String taskDescription = inputs[0].trim();
            String startDate = inputs[1].trim().substring(5);
            String endDate = inputs[2].trim().substring(3);
            task = new Event(taskDescription, startDate, endDate);

        } else {
            throw new MiloException("Erm... I don't know what you mean...");
        }
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
