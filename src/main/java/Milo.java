import java.util.Scanner;

public class Milo {
    private static String LINE = "____________________________________________________________";
    private static String BANNER =
            " __  __   _       \n"
            + "|  \\/  (_) | ___  \n"
            + "| |\\/| | | |/ _ \\ \n"
            + "| |  | | | | (_) |\n"
            + "|_|  |_|_|_|\\___/\n";

    public static void printResponse(String string) {
        System.out.println(LINE + "\n" + "    " + string + "\n" + LINE + "\n");
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
            } else {
                printResponse(input);
            }
        }

        scanner.close();

    }

}
