package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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
    public TextField searchBar;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    private void setFormattedDateCellFactory(TableColumn<Note, Date> column) {
        column.setCellFactory(cell -> new TableCell<Note, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    LocalDateTime localDateTime = item.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    String formattedDate = localDateTime.format(dateFormatter);
                    setText(formattedDate);
                } else {
                    setText(null);
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notesTable.setRowFactory(tv -> new TableRow<Note>() {
            private boolean isSameDay(Date date1, Date date2) {
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(date1);
                cal2.setTime(date2);
                return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
            }

            @Override
            protected void updateItem(Note item, boolean empty) {
                super.updateItem(item, empty);


                if (item != null) {
                    Date reminder = item.getReminder();
                    Date today = new Date();

                    if (reminder != null) {
                        if (!item.isDone()) {
                            
                        } else {
                            System.out.println("YBNDE");
                        }
                    } else {
                        setStyle("");
                    }

                    if (item.getCreatedBy() != Context.getInstance().getActualUser().getRegistrationNumber()) {
                        setStyle(getStyle() + "-fx-font-style: italic;");
                    }
                } else {
                    setStyle("");
                }

            }
        });
        colHeader.setCellValueFactory(new PropertyValueFactory<>("header"));
        colHeader.setCellFactory(column -> new TableCell<Note, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    Note note = getTableRow().getItem();
                    Text txt = new Text(item);
                    txt.setStrikethrough(note.isDone());
                    setGraphic(txt);
                } else {
                    setText(null);
                }
            }
        });
        colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        colTags.setCellValueFactory(new PropertyValueFactory<>("tag"));

        colColor.setCellFactory(column -> new TableCell<Note, String>() {
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
                    setGraphic(null);
                }
            }
        });
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colCreated.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        setFormattedDateCellFactory(colCreated);
        colEdited.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        setFormattedDateCellFactory(colEdited);

        notesTable.setEditable(true);
        notesTable.setItems(FXCollections.observableList(Notes.getInstance().getUserNotes(Context.getInstance().getActualUser())));
        notesTable.getItems().sort(new NoteComparator(NoteParameter.CreationDate));
    }


    public void commitEdit(TableColumn.CellEditEvent<Note, String> noteStringCellEditEvent) throws IOException {
        Note selected = notesTable.getSelectionModel().getSelectedItem();
        selected.setHeader(noteStringCellEditEvent.getNewValue());
    }

    public void cancelEdit(TableColumn.CellEditEvent<Note, String> noteStringCellEditEvent) {
        System.out.println("Cancela");
    }

    public void startEdit(TableColumn.CellEditEvent<Note, Object> noteStringCellEditEvent) {
        Context.getInstance().setToEdit(notesTable.getSelectionModel().getSelectedItem());

        Application.setRoot("openedNote");

    }

    public void moveToCreateNote(ActionEvent mouseEvent) throws IOException {
        Application.setRoot("createNote");
    }

    public void moveToTheTrash(ActionEvent mouseEvent) throws IOException {
        Application.setRoot("trash");
    }

    public void moveToLogin(ActionEvent mouseEvent) throws IOException {
        Context.getInstance().setActualUser(null);
        Application.setRoot("login");
    }

    public void searchHandler(KeyEvent keyEvent) {
        notesTable.setItems(FXCollections.observableList(Notes.getInstance().searchNote(Context.getInstance().getActualUser(), searchBar.getText())));
    }
}
