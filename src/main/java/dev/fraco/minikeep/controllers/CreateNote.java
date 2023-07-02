package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.MiniKeepMain;
import dev.fraco.minikeep.logic.Context;
import dev.fraco.minikeep.logic.Note;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

/**
 * The controller class for the "Create Note" window.
 * This class handles the creation of notes and manages user input.
 */
public class CreateNote extends FormNoteParser {
    /**
     * HBox container for displaying error message.
     */
    public HBox errorBox;

    /**
     * Label for displaying error message.
     */
    public Label errorLabel;

    /**
     * Label for displaying the character count of the note title.
     */
    public Label headerCounter;

    /**
     * Label for displaying the character count of the note body.
     */
    public Label bodyCounter;

    /**
     * Rectangle representing the color bar for the note.
     */
    public Rectangle colorBar;

    /**
     * Event handler for the "Back" button.
     *
     * @param ignoredActionEvent The event triggering the handler (ignored).
     */
    public void backHandler(ActionEvent ignoredActionEvent) {
        MiniKeepMain.setRoot("workspace");
    }

    /**
     * Event handler for the "Add" button.
     *
     * @param ignoredActionEvent The event triggering the handler (ignored).
     */
    public void addHandler(ActionEvent ignoredActionEvent) {
        parse();

        if (error == null) {
            try {
                // Create a new Note object with the entered information
                Note note = new Note(header, body, tag, color, priority, collaborators, Context.getInstance().getActualUser());

                // Set the reminder if selected
                if (reminderPicker.getValue() != null) {
                    note.setReminder(reminder);
                }

                MiniKeepMain.setRoot("workspace");
            } catch (IOException e) {
                MiniKeepMain.handleException(e);
            }
        } else {
            // Display the error message
            errorLabel.setText(error);
            errorBox.setVisible(true);
        }
    }

    /**
     * Event handler for the note title input field.
     * Updates the character count label for the note title.
     *
     * @param ignoredActionEvent The event triggering the handler (ignored).
     */
    public void titleHandler(KeyEvent ignoredActionEvent) {
        while (titleInput.getText().length() > 30) {
            titleInput.deletePreviousChar();
        }
        headerCounter.setText(titleInput.getText().length() + "/30");
    }

    /**
     * Event handler for the note body input field.
     * Updates the character count label for the note body.
     *
     * @param ignoredKeyEvent The event triggering the handler (ignored).
     */
    public void bodyHandler(KeyEvent ignoredKeyEvent) {
        while (bodyInput.getText().length() > 200) {
            bodyInput.deletePreviousChar();
        }
        bodyCounter.setText(bodyInput.getText().length() + "/200");
    }

    /**
     * Event handler for changing the color of the note.
     *
     * @param ignoredEvent The event triggering the handler (ignored).
     */
    public void changeColorHandler(Event ignoredEvent) {
        colorBar.setFill(Paint.valueOf(colorInput.getValue().toString()));
    }
}
