package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.Context;
import dev.fraco.minikeep.logic.Note;
import dev.fraco.minikeep.logic.Priority;
import dev.fraco.minikeep.logic.Users;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class OpenedNote implements Initializable {
    public Note note = Context.getInstance().getToEdit();
    public TextField titleInput;
    public TextField tagsInput;
    public TextArea bodyInput;
    public ColorPicker colorInput;
    public ChoiceBox<Priority> priorityCombo;
    public TextField collaboratorsInput;
    public Label errorLabel;
    public HBox errorBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleInput.setText(note.getHeader());
        tagsInput.setText(note.getTag());
        bodyInput.setText(note.getBody());
        colorInput.setValue(Color.valueOf(note.getColor()));
        priorityCombo.setItems(FXCollections.observableArrayList(Priority.values()));
        priorityCombo.setValue(note.getPriority());
        StringBuilder collaborators = new StringBuilder();
        boolean isFirst = true;
        for (long rn : note.getCollaborators()) {
            if (!isFirst) {
                collaborators.append(", ");
            }
            isFirst = false;
            collaborators.append(Users.getInstance().getEmail(rn));
        }
        collaboratorsInput.setText(collaborators.toString());
    }

    public void backHandler(ActionEvent actionEvent) {
        Application.setRoot("workspace");
    }

    public void addHandler(ActionEvent actionEvent) {
    }


    public void trashHandler(ActionEvent actionEvent) {
        try {
            note.markAsDeleted();
            Application.setRoot("workspace");
        } catch (IOException e) {
            Application.handleException(e);
        }
    }
}
