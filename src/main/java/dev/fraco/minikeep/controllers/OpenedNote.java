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
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
    public DatePicker reminderInput;
    public Label headerCounter;
    public Label bodyCounter;
    public Label colab;
    public CheckBox isCompleted;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleInput.setText(note.getHeader());
        tagsInput.setText(note.getTag());
        bodyInput.setText(note.getBody());
        colorInput.setValue(Color.valueOf(note.getColor()));
        priorityCombo.setItems(FXCollections.observableArrayList(Priority.values()));
        priorityCombo.setValue(note.getPriority());
        isCompleted.setSelected(note.isDone());
        StringBuilder collaborators = new StringBuilder();
        if (note.getCreatedBy() != Context.getInstance().getActualUser().getRegistrationNumber()) {
            colab.setText("Creada por " + Users.getInstance().getEmail(note.getCreatedBy()));
            colab.setStyle(colab.getStyle() + "-fx-font-weight: bold;");
            collaboratorsInput.setVisible(false);
        } else {
            boolean isFirst = true;
            for (long rn : note.getCollaborators()) {
                if (!isFirst) {
                    collaborators.append(", ");
                }
                isFirst = false;
                collaborators.append(Users.getInstance().getEmail(rn));
            }
        }

        if (note.getReminder() != null) {
            reminderInput.setValue(note.getReminder().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        collaboratorsInput.setText(collaborators.toString());
        titleHandler(null);
        bodyHandler(null);
    }

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
                note.setHeader(titleInput.getText());
                note.setBody(bodyInput.getText());
                note.setColor(String.valueOf(colorInput.getValue()));
                if (note.getCreatedBy() == Context.getInstance().getActualUser().getRegistrationNumber()) {
                    note.setCollaborators(collaborators);
                }
                note.setTag(tagsInput.getText());
                note.setPriority(priorityCombo.getValue());
                if (reminderInput.getValue() != null) {
                    note.setReminder(Date.from(reminderInput.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                note.setDone(isCompleted.isSelected());
                Application.setRoot("workspace");
            } catch (IOException e) {
                Application.handleException(e);
            }
        } else {
            errorLabel.setText(error);
            errorBox.setVisible(true);
        }
    }


    public void trashHandler(ActionEvent actionEvent) {
        if (note.getCreatedBy() == Context.getInstance().getActualUser().getRegistrationNumber()) {
            try {
                note.markAsDeleted();
                Application.setRoot("workspace");
            } catch (IOException e) {
                Application.handleException(e);
            }
        } else {
            errorLabel.setText("Solo el creador puede eliminar una nota");
            errorBox.setVisible(true);
        }
    }

    public void titleHandler(KeyEvent _actionEvent) {
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
