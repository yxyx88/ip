package milo;

import milo.ui.Ui;

/** Starts Milo and connects its user interface to its command logic. */
public class Milo {
    private static final Ui UI = new Ui();

    /** Starts Milo's input loop. */
    public static void main(String[] args) {
        UI.showWelcome();

        Logic logic = new Logic();
        if (logic.hasLoadingError()) {
            System.out.println(logic.getLoadingError());
        } else {
            UI.showLoading();
        }

        UI.showGreeting();

        while (true) {
            String input = UI.readCommand();
            UI.showResponse(logic.execute(input));
            if (logic.isExitCommand(input)) {
                break;
            }
        }

        UI.close();
    }
}
