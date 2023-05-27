import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * The MenuInteraction class represents the menu interaction functionality
 * of the application. It allows the user to navigate through different menus
 * and perform various actions such as signing in, signing up, creating, editing,
 * and deleting notes, and managing the trash.
 */
public class MenuInteraction {
    private static final int headerMaxLength = 30;
    private static final int bodyMaxLength = 200;
    private final Users users = Users.getInstance();
    private final Notes notes = Notes.getInstance();
    private final UserInput userInput = UserInput.getInstance();
    private final OutputFormatter formatter;
    private User user; // The current user
    private boolean userWantsToExit;

    /**
     * Constructs a MenuInteraction object with the specified OutputFormatter instance.
     *
     * @param formatter the OutputFormatter to use for displaying output
     */
    public MenuInteraction(OutputFormatter formatter) {
        this.formatter = formatter;
        user = null;
        userWantsToExit = false;
    }

    /**
     * Starts the main menu loop. Displays different menus based on the user's
     * authentication status and handles user input accordingly.
     *
     * @throws IOException if an I/O error occurs while reading user input
     */
    public void startLoop() throws IOException {
        do {
            if (user == null) {
                sessionMenu();
            } else {
                generalMenu();
            }
        } while (!userWantsToExit);
    }

    /**
     * Displays the session menu options and handles the user's choice.
     * The options include signing in, creating an account, and exiting.
     * The method prompts the user for input and performs the corresponding action based on the selected option.
     */
    private void sessionMenu() {
        OutputFormatter.showMenu("1. Iniciar Sesión", "2. Crear cuenta", "0. Salir");
        int option = userInput.getInt("Opción", 0, 2);

        switch (option) {
            case 1:
                signIn();
                break;
            case 2:
                signUp();
                break;
            case 0:
                userWantsToExit = true;
                break;
        }
    }

    /**
     * Performs the sign-in process.
     * The method prompts the user for their email and password, verifies the credentials,
     * and sets the user variable if the sign-in is successful.
     */
    private void signIn() {
        String email = userInput.getText("Correo", UserInput.emailPattern);
        String password = userInput.getText("Clave ");

        User possibleUser = users.getUser(email, password);

        if (possibleUser == null) {
            OutputFormatter.showError("Email y/o contraseña incorrecto");
        } else {
            OutputFormatter.showSuccess("Inicio de sesion exitoso");
            user = possibleUser;
        }
    }

