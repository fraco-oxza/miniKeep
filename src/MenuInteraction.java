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
    OutputFormatter.showMenu(
        "1. Ver Notas",
        "2. Crear Nota",
        "3. Editar Nota",
        "4. Eliminar Nota",
        "5. Ver papelera",
        "6. Cerrar sesión",
        "0. Salir");
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
        user = null;
        break;
      case 0:
        userWantsToExit = true;
        break;
    }
  }

  private void notesMenu() {
    System.out.println("Mostrar las notas ordenadas por: ");
    OutputFormatter.showMenu(
        "1. Nombre",
        "2. Fecha de creación",
        "3. Prioridad",
        "4. Agrupados por color",
        "5. Agrupados por temas");
    int option = userInput.getInt("Opción: ", 1, 5);

    List<Note> userNotes = Notes.getInstance().getUserNotes(user);

    switch (option) {
      case 1:
        Collections.sort(userNotes, new NoteComparator(NoteParameter.Header));
        OutputFormatter.showNotes(userNotes);
        break;
      case 2:
        Collections.sort(userNotes, new NoteComparator(NoteParameter.CreationDate));
        OutputFormatter.showNotes(userNotes);
        break;
      case 3:
        Collections.sort(userNotes, new NoteComparator(NoteParameter.CreationDate));
        OutputFormatter.showNotesByPriority(userNotes);
        break;
      case 4:
        Collections.sort(userNotes, new NoteComparator(NoteParameter.CreationDate));
        OutputFormatter.showNotesByColor(userNotes);
        break;
      case 5:
        Collections.sort(userNotes, new NoteComparator(NoteParameter.CreationDate));
        OutputFormatter.showNotesByTag(userNotes);
        break;
      case 0:
        userWantsToExit = true;
        break;
    }
  }

  private void createNoteMenu() {
    System.out.println("Creando nota");
    System.out.println("Debe ingresar los siguientes campos");

    String header = userInput.getText("Titulo", 1, 30);

    String body = userInput.getText("Cuerpo", 1, 200);

    List<String> tags = userInput.getTags("Tags[separadas por ',']");

    String color = userInput.getText("Color");

    Priority priority = userInput.getPriority("Prioridad");

    // The note is immediately saved in the file through the Notes instance
    new Note(header, body, tags, color, priority, user.getRegistrationNumber());
    OutputFormatter.showSuccess("Nota creada con exito");
  }

  private void editMenu() {
    List<Note> userNotes = Notes.getInstance().getUserNotes(user);
    OutputFormatter.showShortNotes(userNotes);
    int index = userInput.getInt("Indice a modificar[0= volver]", 0, userNotes.size());

    if (index == 0) return;

    Note note = userNotes.get(index - 1);
    OutputFormatter.showNote(note);

    OutputFormatter.showMenu(
        "1. Titulo", "2. Color", "3. Prioridad", "4. Tags", "5. Cuerpo", "0. Volver");
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

    String body = userInput.getText("Cuerpo nuevo", 1, 200);

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
    String newHeader = userInput.getText("Titulo Nuevo", 1, 30);
    note.setHeader(newHeader);
    OutputFormatter.showSuccess("Titulo cambiado con exito");
  }

  private void deleteMenu() {
    List<Note> userNotes = Notes.getInstance().getUserNotes(user);
    OutputFormatter.showShortNotes(userNotes);
    int index = userInput.getInt("Indice a borrar[0= volver]: ", 0, userNotes.size());

    if (index == 0) return;

    userNotes.get(index - 1).markAsDeleted();
    OutputFormatter.showSuccess("Nota eliminada con exito");
  }

  private void trashMenu() {
    OutputFormatter.showEncloseHeader("Papelera de Reciclaje");

    OutputFormatter.showShortNotes(Notes.getInstance().getTrashNotes(user));

    System.out.println("Que desea hacer en la papelera");
    OutputFormatter.showMenu(
        "1. Eliminar una nota",
        "2. Eliminar todo",
        "3. Restaurar una nota",
        "4. Restaurar todo",
        "0. Volver atras");
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

    int index =
        userInput.getInt("Indice a borrar definitivamente[0= volver]: ", 0, trashNotes.size());

    if (index == 0) return;

    Note targetToRemove = trashNotes.get(index - 1);
    if (Notes.getInstance().removeNote(targetToRemove)) {
      System.out.println("## Nota eliminada con exito");
    } else System.err.println("Algo raro pasa, la nota no se borro");
  }

  private void deleteAllNotes() {
    List<Note> trashNotes = Notes.getInstance().getTrashNotes(user);

    if (!userInput.getConfirmation("Desea eliminar definitivamente todas las notas")) return;

    for (Note targetToRemove : trashNotes) {
      if (Notes.getInstance().removeNote(targetToRemove)) {
        OutputFormatter.showSuccess("## Nota eliminada con exito");
      } else System.err.println("Algo raro pasa, la nota no se borro");
    }
  }
}
