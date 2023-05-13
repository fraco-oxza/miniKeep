import java.util.*;

public class MenuInteraction {
    private User user;
    private final UserInput userInput = UserInput.getInstance();
    private static final int headerMaxLength = 30;
    private static final int bodyMaxLength = 200;
    private boolean userWantsToExit;

    public MenuInteraction() {
        user = null;
        userWantsToExit = false;
    }

    public void startLoop() {
        do {
            if (user == null) {
                sessionMenu();
            } else {
                generalMenu();
            }
        } while (!userWantsToExit);
    }

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

    private void signIn() {
        String email = userInput.getText("Correo", UserInput.emailPattern);
        String password = userInput.getText("Clave ");

        User possibleUser = Users.getInstance().getUser(email, password);

        if (possibleUser == null) {
            OutputFormatter.showError("Email y/o contraseña incorrecto");
        } else {
            OutputFormatter.showSuccess("Inicio de sesion exitoso");
            user = possibleUser;
        }
    }

    private void signUp() {
        try {
            User user = userInput.addNewUser();
            OutputFormatter.showSuccess("Usuario creado con exito");
            OutputFormatter.showSuccess("Su clave es : " + user.getPassword());
        } catch (UserAlreadyExistsException e) {
            OutputFormatter.showError("Ya existe un usuario con su numero de matricula o correo");
        }
    }

