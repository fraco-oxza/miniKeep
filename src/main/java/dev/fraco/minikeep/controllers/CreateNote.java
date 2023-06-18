package dev.fraco.minikeep.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CreateNote {
    public TextField titleInput;
    public TextField tagsInput;
    public TextArea bodyInput;
    public ColorPicker colorInput;
    public TextField collaboratorsInput;

    public void backHandler(ActionEvent actionEvent) {
        System.out.println("Event");
    }

    public void addHandler(ActionEvent actionEvent) {
        System.out.println("Event");
    }
}
