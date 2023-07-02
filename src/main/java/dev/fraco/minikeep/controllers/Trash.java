package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.MiniKeepMain;
import dev.fraco.minikeep.logic.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Trash class represents the trash view in MiniKeep.
 * It extends the Workspace class and provides functionality for managing deleted notes.
 */
public class Trash extends Workspace {
    public Button deleteBtn;
    public Button recover;

    /**
     * Handles the event when the user clicks the back button.
     * Performs the necessary actions to navigate back to the workspace.
     *
     * @param ignoredActionEvent the action event (ignored)
     */
    public void backHandler(ActionEvent ignoredActionEvent) {
        MiniKeepMain.setRoot("workspace");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        selectHandler(null);
        notesTable.setItems(FXCollections.observableList(ctx.notes.getTrashNotes(ctx.getActualUser())));
    }

    @Override
    public void selectHandler(Note note) {
        if (note == null) {
            recover.setOpacity(0.6);
            deleteBtn.setOpacity(0.6);
        } else {
            recover.setOpacity(1);
            deleteBtn.setOpacity(1);
        }
    }

    /**
     * Handles the event when the user clicks the delete button for a single note.
     * Deletes the selected note from the trash and updates the UI.
     *
     * @param ignoredActionEvent the action event (ignored)
     */
    public void deleteOne(ActionEvent ignoredActionEvent) {
        try {
            if (notesTable.getSelectionModel().getSelectedItem() != null) {
                ctx.notes.removeNote(notesTable.getSelectionModel().getSelectedItem());
                notesTable.getItems().remove(notesTable.getSelectionModel().getSelectedItem());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the event when the user clicks the delete all button.
     * Deletes all notes from the trash and navigates back to the workspace.
     *
     * @param ignoredActionEvent the action event (ignored)
     */
    public void deleteAll(ActionEvent ignoredActionEvent) {
        try {
            for (Note note : notesTable.getItems()) {
                ctx.notes.removeNote(note);
            }
            MiniKeepMain.setRoot("workspace");
        } catch (IOException e) {
            MiniKeepMain.handleException(e);
        }
    }

    /**
     * Handles the event when the user clicks the recover button for a single note.
     * Restores the selected note from the trash and updates the UI.
     *
     * @param ignoredActionEvent the action event (ignored)
     */
    public void recoverOne(ActionEvent ignoredActionEvent) {
        try {
            if (notesTable.getSelectionModel().getSelectedItem() != null) {
                notesTable.getSelectionModel().getSelectedItem().restore();
                notesTable.getItems().remove(notesTable.getSelectionModel().getSelectedItem());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the event when the user clicks the recover all button.
     * Restores all notes from the trash and navigates back to the workspace.
     *
     * @param ignoredActionEvent the action event (ignored)
     */
    public void recoverAll(ActionEvent ignoredActionEvent) {
        try {
            for (Note note : notesTable.getItems()) {
                note.restore();
            }
            MiniKeepMain.setRoot("workspace");
        } catch (IOException e) {
            MiniKeepMain.handleException(e);
        }
    }
}
