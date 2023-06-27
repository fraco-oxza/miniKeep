package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class Trash implements Initializable {
    private final Context ctx = Context.getInstance();
    public TableView<Note> notesTable;
    public TableColumn<Note, String> colHeader;
    public TableColumn<Note, Priority> colPriority;
    public TableColumn<Note, ArrayList<String>> colTags;
    public TableColumn<Note, String> colColor;
    public TableColumn<Note, Date> colCreated;
    public TableColumn<Note, Date> colEdited;
    public Button deleteBtn;
    public Button recover;

    public void backHandler(ActionEvent ignoredActionEvent) {
        Application.setRoot("workspace");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colHeader.setCellValueFactory(new PropertyValueFactory<>("header"));
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colTags.setCellValueFactory(new PropertyValueFactory<>("tag"));

        colColor.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    Color color = Color.valueOf(item);
                    int red = (int) (color.getRed() * 255);
                    int green = (int) (color.getGreen() * 255);
                    int blue = (int) (color.getBlue() * 255);

                    String cssColor = String.format("#%02x%02x%02x", red, green, blue);
                    setStyle("-fx-background-color: " + cssColor + ";");
                } else {
                    setStyle("-fx-background-color: transparent;");
                    setGraphic(null);
                }
            }
        });
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colCreated.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        Workspace.setFormattedDateCellFactory(colCreated);
        colEdited.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        Workspace.setFormattedDateCellFactory(colEdited);

        notesTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, note, t1) -> selectHandler(t1)));
        selectHandler(null);
        notesTable.setItems(FXCollections.observableList(ctx.notes.getTrashNotes(ctx.getActualUser())));
        notesTable.getItems().sort(new NoteComparator(NoteParameter.CreationDate));
    }

    private void selectHandler(Note note) {
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
