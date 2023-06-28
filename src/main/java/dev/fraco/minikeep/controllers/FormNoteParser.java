package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.logic.Context;
import dev.fraco.minikeep.logic.Priority;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public abstract class FormNoteParser implements Initializable {
    protected final Context ctx = Context.getInstance();
    public TextField titleInput;
    public TextField tagsInput;
    public TextArea bodyInput;
    public ColorPicker colorInput;
    public ChoiceBox<Priority> priorityCombo;
    public DatePicker reminderPicker;
    public TextField collaboratorsInput;

    protected String error = null;
    protected ArrayList<Long> collaborators;

    protected String header, tag, body, color;
    protected Priority priority;
    protected Date reminder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (priorityCombo != null) {
            priorityCombo.setItems(FXCollections.observableArrayList(Priority.values()));
            priorityCombo.setValue(Priority.Normal);
        }
    }

    protected void parse() {
        error = null;
        collaborators = new ArrayList<>();

        if (titleInput.getText().length() == 0) {
            error = "Debe ingresar un titulo";
            return;
        }
        if (!collaboratorsInput.getText().isEmpty()) {
            String[] collaboratorsUnchecked = collaboratorsInput.getText().split(",");

            for (String s : collaboratorsUnchecked) {
                String collaborator = s.trim().toLowerCase();
                if (!SignIn.emailPattern.matcher(collaborator).matches()) {
                    error = "\"" + collaborator + "\" No es un correo valido";
                    return;
                }
                if (!ctx.users.existsEmail(collaborator)) {
                    error = "\"" + collaborator + "\" No tiene una cuenta creada";
                    return;
                }
                collaborators.add(ctx.users.getRegistrationNumber(collaborator));
            }
        }

        header = titleInput.getText();
        body = bodyInput.getText();
        tag = tagsInput.getText();
        color = colorInput.getValue().toString();
        priority = priorityCombo.getValue();
        if (reminderPicker.getValue() != null) {
            reminder = Date.from(reminderPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            reminder = null;
        }
    }
}
