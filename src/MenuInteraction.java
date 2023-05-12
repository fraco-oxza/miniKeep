import java.util.*;

public class MenuInteraction {
    private User user;
    private final UserInput userInput = UserInput.getInstance();
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

    public void signIn() {
        String email = userInput.getText("Correo : ", UserInput.emailPattern);
        String password = userInput.getText("Clave  : ");

        User possibleUser = Users.get_instance().getUser(email, password);

        if (possibleUser == null) {
            System.out.println("!! Email y/o contraseña incorrecto");
        } else {
            System.out.println("## Inicio de sesion exitoso");
            user = possibleUser;
        }
    }

    public void signUp() {
        try {
            User user = Users.get_instance().addNewUserFromStdin();
            System.out.println("## Usuario creado con exito");
            System.out.println("## Su clave es : " + user.getPassword());
        } catch (UserAlreadyExistsException e) {
            System.out.println("!! Ya existe un usuario con su numero de matricula o correo");
        }
    }

    public void sessionMenu() {
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Crear cuenta");
        System.out.println("0. Salir");
        int option = userInput.getInt("Opción: ", 0, 2);

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

    public void generalMenu() {
        System.out.println("1. Ver Notas");
        System.out.println("2. Crear Nota");
        System.out.println("3. Editar Nota");
        System.out.println("4. Eliminar Nota");
        System.out.println("5. Ver papelera");
        System.out.println("6. Cerrar sesión");
        System.out.println("0. Salir");

        int option = userInput.getInt("Opción: ", 0, 6);

        switch (option) {
            case 1:
                notesMenu();
                break;
            case 2:
                createNoteMenu();
                break;
            case 4:
                deleteMenu();
                break;
            case 5:
                trashMenu();
                break;
            case 6:
                user = null;
                break;
            case 0:
                userWantsToExit = true;
                break;

        }
    }

    private void recoverOneNote() {
        ArrayList<Note> trashNotes = Notes.getInstance().getTrashNotes(user);
        System.out.println();
        showShortNotes(trashNotes);
        System.out.println();
        int index = userInput.getInt("Indice a recuperar[0= volver]: ", 0, trashNotes.size());

        if (index == 0) return;

        trashNotes.get(index - 1).restore();
        System.out.println("## Nota recuperada con exito");
    }

    private void recoverAllNotes() {
        ArrayList<Note> trashNotes = Notes.getInstance().getTrashNotes(user);

        // TODO: pedir confirmacion

        for (Note note : trashNotes) {
            note.restore();
        }
        System.out.println("## Notas recuperadas con exito");
    }

    private void trashMenu() {
        System.out.println();
        showShortNotes(Notes.getInstance().getTrashNotes(user));
        System.out.println();

        System.out.println("Que desea hacer en la papelera");
        System.out.println("1. Eliminar una nota");
        System.out.println("2. Eliminar todo");
        System.out.println("3. Restaurar una nota");
        System.out.println("4. Restaurar todo");
        System.out.println("0. Volver atras");
        int option = userInput.getInt("Opción: ", 0, 4);

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
                // TODO: Check this methods
                recoverOneNote(); // <-
                break;
            case 4:
                recoverAllNotes(); // <-
                break;

        }
    }

    private void deleteOneNoteMenu() {
        ArrayList<Note> trashNotes = Notes.getInstance().getTrashNotes(user);

        int index = userInput.getInt("Indice a borrar definitivamente[0= volver]: ", 0, trashNotes.size());

        if (index == 0) return;

        Note targetToRemove = trashNotes.get(index - 1);
        if (Notes.getInstance().notes.remove(targetToRemove)) {
            Notes.getInstance().save();
            System.out.println("## Nota eliminada con exito");
        } else System.err.println("Algo raro pasa, la nota no se borro");
    }

    private void deleteAllNotes() {
        ArrayList<Note> trashNotes = Notes.getInstance().getTrashNotes(user);

        // TODO: pedir una confirmacion

        for (Note targetToRemove : trashNotes) {
            if (Notes.getInstance().notes.remove(targetToRemove)) {
                Notes.getInstance().save();
                System.out.println("## Nota eliminada con exito");
            } else System.err.println("Algo raro pasa, la nota no se borro");
        }
    }

    private void deleteMenu() {
        ArrayList<Note> userNotes = Notes.getInstance().getUserNotes(user);
        System.out.println();
        showShortNotes(userNotes);
        System.out.println();
        int index = userInput.getInt("Indice a borrar[0= volver]: ", 0, userNotes.size());

        if (index == 0) return;

        userNotes.get(index - 1).markAsDeleted();
        System.out.println("## Nota eliminada con exito");
    }

    private void createNoteMenu() {
        System.out.println("Creando nota");
        System.out.println("Debe ingresar los siguientes campos");

        String header = userInput.getText("Titulo: ", 1, 30);

        String body = userInput.getText("Cuerpo: ", 1, 200);

        String textOfTags = userInput.getText("Tags[separadas por ',']: ");
        textOfTags = textOfTags.trim().toLowerCase();
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(textOfTags.split("\\s*,\\s*")));

