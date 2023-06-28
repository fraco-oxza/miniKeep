package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Trash extends Workspace {
    public Button deleteBtn;
    public Button recover;
    public void backHandler(ActionEvent ignoredActionEvent) {
        Application.setRoot("workspace");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
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

    public void deleteAll(ActionEvent ignoredActionEvent) {
        try {
            for (Note note : notesTable.getItems()) {
                ctx.notes.removeNote(note);
            }
            Application.setRoot("workspace");
        } catch (IOException e) {
            Application.handleException(e);
        }
    }

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

    public void recoverAll(ActionEvent ignoredActionEvent) {
        try {
            for (Note note : notesTable.getItems()) {
                note.restore();
            }
            Application.setRoot("workspace");
        } catch (IOException e) {
            Application.handleException(e);
        }
    }
}
