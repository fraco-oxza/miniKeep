package persistence;

import note.Note;
import user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Notes implements Persistent {
    private static Notes notesInstance = null;
    private ArrayList<Note> notes;

    private Notes() {
        try {
            load();
        } catch (FileNotFoundException e) {
            notes = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized Notes getInstance() {
        if (notesInstance == null) notesInstance = new Notes();
        return notesInstance;
    }

    public void addNote(Note note) throws IOException {
        this.notes.add(note);
        save();
    }

    public boolean removeNote(Note note) throws IOException {
        boolean result = notes.remove(note);
        save();
        return result;
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

    @Override
    @SuppressWarnings("unchecked")
    public void load() throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream("./notes.ser.bin");
        ObjectInputStream objIn = new ObjectInputStream(file);
        notes = (ArrayList<Note>) objIn.readObject();
        objIn.close();
        file.close();
    }

    public void save() throws IOException {
        FileOutputStream file = new FileOutputStream("./notes.ser.bin");
        ObjectOutputStream objOut = new ObjectOutputStream(file);
        objOut.writeObject(notes);
        objOut.close();
        file.close();
    }
}
