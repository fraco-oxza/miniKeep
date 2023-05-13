import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Notes {
    private static Notes notesInstance = null;
    ArrayList<Note> notes;

    @SuppressWarnings("unchecked")
    private Notes() {
        try {
            FileInputStream file = new FileInputStream("./notes.ser.bin");
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

    public synchronized static Notes getInstance() {
        if (notesInstance == null) notesInstance = new Notes();
        return notesInstance;
    }

    public void addNote(Note note) {
        this.notes.add(note);
        save();
    }

    public List<Note> getTrashNotes(User user) {
        ArrayList<Note> userNotes = new ArrayList<>();

        for (Note note : this.notes) {
            if (note.getCreatedBy() == user.getRegistrationNumber() && note.isDeleted()) {
                userNotes.add(note);
            }
        }

        return userNotes;
    }

    public List<Note> getUserNotes(User user) {
        ArrayList<Note> userNotes = new ArrayList<>();

        for (Note note : this.notes) {
            if (note.getCreatedBy() == user.getRegistrationNumber() && !note.isDeleted()) {
                userNotes.add(note);
            }
        }

        return userNotes;
    }

    public void save() {
        try {
            FileOutputStream file = new FileOutputStream("./notes.ser.bin");
            ObjectOutputStream objOut = new ObjectOutputStream(file);
            objOut.writeObject(notes);
            objOut.close();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