    /**
     * Performs the sign-up process.
     * The method prompts the user to enter their details to create a new account.
     * If the account creation is successful, the user's information is displayed, including the generated password.
     * If an error occurs during the account creation, an appropriate error message is displayed.
     */
    private void signUp() {
        try {
            User user = userInput.addNewUser();
            OutputFormatter.showSuccess("Usuario creado con exito");
            OutputFormatter.showSuccess("Su clave es : " + user.getPassword());
        } catch (UserAlreadyExistsException e) {
            OutputFormatter.showError("Ya existe un usuario con su numero de matricula o correo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the general menu options for the authenticated user.
     * The method prompts the user to select an option and performs the corresponding action based on the input.
     * The available options include viewing notes, creating a new note, editing a note, deleting a note,
     * viewing the trash, displaying user information, logging out, and exiting the application.
     * If an invalid option is selected, an appropriate error message is displayed.
     *
     * @throws IOException if an I/O error occurs during user input
     */
    private void generalMenu() throws IOException {
        OutputFormatter.showMenu("1. Ver Notas", "2. Crear Nota", "3. Editar Nota", "4. Eliminar Nota", "5. Ver papelera", "6. Mostrar datos de usuario", "7. Cerrar sesión", "0. Salir");
        int option = userInput.getInt("Opción", 0, 6);

        switch (option) {
            case 1:
                notesMenu();
                break;
            case 2:
                createNoteMenu();
                break;
            case 3:
                editMenu();
                break;
            case 4:
                deleteMenu();
                break;
            case 5:
                trashMenu();
                break;
            case 6:
                userMenu();
                break;
            case 7:
                user = null;
                break;
            case 0:
                userWantsToExit = true;
                break;
        }
    }

    /**
     * Displays the user information in a formatted manner.
     * The method uses the OutputFormatter to enclose the user information in a header
     * and then displays the formatted user information using the formatter's showUser() method.
     */
    private void userMenu() {
        formatter.showEncloseHeader("Datos de Usuario");
        formatter.showUser(user);
    }

    /**
     * Displays the notes menu where the user can choose the order in which to display the notes.
     * The method prompts the user to select an option for ordering the notes and then performs the corresponding action.
     * The available options are:
     * 1. Order by name
     * 2. Order by date
     * 3. Order by priority
     * 4. Grouped by color
     * 5. Grouped by tags
     * The method uses the OutputFormatter to display the menu options and to show the formatted notes.
     * It also uses the NoteComparator to sort the notes based on the selected order.
     */
    private void notesMenu() {
        System.out.println("En que orden desea mostrar las notas: ");
        OutputFormatter.showMenu("1. Nombre", "2. Fecha", "3. Prioridad", "4. Agrupados por color", "5. Agrupados por temas");
        int option = userInput.getInt("Opción", 1, 5);

        List<Note> userNotes = notes.getUserNotes(user);

        Collections.sort(userNotes, new NoteComparator(NoteParameter.UpdateDate));
        switch (option) {
            case 1:
                Collections.sort(userNotes, new NoteComparator(NoteParameter.Header));
                formatter.showNotes(userNotes);
                break;
            case 2:
                OutputFormatter.showMenu();
                OutputFormatter.showMenu("1. Creacion", "2. Ultima Actualizacion", "3. Ultima Visita");
                NoteParameter parameter = NoteParameter.from(userInput.getInt("Ordenar por", 1, 3));
                Collections.sort(userNotes, new NoteComparator(parameter));
                formatter.showNotes(userNotes);
                break;
            case 3:
                notesByPriorityMenu(userNotes);
                break;
            case 4:
                notesByColorMenu(userNotes);
                break;
            case 5:
                notesByTagMenu(userNotes);
                break;
            case 0:
                userWantsToExit = true;
                break;
        }
    }

    /**
     * Displays the menu for showing notes based on tags.
     * The method prompts the user to select an option:
     * 1. Show all notes
     * 2. Show notes with a specific tag
     * Depending on the selected option, the method either calls the formatter's showNotesByTag method to show all notes grouped by tags,
     * or prompts the user to select a specific tag and then calls the formatter's showNotesWithTag method to show notes with that tag.
     *
     * @param userNotes the list of user notes
     */
    private void notesByTagMenu(List<Note> userNotes) {
        OutputFormatter.showMenu("1. Todas las notas", "2. Tag especifica");
        int option = userInput.getInt("Que desea mostrar", 1, 2);

        if (option == 1) {
            formatter.showNotesByTag(userNotes);
        } else {
            HashSet<String> availableTagsMap = new HashSet<>();

            for (Note note : userNotes) {
                availableTagsMap.addAll(note.getTags());
            }

            ArrayList<String> availableTags = new ArrayList<>(availableTagsMap);

            String tag = userInput.pickOne("Indice de la Tag: ", availableTags);
            formatter.showNotesWithTag(userNotes, tag);
        }
    }

    /**
     * Displays the menu for showing notes based on colors.
     * The method prompts the user to select an option:
     * 1. Show all notes
     * 2. Show notes with a specific color
     * Depending on the selected option, the method either calls the formatter's showNotesByColor method to show all notes grouped by colors,
     * or prompts the user to select a specific color and then calls the formatter's showNotesWithColor method to show notes with that color.
     *
     * @param userNotes the list of user notes
     */
    private void notesByColorMenu(List<Note> userNotes) {
        OutputFormatter.showMenu("1. Todas las notas", "2. Color especifico");
        int option = userInput.getInt("Que desea mostrar", 1, 2);

        if (option == 1) {
            formatter.showNotesByColor(userNotes);
        } else {
            HashSet<String> availableColorsMap = new HashSet<>();

            for (Note note : userNotes) {
                availableColorsMap.add(note.getColor().toLowerCase());
            }

            ArrayList<String> availableColors = new ArrayList<>(availableColorsMap);

            String color = userInput.pickOne("Indice del color", availableColors);
            formatter.showNotesWithColor(userNotes, color);
        }
    }

    /**
     * Displays the menu for showing notes based on priority.
     * The method prompts the user to enter a priority to show notes with that priority.
     * If the user enters an invalid priority or chooses not to specify a priority, it calls the formatters showNotesByPriority method to show all notes grouped by priority.
     * Otherwise, it calls the formatters showNotesWithPriority method to show notes with the specified priority.
     *
     * @param userNotes the list of user notes
     */
    private void notesByPriorityMenu(List<Note> userNotes) {
        Priority priority = userInput.getOptionalPriority("Prioridad a mostrar");

        if (priority == null) {
            formatter.showNotesByPriority(userNotes);
        } else {
            formatter.showNotesWithPriority(userNotes, priority);
        }
    }

    /**
     * Displays the menu for creating a new note.
     * The method prompts the user to enter the necessary fields for creating a note, including the header, body, tags, color, and priority.
     * Once the user enters all the required information, it creates a new Note object and saves it using the Notes instance.
     * Finally, it displays a success message to indicate that the note has been created successfully.
     *
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void createNoteMenu() throws IOException {
        System.out.println("Creando nota");
        System.out.println("Debe ingresar los siguientes campos");

        String header = userInput.getText("Titulo", 1, headerMaxLength);

        String body = userInput.getText("Cuerpo", 1, bodyMaxLength);

        List<String> tags = userInput.getTags("Tags[separadas por ',']");

        String color = userInput.getText("Color").toLowerCase();

        Priority priority = userInput.getPriority("Prioridad");

        // The note is immediately saved in the file through the Notes instance
        new Note(header, body, tags, color, priority, user);
        OutputFormatter.showSuccess("Nota creada con exito");
    }

    /**
     * Displays the menu for editing a note.
     * The method retrieves the user's notes, sorts them by header, and displays a list of short notes.
     * The user is prompted to enter the index of the note they want to modify.
     * If the index is 0, the method returns and goes back to the previous menu.
     * Otherwise, it retrieves the selected note, displays the full note details, and presents a menu of fields that can be modified.
     * Based on the user's selection, the corresponding edit method is called to modify the note.
     *
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void editMenu() throws IOException {
        List<Note> userNotes = notes.getUserNotes(user);
        Collections.sort(userNotes, new NoteComparator(NoteParameter.Header));
        formatter.showShortNotes(userNotes);
        int index = userInput.getInt("Indice a modificar[0= volver]", 0, userNotes.size());

        if (index == 0) return;

        Note note = userNotes.get(index - 1);
        formatter.showNote(note);

        OutputFormatter.showMenu("1. Titulo", "2. Color", "3. Prioridad", "4. Tags", "5. Cuerpo", "0. Volver");
        int option = userInput.getInt("Campo a modificar", 0, 5);

        switch (option) {
            case 1:
                editTitle(note);
                break;
            case 2:
                editColor(note);
                break;
            case 3:
                editPriority(note);
                break;
            case 4:
                editTags(note);
                break;
            case 5:
                editBody(note);
                break;
            case 0:
                break;
        }
    }

    /**
     * Allows the user to edit the tags of a note.
     * The method displays the current tags of the note and prompts the user to enter the new tags.
     * The new tags are set for the note, and a success message is displayed.
     *
     * @param note the note to edit
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void editTags(Note note) throws IOException {
        System.out.println("Tags antiguas : " + OutputFormatter.formatTags(note.getTags()));

        List<String> tags = userInput.getTags("Tags nuevos");

        note.setTags(tags);
        OutputFormatter.showSuccess("Tags actualizadas con exito");
    }

    /**
     * Allows the user to edit the body of a note.
     * The method displays the current body of the note and prompts the user to enter the new body.
     * The new body is set for the note, and a success message is displayed.
     *
     * @param note the note to edit
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void editBody(Note note) throws IOException {
        System.out.println("Cuerpo antiguo : ");
        System.out.println(OutputFormatter.adjustLine(note.getBody(), formatter.lineLength));

        String body = userInput.getText("Cuerpo nuevo", 1, bodyMaxLength);

        note.setBody(body);
        OutputFormatter.showSuccess("Cuerpo actualizado con exito");
    }

    /**
     * Allows the user to edit the priority of a note.
     * The method displays the current priority of the note and prompts the user to enter the new priority.
     * The new priority is set for the note, and a success message is displayed.
     *
     * @param note the note to edit
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void editPriority(Note note) throws IOException {
        System.out.println("Prioridad Antigua : " + note.getPriority());

        Priority priority = userInput.getPriority("Prioridad Nueva");

        note.setPriority(priority);
        OutputFormatter.showSuccess("Prioridad Actualizada con exito");
    }

    /**
     * Allows the user to edit the color of a note.
     * The method displays the current color of the note and prompts the user to enter the new color.
     * The new color is set for the note, and a success message is displayed.
     *
     * @param note the note to edit
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void editColor(Note note) throws IOException {
        System.out.println("Color Antiguo: " + note.getColor());
        String newColor = userInput.getText("Color Nuevo: ");
        note.setColor(newColor);
        OutputFormatter.showSuccess("Color cambiado con exito");
    }

    /**
     * Allows the user to edit the title of a note.
     * The method displays the current title of the note and prompts the user to enter the new title.
     * The new title is set for the note, and a success message is displayed.
     *
     * @param note the note to edit
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void editTitle(Note note) throws IOException {
        System.out.println("Titulo Antiguo : " + note.getHeader());
        String newHeader = userInput.getText("Titulo Nuevo", 1, headerMaxLength);
        note.setHeader(newHeader);
        OutputFormatter.showSuccess("Titulo cambiado con exito");
    }

    /**
     * Displays the delete menu, allowing the user to select a note to delete.
     * The method shows a list of user notes, prompts the user to enter the index of the note to delete,
     * marks the selected note as deleted, and displays a success message.
     *
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void deleteMenu() throws IOException {
        List<Note> userNotes = notes.getUserNotes(user);
        formatter.showShortNotes(userNotes);
        int index = userInput.getInt("Indice a borrar[0= volver]", 0, userNotes.size());

        if (index == 0) return;

        userNotes.get(index - 1).markAsDeleted();
        OutputFormatter.showSuccess("Nota eliminada con exito");
    }

    /**
     * Displays the trash menu, allowing the user to perform operations on the notes in the trash.
     * The method shows the trash header, displays the short notes of the notes in the trash,
     * prompts the user to choose an operation, and performs the selected operation accordingly.
     *
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void trashMenu() throws IOException {
        formatter.showEncloseHeader("Papelera de Reciclaje");

        formatter.showShortNotes(notes.getTrashNotes(user));

        OutputFormatter.showPromptLn("Que desea hacer en la papelera");
        OutputFormatter.showMenu("1. Eliminar una nota", "2. Eliminar todo", "3. Restaurar una nota", "4. Restaurar todo", "0. Volver atras");
        int option = userInput.getInt("Opción", 0, 4);

        switch (option) {
            case 0:
                return;
            case 1:
                deleteOneNoteMenu();
                break;
            case 2:
                deleteAllNotes();
                break;
            case 3:
                recoverOneNote();
                break;
            case 4:
                recoverAllNotes();
                break;
        }
    }

    /**
     * Prompts the user to choose a note from the trash to recover and restores the selected note.
     * The method displays the short notes of the notes in the trash, prompts the user to enter the index
     * of the note to recover, and restores the selected note from the trash.
     *
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void recoverOneNote() throws IOException {
        List<Note> trashNotes = notes.getTrashNotes(user);

        formatter.showShortNotes(trashNotes);

        int index = userInput.getInt("Indice a recuperar[0= volver]: ", 0, trashNotes.size());

        if (index == 0) return;

        trashNotes.get(index - 1).restore();
        OutputFormatter.showSuccess("Nota recuperada con exito");
    }

    /**
     * Prompts the user for confirmation and recovers all notes from the trash.
     * The method retrieves all notes in the trash belonging to the user,
     * prompts the user for confirmation to recover all notes,
     * and restores each note from the trash.
     *
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void recoverAllNotes() throws IOException {
        List<Note> trashNotes = notes.getTrashNotes(user);

        if (!userInput.getConfirmation("Desea recuperar todas las notas")) return;

        for (Note note : trashNotes) {
            note.restore();
        }

        OutputFormatter.showSuccess("Notas recuperadas con exito");
    }


    /**
     * Prompts the user to select a note from the trash to delete permanently.
     * The method retrieves all notes in the trash belonging to the user,
     * prompts the user to select a note to delete permanently,
     * and removes the selected note from the notes collection.
     *
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void deleteOneNoteMenu() throws IOException {
        List<Note> trashNotes = notes.getTrashNotes(user);

        int index = userInput.getInt("Indice a borrar definitivamente[0= volver]: ", 0, trashNotes.size());

        if (index == 0) return;

        Note targetToRemove = trashNotes.get(index - 1);
        if (notes.removeNote(targetToRemove)) {
            OutputFormatter.showSuccess("Nota eliminada con exito");
        }
        OutputFormatter.showError("Algo raro pasa, la nota no se borro");
    }

    /**
     * Prompts the user to confirm the deletion of all notes in the trash.
     * If confirmed, deletes all notes in the trash permanently.
     *
     * @throws IOException if an I/O error occurs while reading user input
     */
    private void deleteAllNotes() throws IOException {
        List<Note> trashNotes = notes.getTrashNotes(user);

        if (!userInput.getConfirmation("Desea eliminar definitivamente todas las notas")) return;

        for (Note targetToRemove : trashNotes) {
            if (notes.removeNote(targetToRemove)) {
                OutputFormatter.showSuccess("Nota eliminada con exito");
            } else OutputFormatter.showError("Algo raro pasa, la nota no se borro");
        }
    }
}
