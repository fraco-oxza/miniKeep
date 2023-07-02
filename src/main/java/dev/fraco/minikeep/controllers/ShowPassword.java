package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.MiniKeepMain;
import dev.fraco.minikeep.logic.Context;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Show Password view.
 * Displays the user's password and allows the user to proceed to the login view.
 */
public class ShowPassword implements Initializable {
    private final Context ctx = Context.getInstance();
    public Label password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the password label to display the user's password
        password.setText(ctx.getActualUser().getPassword());
        ctx.setActualUser(null);
    }

    /**
     * Handles the event when the user clicks the next button.
     * Switches to the login view.
     *
     * @param ignoredActionEvent the action event (ignored)
     */
    public void nextHandler(ActionEvent ignoredActionEvent) {
        MiniKeepMain.setRoot("login");
    }
}
