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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class CreateNote implements Initializable {
    public TextField titleInput;
    public TextField tagsInput;
    public TextArea bodyInput;
    public ColorPicker colorInput;
    public TextField collaboratorsInput;
    public ChoiceBox<Priority> priorityCombo;
    public HBox errorBox;
    public Label errorLabel;
    public Label headerCounter;
    public Label bodyCounter;
    public DatePicker reminderPicker;


    public void backHandler(ActionEvent actionEvent) {
        Application.setRoot("workspace");
    }

    public void addHandler(ActionEvent actionEvent) {
        String error = null;
        ArrayList<Long> collaborators = new ArrayList<>();

        if (titleInput.getText().length() == 0) {
            error = "Debe ingresar un titulo";
        }
        if (error == null && !collaboratorsInput.getText().isEmpty()) {
            String[] collaboratorsUnchecked = collaboratorsInput.getText().split(",");

            for (String s : collaboratorsUnchecked) {
                String collaborator = s.trim().toLowerCase();
                if (!SignIn.emailPattern.matcher(collaborator).matches()) {
                    error = "\"" + collaborator + "\" No es un correo valido";
                    break;
                }
                if (!Users.getInstance().existsEmail(collaborator)) {
                    error = "\"" + collaborator + "\" No tiene una cuenta creada";
                    break;
                }
                collaborators.add(Users.getInstance().getRegistrationNumber(collaborator));
            }
        }

        if (error == null) {
            try {
                Note note = (new Note(titleInput.getText(), bodyInput.getText(), tagsInput.getText(), colorInput.getValue().toString(), priorityCombo.getValue(), collaborators, Context.getInstance().getActualUser()));
                if (reminderPicker.getValue() != null) {
                    note.setReminder(Date.from(reminderPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                Application.setRoot("workspace");
            } catch (IOException e) {
                Application.handleException(e);
            }
        } else {
            errorLabel.setText(error);
            errorBox.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        priorityCombo.setItems(FXCollections.observableArrayList(Priority.values()));
        priorityCombo.setValue(Priority.Normal);
    }

    public void titleHandler(KeyEvent actionEvent) {
        while (titleInput.getText().length() > 30) {
            titleInput.deletePreviousChar();
        }
        headerCounter.setText(titleInput.getText().length() + "/30");
    }

    public void bodyHandler(KeyEvent keyEvent) {
        while (bodyInput.getText().length() > 200) {
            bodyInput.deletePreviousChar();
        }
        bodyCounter.setText(bodyInput.getText().length() + "/200");
    }
}
