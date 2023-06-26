package dev.fraco.minikeep.controllers;

import dev.fraco.minikeep.Application;
import dev.fraco.minikeep.logic.*;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private Notes notes = Notes.getInstance();
    private Context ctx = Context.getInstance();
    private Note actual = null;
    public TableView<Note> notesTable;
    public TableColumn<Note, String> colHeader;
    public TableColumn<Note, Priority> colPriority;
    public TableColumn<Note, ArrayList<String>> colTags;
    public TableColumn<Note, String> colColor;
    public TableColumn<Note, Date> colCreated;
    public TableColumn<Note, Date> colEdited;
    public TextField searchBar;
    private static final DateTimeFormatter dateFormatterV1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dateFormatterV2 = DateTimeFormatter.ofPattern("hh:mm a");
    public Text noteHeader;
    public TextField titleInput;
    public Label headerCounter;
    public TextField tagsInput;
    public TextArea bodyInput;
    public Label bodyCounter;
    public ColorPicker colorInput;
    public ChoiceBox<Priority> priorityCombo;
    public DatePicker reminderPicker;
    public TextField collaboratorsInput;
    public HBox errorBox;
    public Label errorLabel;
    public VBox modifyBox;
    public CheckBox ended;
    public Label colab;


    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    private void setFormattedDateCellFactory(TableColumn<Note, Date> column) {
        column.setCellFactory(cell -> new TableCell<Note, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    LocalDateTime localDateTime = item.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    String formattedDate;
                    if (isSameDay(item, new Date())) {
                        formattedDate = localDateTime.format(dateFormatterV2);
                    } else {
                        formattedDate = localDateTime.format(dateFormatterV1);
                    }
                    setText(formattedDate);
                } else {
                    setText(null);
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notesTable.setRowFactory(tv -> new TableRow<>() {


            @Override
            protected void updateItem(Note item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    if (item.getReminder() != null) {
                        if (item.isDone()) {
                            pseudoClassStateChanged(PseudoClass.getPseudoClass("is-today"), false);
                            pseudoClassStateChanged(PseudoClass.getPseudoClass("is-late"), false);
                        } else if (isSameDay(item.getReminder(), new Date())) {
                            pseudoClassStateChanged(PseudoClass.getPseudoClass("is-today"), true);
                            pseudoClassStateChanged(PseudoClass.getPseudoClass("is-late"), false);
                        } else if (item.getReminder().getTime() < new Date().getTime()) {
                            pseudoClassStateChanged(PseudoClass.getPseudoClass("is-today"), false);
                            pseudoClassStateChanged(PseudoClass.getPseudoClass("is-late"), true);
                        } else {
                            pseudoClassStateChanged(PseudoClass.getPseudoClass("is-today"), false);
                            pseudoClassStateChanged(PseudoClass.getPseudoClass("is-late"), false);
                        }
                    } else {
                        pseudoClassStateChanged(PseudoClass.getPseudoClass("is-today"), false);
                        pseudoClassStateChanged(PseudoClass.getPseudoClass("is-late"), false);

                    }
                } else {
                    pseudoClassStateChanged(PseudoClass.getPseudoClass("is-today"), false);
                    pseudoClassStateChanged(PseudoClass.getPseudoClass("is-late"), false);
                }

            }
        });

        colHeader.setCellValueFactory(new PropertyValueFactory<>("header"));
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
                    setStyle("-fx-background-color: transparent;");
                    setGraphic(null);
                }
            }
        });
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colCreated.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        setFormattedDateCellFactory(colCreated);
        colEdited.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        setFormattedDateCellFactory(colEdited);
        priorityCombo.setItems(FXCollections.observableArrayList(Priority.values()));

        ScrollPane scrollPane = new ScrollPane(notesTable);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        notesTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, note, t1) -> selectHandler(t1)));

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
        if (!notesTable.getItems().isEmpty()) {
            notesTable.getSelectionModel().select(notesTable.getItems().get(0));
        } else {
            notesTable.getSelectionModel().select(null);
        }
    }

    public void selectHandler(Note note) {
        actual = note;
        if (note == null) {
            modifyBox.setVisible(false);
            return;
        }
        titleInput.setText(note.getHeader());
        titleHandler(null);
        bodyInput.setText(note.getBody());
        bodyHandler(null);
        tagsInput.setText(note.getTag());
        colorInput.setValue(Color.valueOf(note.getColor()));
        priorityCombo.setValue(note.getPriority());

        errorLabel.setText("");
        errorBox.setVisible(false);

        reminderPicker.setValue(null);
        ended.setSelected(false);
        if (note.getReminder() != null) {
            reminderPicker.setValue(note.getReminder().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            ended.setSelected(note.isDone());
            ended.setVisible(true);
        } else {
            ended.setVisible(false);
        }
        StringBuilder collaborators = new StringBuilder();

        if (note.getCreatedBy() != Context.getInstance().getActualUser().getRegistrationNumber()) {
            colab.setText("Creada por " + Users.getInstance().getEmail(note.getCreatedBy()));
            colab.setStyle(colab.getStyle() + "-fx-font-weight: bold;");
            collaboratorsInput.setVisible(false);
        } else {
            collaboratorsInput.setVisible(true);

            colab.setText("Colaboradores (email separados por coma)");
            boolean isFirst = true;
            for (long rn : note.getCollaborators()) {
                if (!isFirst) {
                    collaborators.append(", ");
                }
                isFirst = false;
                collaborators.append(Users.getInstance().getEmail(rn));
            }
            collaboratorsInput.setText(collaborators.toString());
        }


        modifyBox.setVisible(true);
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
                actual.setHeader(titleInput.getText());
                actual.setTag(tagsInput.getText());
                actual.setBody(bodyInput.getText());
                actual.setColor(colorInput.getValue().toString());
                actual.setPriority(priorityCombo.getValue());
                if (actual.getCreatedBy() == Context.getInstance().getActualUser().getRegistrationNumber()) {
                    actual.setCollaborators(collaborators);
                }
                if (reminderPicker.getValue() != null) {
                    actual.setReminder(Date.from(reminderPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    actual.setDone(ended.isSelected());
                }
                selectHandler(actual);
                notesTable.refresh();
            } catch (IOException e) {
                Application.handleException(e);
            }
        } else {
            errorLabel.setText(error);
            errorBox.setVisible(true);
        }
    }

    public void backHandler(ActionEvent actionEvent) {
        selectHandler(null);
        notesTable.getSelectionModel().select(null);
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

    public void moveHandler(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DOWN) {
            notesTable.getSelectionModel().selectBelowCell();
        } else if (keyEvent.getCode() == KeyCode.UP) {
            notesTable.getSelectionModel().selectAboveCell();
        }
    }

    public void deleteBtn(ActionEvent actionEvent) {
        try {
            if (actual.getCreatedBy() == ctx.getActualUser().getRegistrationNumber()) {
                actual.markAsDeleted();
                notesTable.getItems().remove(actual);
                notesTable.refresh();
            } else {
                errorLabel.setText("Solo el creador de una nota puede eliminarla");
                errorBox.setVisible(true);
            }
        } catch (IOException e) {
            Application.handleException(e);
        }
    }
}
