package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.Context;
import dev.fraco.minikeep.logic.User;
import dev.fraco.minikeep.logic.UserAlreadyExistsException;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.regex.Pattern;

public class SignIn {
    private final Context ctx = Context.getInstance();

    public DatePicker dateInput;
    public TextField emailInput;
    public TextField registrationNumberInput;
    public TextField lastNameInput;
    public TextField firstNameInput;
    public TextField phoneInput;

    public static final Pattern emailPattern = Pattern.compile("^[a-z0-9.\\-_]+@([a-z]+\\.)+[a-z0-9]+$", Pattern.CASE_INSENSITIVE);
    public Label errorLabel;
    public HBox errorBox;

    public void submitHandler(ActionEvent ignoredActionEvent) {
        String registrationNumberString = registrationNumberInput.getText().trim();
        Long registrationNumber = null;
        String firstName = firstNameInput.getText().trim();
        String lastName = lastNameInput.getText().trim();
        String email = emailInput.getText().trim();
        String phoneNumber = phoneInput.getText().trim();


        String error = null;
        if (firstName.length() == 0) {
            error = "No ha introducido un nombre";
        }
        if (error == null && lastName.length() == 0) {
            error = "No ha introducido un apellido";
        }
        if (error == null) {
            try {
                registrationNumber = Long.parseLong(registrationNumberString);
            } catch (NumberFormatException nfe) {
                error = "Número de matrícula inválido";
            }
        }
        if (error == null && !emailPattern.matcher(email).matches()) {
            error = "Email inválido";
        }
        if (error == null && phoneNumber.length() == 0) {
            error = "No ha introducido un número de teléfono";
        }
        if (error == null && dateInput.getValue() == null) {
            error = "No ha introducido su fecha de nacimiento";
        }

        User user = null;
        if (error == null) {
            try {
                user = new User(registrationNumber, firstName, lastName, email, phoneNumber, dateInput.getValue().toString());
            } catch (UserAlreadyExistsException e) {
                error = "Ya existe un usuario con sus datos";
            } catch (IOException e) {
                Application.handleException(e);
            }
        }

        if (error == null) {
            ctx.setActualUser(user);
            Application.setRoot("showPassword");
        } else {
            errorLabel.setText(error);
            errorBox.setVisible(true);
        }
    }

    public void backHandler(ActionEvent ignoredActionEvent) {
        Application.setRoot("login");
    }
}
