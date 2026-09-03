package milo.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import milo.Milo;

/** Controller for Milo's main GUI. */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private Milo milo;
    private final Image userImage = loadImage("/images/User.png");
    private final Image miloImage = loadImage("/images/Milo.png");

    /** Binds the scroll pane to the latest dialog. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects Milo after the FXML view has loaded. */
    public void setMilo(Milo newMilo) {
        milo = newMilo;
        dialogContainer.getChildren().add(DialogBox.getMiloDialog(milo.getGreeting(), miloImage));
    }

    /** Adds the user message and Milo's response to the chat, then clears the text field. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = formatForDialog(milo.getResponse(input));
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMiloDialog(response, miloImage));
        userInput.clear();
    }

    /** Removes indentation that was only needed by the legacy console response layout. */
    private String formatForDialog(String response) {
        return String.join("\n", response.lines().map(String::stripLeading).toList());
    }

    /** Loads an avatar from the resources folder, or returns a blank placeholder when it is absent. */
    private Image loadImage(String resourcePath) {
        if (getClass().getResourceAsStream(resourcePath) == null) {
            return new WritableImage(1, 1);
        }
        return new Image(getClass().getResourceAsStream(resourcePath));
    }
}
