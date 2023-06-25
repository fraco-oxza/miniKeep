package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;

public class Trash {
    public TableColumn colHeader;
    public TableColumn colPriority;
    public TableColumn colTags;
    public TableColumn colColor;
    public TableColumn colCreated;
    public TableColumn colEdited;
    public TableView notesTable;

    public void commitEdit(TableColumn.CellEditEvent cellEditEvent) {
    }

    public void deleteNote(TableColumn.CellEditEvent cellEditEvent) {
    }

    public void backHandler(ActionEvent actionEvent) {
            Application.setRoot("workspace");

    }
}
