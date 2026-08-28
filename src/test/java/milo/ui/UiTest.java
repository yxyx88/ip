package milo.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;

import milo.task.TaskList;
import milo.task.ToDo;

/** Tests Milo's user-facing search output. */
public class UiTest {

    /** Verifies matching tasks are displayed with one-based numbering. */
    @Test
    public void showSearchResults_matchingTasks_expectedNumberedOutput() {
        Ui ui = new Ui();
        TaskList matches = new TaskList(List.of(new ToDo("read book"), new ToDo("return book")));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(output));
            ui.showSearchResults(matches);
        } finally {
            System.setOut(originalOutput);
        }

        String response = output.toString(StandardCharsets.UTF_8);
        assertTrue(response.contains("Here are the tasks I found:"));
        assertTrue(response.contains("1. [T] [ ] read book"));
        assertTrue(response.contains("2. [T] [ ] return book"));
    }

    /** Verifies the UI reports when a search has no matching tasks. */
    @Test
    public void showSearchResults_noMatches_expectedNoMatchMessage() {
        Ui ui = new Ui();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(output));
            ui.showSearchResults(new TaskList());
        } finally {
            System.setOut(originalOutput);
        }

        assertTrue(output.toString(StandardCharsets.UTF_8)
                .contains("You don't have any matching tasks :("));
    }
}
