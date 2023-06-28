package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.MiniKeepMain;
import dev.fraco.minikeep.logic.Context;
import dev.fraco.minikeep.logic.Note;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CreateNote extends FormNoteParser {
    public HBox errorBox;
    public Label errorLabel;
    public Label headerCounter;
    public Label bodyCounter;

    public void backHandler(ActionEvent ignoredActionEvent) {
        MiniKeepMain.setRoot("workspace");
    }

    public void addHandler(ActionEvent ignoredActionEvent) {
        parse();

        if (error == null) {
            try {
                Note note = new Note(header, body, tag, color, priority, collaborators, Context.getInstance().getActualUser());
                if (reminderPicker.getValue() != null) {
                    note.setReminder(reminder);
                }
                MiniKeepMain.setRoot("workspace");
            } catch (IOException e) {
                MiniKeepMain.handleException(e);
            }
        } else {
            errorLabel.setText(error);
            errorBox.setVisible(true);
        }
    }

    public void titleHandler(KeyEvent ignoredActionEvent) {
        while (titleInput.getText().length() > 30) {
            titleInput.deletePreviousChar();
        }
        headerCounter.setText(titleInput.getText().length() + "/30");
    }

    public void bodyHandler(KeyEvent ignoredKeyEvent) {
        while (bodyInput.getText().length() > 200) {
            bodyInput.deletePreviousChar();
        }
        bodyCounter.setText(bodyInput.getText().length() + "/200");
    }
}
