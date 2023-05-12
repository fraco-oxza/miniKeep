import java.io.*;
import java.util.ArrayList;

public class Notes {
    private static Notes notesInstance = null;
    ArrayList<Note> notes;

    private Notes() {
        try {
            FileInputStream file = new FileInputStream("./notes.ser");
            ObjectInputStream objIn = new ObjectInputStream(file);
            notes = (ArrayList<Note>) objIn.readObject();
            objIn.close();
            file.close();
        } catch (FileNotFoundException e) {
            notes = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Notes getInstance() {
        if (notesInstance == null) notesInstance = new Notes();
        return notesInstance;
    }

    public void addNote(Note note) {
        this.notes.add(note);
        save();
    }

    public ArrayList<Note> getTrashNotes(User user) {
        ArrayList<Note> userNotes = new ArrayList<>();

        for (Note note : this.notes) {
            if (note.getCreated_by() == user.getRegistration_number()) {
                userNotes.add(note);
            }
        }

        return userNotes;
    }

    public ArrayList<Note> getUserNotes(User user) {
        ArrayList<Note> userNotes = new ArrayList<>();

        for (Note note : this.notes) {
            if (note.getCreated_by() == user.getRegistration_number() && !note.isDeleted()) {
                userNotes.add(note);
            }
        }

        return userNotes;
    }

    public void save() {
        try {
            FileOutputStream file = new FileOutputStream("./notes.ser");
            ObjectOutputStream objOut = new ObjectOutputStream(file);
            objOut.writeObject(notes);
            objOut.close();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
