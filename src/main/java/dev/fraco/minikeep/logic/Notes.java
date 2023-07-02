package dev.fraco.minikeep.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a notes repository that utilizes the Singleton pattern. It ensures that
 * there is only a single instance of this class throughout the program. The class possesses a
 * special feature: whenever a change is made to the user list, it immediately persists the changes
 * to a file, providing persistence across program executions. Additionally, when the class is
 * instantiated, it loads the existing data from the file, if any.
 */
public class Notes {
    private static Notes notesInstance = null;
    private ArrayList<Note> notes;

    /**
     * Private constructor of the Notes class.
     * This constructor is private to follow the Singleton design pattern and ensure
     * that only one instance of the Notes class exists throughout the system.
     * The load method is invoked to load the notes from storage.
     * If the notes file is not found, a new empty list is created.
     * Any exceptions that occur during the loading process are thrown as a RuntimeException.
     *
     * @throws RuntimeException if an error occurs while loading the notes.
     */
    private Notes() {
        try {
            load();
        } catch (FileNotFoundException e) {
            notes = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the singleton instance of the Notes class.
     *
     * @return The singleton instance of Notes.
     */
    public static synchronized Notes getInstance() {
        if (notesInstance == null) notesInstance = new Notes();
        return notesInstance;
    }

    /**
     * Adds a new note to the collection.
     *
     * @param note The note to be added.
     * @throws IOException If an error occurs while saving the notes to the file.
     */
    public void addNote(Note note) throws IOException {
        this.notes.add(note);
        save();
    }

    /**
     * Removes a note from the collection.
     *
     * @param note The note to be removed.
     * @throws IOException If an error occurs while saving the notes to the file.
     */
    public void removeNote(Note note) throws IOException {
        notes.remove(note);
        save();
    }

    /**
     * Retrieves the trashed notes belonging to the specified user.
     *
     * @param user The user whose trashed notes are to be retrieved.
     * @return A list of trashed notes belonging to the user.
     */
    public List<Note> getTrashNotes(User user) {
        ArrayList<Note> userNotes = new ArrayList<>();

        for (Note note : this.notes) {
            if (note.getCreatedBy() == user.getRegistrationNumber() && note.isDeleted()) {
                userNotes.add(note);
            }
        }

        return userNotes;
    }

    /**
     * Retrieves the notes belonging to the specified user.
     *
     * @param user The user whose notes are to be retrieved.
     * @return A list of notes belonging to the user.
     */
    public List<Note> getUserNotes(User user) {
        ArrayList<Note> userNotes = new ArrayList<>();

        for (Note note : this.notes) {
            if ((note.getCreatedBy() == user.getRegistrationNumber() || note.getCollaborators().contains(user.getRegistrationNumber())) && !note.isDeleted()) {
                userNotes.add(note);
            }

        }

        return userNotes;
    }

    /**
     * Searches for notes that match the given search expression within the specified user's notes.
     *
     * @param user The user whose notes to search.
     * @param exp  The search expression.
     * @return A list of notes that match the search expression.
     */
    public List<Note> searchNote(User user, String exp) {
        List<Note> userNotes = getUserNotes(user);
        ArrayList<Note> filteredNotes = new ArrayList<>();
        exp = exp.toLowerCase();

        for (Note note : userNotes) {
            if (note.getHeader().toLowerCase().contains(exp) || note.getTag().toLowerCase().contains(exp) || note.getBody().toLowerCase().contains(exp)) {
                filteredNotes.add(note);
            }
        }

        return filteredNotes;
    }

    /**
     * Loads the notes from a serialized file.
     *
     * @throws IOException            If an I/O error occurs while reading the file.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    @SuppressWarnings("unchecked")
    private void load() throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream("./notes.ser.bin");
        ObjectInputStream objIn = new ObjectInputStream(file);
        notes = (ArrayList<Note>) objIn.readObject();
        objIn.close();
        file.close();
    }

    /**
     * Saves the notes to a serialized file.
     *
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public void save() throws IOException {
        FileOutputStream file = new FileOutputStream("./notes.ser.bin");
        ObjectOutputStream objOut = new ObjectOutputStream(file);
        objOut.writeObject(notes);
        objOut.close();
        file.close();
    }
}
