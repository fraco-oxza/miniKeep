package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.*;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class Workspace implements Initializable {
    public TableView<Note> notesTable;
    public TableColumn<Note, String> colHeader;
    public TableColumn<Note, Priority> colPriority;
    public TableColumn<Note, ArrayList<String>> colTags;
    public TableColumn<Note, String> colColor;
    public TableColumn<Note, Date> colCreated;
    public TableColumn<Note, Date> colEdited;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colHeader.setCellValueFactory(new PropertyValueFactory<>("header"));

        notesTable.setEditable(true);
        notesTable.setItems(FXCollections.observableList(Notes.getInstance().getUserNotes(Context.getInstance().getActualUser())));
    }

    public void commitEdit(TableColumn.CellEditEvent<Note, String> noteStringCellEditEvent) throws IOException {
        Note selected = notesTable.getSelectionModel().getSelectedItem();
        selected.setHeader(noteStringCellEditEvent.getNewValue());
    }

    public void cancelEdit(TableColumn.CellEditEvent<Note, String> noteStringCellEditEvent) {
        System.out.println("Cancela");
    }

    public void startEdit(TableColumn.CellEditEvent<Note,Object> noteStringCellEditEvent) throws IOException {
        System.out.println("Quiere editar");
        Context.getInstance().setToEdit(notesTable.getSelectionModel().getSelectedItem());
    }

    public void moveToCreateNote(MouseEvent mouseEvent) throws IOException {
        Application.setRoot("createNote");
    }

    public void moveToTheTrash(MouseEvent mouseEvent) throws IOException {
        Application.setRoot("trash");
    }

    public void moveToLogin(MouseEvent mouseEvent) throws  IOException{
        Context.getInstance().setActualUser(null);
        Application.setRoot("login");
    }
}