        String color = userInput.getText("Color: ");

        System.out.println("Prioridad: ");
        System.out.println("  0. Baja");
        System.out.println("  1. Normal");
        System.out.println("  2. Alta");
        System.out.println("  3. Critica");
        int option = userInput.getInt("Numero: ", 0, 3);

        Priority priority = null;

        switch (option) {
            case 0:
                priority = Priority.Low;
                break;
            case 1:
                priority = Priority.Normal;
                break;
            case 2:
                priority = Priority.High;
                break;
            case 3:
                priority = Priority.Critical;
                break;
        }

        // The note is immediately saved in the file through the Notes instance
        new Note(header, body, tags, color, priority, user.getRegistration_number());
    }

    public static void showShortNotes(ArrayList<Note> notes) {
        int i = 1;
        for (Note note : notes) {
            System.out.printf("%d. %s\n", i, note.getHeader());
            i++;
        }
    }

    public static void showNotes(ArrayList<Note> notes) {
        System.out.println(EncloseString.encloseIterable(notes));
    }

    public static void showNotesByPriority(ArrayList<Note> notes) {
        HashMap<Priority, ArrayList<Note>> notesCluster = new HashMap<>();

        notesCluster.put(Priority.Low, new ArrayList<Note>());
        notesCluster.put(Priority.Normal, new ArrayList<Note>());
        notesCluster.put(Priority.High, new ArrayList<Note>());
        notesCluster.put(Priority.Critical, new ArrayList<Note>());

        for (Note note : notes) {
            notesCluster.get(note.getPriority()).add(note);
        }

        System.out.println(EncloseString.encloseLevel1("Prioridad Baja"));
        showNotes(notesCluster.get(Priority.Low));

        System.out.println(EncloseString.encloseLevel1("Prioridad Normal"));
        showNotes(notesCluster.get(Priority.Normal));

        System.out.println(EncloseString.encloseLevel1("Prioridad Alta"));
        showNotes(notesCluster.get(Priority.High));

        System.out.println(EncloseString.encloseLevel1("Prioridad Critica"));
        showNotes(notesCluster.get(Priority.Critical));
    }

    public static void showNotesByColor(ArrayList<Note> notes) {
        HashMap<String, ArrayList<Note>> notesCluster = new HashMap<>();

        for (Note note : notes) {
            if (notesCluster.containsKey(note.getColor())) {
                notesCluster.get(note.getColor()).add(note);
            } else {
                notesCluster.put(note.getColor(), new ArrayList<>(Collections.singletonList(note)));
            }
        }

        for (String color : notesCluster.keySet()) {

            if (color.equals("")) System.out.println(EncloseString.encloseLevel1("Sin color"));
            else System.out.println(EncloseString.encloseLevel1(color));

            showNotes(notesCluster.get(color));
        }
    }

    public void notesMenu() {
        System.out.println("Mostrar las notas ordenadas por: ");
        System.out.println("1. Nombre");
        System.out.println("2. Fecha de creación");
        System.out.println("3. Prioridad");
        System.out.println("4. Agrupados por color");
        System.out.println("5. Agrupados por temas");
        int option = userInput.getInt("Opción: ", 1, 5);

        ArrayList<Note> userNotes = Notes.getInstance().getUserNotes(user);

        switch (option) {
            case 1:
                Collections.sort(userNotes, new NoteComparator(NoteParameter.Header));
                showNotes(userNotes);
                break;
            case 2:
                Collections.sort(userNotes, new NoteComparator(NoteParameter.CreationDate));
                showNotes(userNotes);
                break;
            case 3:
                Collections.sort(userNotes, new NoteComparator(NoteParameter.CreationDate));
                showNotesByPriority(userNotes);
                break;
            case 4:
                Collections.sort(userNotes, new NoteComparator(NoteParameter.CreationDate));
                showNotesByColor(userNotes);
                break;
            case 5:
                Collections.sort(userNotes, new NoteComparator(NoteParameter.CreationDate));
                showNotesByTag(userNotes);
                break;
            case 0:
                userWantsToExit = true;
                break;

        }

    }

    private void showNotesByTag(ArrayList<Note> userNotes) {
        HashMap<String, ArrayList<Note>> tagNotesMap = new HashMap<>();

        for (Note note : userNotes) {
            for (String tag : note.getTags()) {
                if (tagNotesMap.containsKey(tag)) {
                    tagNotesMap.get(tag).add(note);
                } else {
                    tagNotesMap.put(tag, new ArrayList<>(Collections.singletonList(note)));
                }
            }
        }

        System.out.println("======================================");
        for (String tag : tagNotesMap.keySet()) {

            if (tag.equals("")) System.out.println("\nSin Tag\n");
            else System.out.println("\n" + tag + "\n");

            showNotes(tagNotesMap.get(tag));
        }
        System.out.println("======================================");
    }
}