    private void generalMenu() {
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
            case 7:
                user = null;
                break;
            case 0:
                userWantsToExit = true;
                break;
        }
    }

    private void userMenu() {
        OutputFormatter.showEncloseHeader("Datos de Usuario");
        OutputFormatter.showUser(user);
    }

    private void notesMenu() {
        System.out.println("En que orden desea mostrar las notas: ");
        OutputFormatter.showMenu("1. Nombre", "2. Fecha", "3. Prioridad", "4. Agrupados por color", "5. Agrupados por temas");
        int option = userInput.getInt("Opción", 1, 5);

        List<Note> userNotes = Notes.getInstance().getUserNotes(user);

        Collections.sort(userNotes, new NoteComparator(NoteParameter.UpdateDate));
        switch (option) {
            case 1:
                Collections.sort(userNotes, new NoteComparator(NoteParameter.Header));
                OutputFormatter.showNotes(userNotes);
                break;
            case 2:
                OutputFormatter.showMenu();
                OutputFormatter.showMenu("1. Creacion", "2. Ultima Actualizacion", "3. Ultima Visita");
                NoteParameter parameter = NoteParameter.from(userInput.getInt("Ordenar por", 1, 3));
                Collections.sort(userNotes, new NoteComparator(parameter));
                OutputFormatter.showNotes(userNotes);
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

    private void notesByTagMenu(List<Note> userNotes) {
        OutputFormatter.showMenu("1. Todas las notas", "2. Tag especifica");
        int option = userInput.getInt("Que desea mostrar", 1, 2);

        if (option == 1) {
            OutputFormatter.showNotesByTag(userNotes);
        } else {
            HashSet<String> availableTagsMap = new HashSet<>();

            for (Note note : userNotes) {
                availableTagsMap.addAll(note.getTags());
            }

            ArrayList<String> availableTags = new ArrayList<>(availableTagsMap);

            String tag = userInput.pickOne("Indice de la Tag: ", availableTags);
            OutputFormatter.showNotesWithTag(userNotes, tag);
        }
    }

    private void notesByColorMenu(List<Note> userNotes) {
        OutputFormatter.showMenu("1. Todas las notas", "2. Color especifico");
        int option = userInput.getInt("Que desea mostrar", 1, 2);

        if (option == 1) {
            OutputFormatter.showNotesByColor(userNotes);
        } else {
            HashSet<String> availableColorsMap = new HashSet<>();

            for (Note note : userNotes) {
                availableColorsMap.add(note.getColor().toLowerCase());
            }

            ArrayList<String> availableColors = new ArrayList<>(availableColorsMap);

            String color = userInput.pickOne("Indice del color", availableColors);
            OutputFormatter.showNotesWithColor(userNotes, color);
        }
    }

    private void notesByPriorityMenu(List<Note> userNotes) {
        Priority priority = userInput.getOptionalPriority("Prioridad a mostrar");

        if (priority == null) {
            OutputFormatter.showNotesByPriority(userNotes);
        } else {
            OutputFormatter.showNotesWithPriority(userNotes, priority);
        }
    }

    private void createNoteMenu() {
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

    private void editMenu() {
        List<Note> userNotes = Notes.getInstance().getUserNotes(user);
        Collections.sort(userNotes, new NoteComparator(NoteParameter.Header));
        OutputFormatter.showShortNotes(userNotes);
        int index = userInput.getInt("Indice a modificar[0= volver]", 0, userNotes.size());

        if (index == 0) return;

        Note note = userNotes.get(index - 1);
        OutputFormatter.showNote(note);

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

    private void editTags(Note note) {
        System.out.println("Tags antiguas : " + OutputFormatter.formatTags(note.getTags()));

        List<String> tags = userInput.getTags("Tags nuevos");

        note.setTags(tags);
        OutputFormatter.showSuccess("Tags actualizadas con exito");
    }

    private void editBody(Note note) {
        System.out.println("Cuerpo antiguo : ");
        System.out.println(OutputFormatter.adjustLine(note.getBody(), OutputFormatter.lineLength));

        String body = userInput.getText("Cuerpo nuevo", 1, bodyMaxLength);

        note.setBody(body);
        OutputFormatter.showSuccess("Cuerpo actualizado con exito");
    }

    private void editPriority(Note note) {
        System.out.println("Prioridad Antigua : " + note.getPriority());

        Priority priority = userInput.getPriority("Prioridad Nueva");

        note.setPriority(priority);
        OutputFormatter.showSuccess("Prioridad Actualizada con exito");
    }

    private void editColor(Note note) {
        System.out.println("Color Antiguo: " + note.getColor());
        String newColor = userInput.getText("Color Nuevo: ");
        note.setColor(newColor);
        OutputFormatter.showSuccess("Color cambiado con exito");
    }

    private void editTitle(Note note) {
        System.out.println("Titulo Antiguo : " + note.getHeader());
        String newHeader = userInput.getText("Titulo Nuevo", 1, headerMaxLength);
        note.setHeader(newHeader);
        OutputFormatter.showSuccess("Titulo cambiado con exito");
    }

    private void deleteMenu() {
        List<Note> userNotes = Notes.getInstance().getUserNotes(user);
        OutputFormatter.showShortNotes(userNotes);
        int index = userInput.getInt("Indice a borrar[0= volver]", 0, userNotes.size());

        if (index == 0) return;

        userNotes.get(index - 1).markAsDeleted();
        OutputFormatter.showSuccess("Nota eliminada con exito");
    }

    private void trashMenu() {
        OutputFormatter.showEncloseHeader("Papelera de Reciclaje");

        OutputFormatter.showShortNotes(Notes.getInstance().getTrashNotes(user));

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

    private void recoverOneNote() {
        List<Note> trashNotes = Notes.getInstance().getTrashNotes(user);

        OutputFormatter.showShortNotes(trashNotes);

        int index = userInput.getInt("Indice a recuperar[0= volver]: ", 0, trashNotes.size());

        if (index == 0) return;

        trashNotes.get(index - 1).restore();
        OutputFormatter.showSuccess("Nota recuperada con exito");
    }

    private void recoverAllNotes() {
        List<Note> trashNotes = Notes.getInstance().getTrashNotes(user);

        if (!userInput.getConfirmation("Desea recuperar todas las notas")) return;

        for (Note note : trashNotes) {
            note.restore();
        }

        OutputFormatter.showSuccess("Notas recuperadas con exito");
    }

    private void deleteOneNoteMenu() {
        List<Note> trashNotes = Notes.getInstance().getTrashNotes(user);

        int index = userInput.getInt("Indice a borrar definitivamente[0= volver]: ", 0, trashNotes.size());

        if (index == 0) return;

        Note targetToRemove = trashNotes.get(index - 1);
        if (Notes.getInstance().removeNote(targetToRemove)) {
            OutputFormatter.showSuccess("Nota eliminada con exito");
        }
        OutputFormatter.showError("Algo raro pasa, la nota no se borro");
    }

    private void deleteAllNotes() {
        List<Note> trashNotes = Notes.getInstance().getTrashNotes(user);

        if (!userInput.getConfirmation("Desea eliminar definitivamente todas las notas")) return;

        for (Note targetToRemove : trashNotes) {
            if (Notes.getInstance().removeNote(targetToRemove)) {
                OutputFormatter.showSuccess("Nota eliminada con exito");
            } else OutputFormatter.showError("Algo raro pasa, la nota no se borro");
        }
    }
}
