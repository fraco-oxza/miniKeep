package note;

import ioUtils.OutputFormatter;
import persistence.Notes;
import user.User;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represent one note.Note in the system. when it is created it is immediately added to the
 * notes file through the persistence.Notes object, also when changes are made to it is automatically updates
 * persistence.Notes therefore it is saved in the persistence
 */
public class Note implements Serializable {
    // In this field we use the user's registration number.Now, why do we use the registration number
    // instead of using a reference to the user, the answer is because of the serialization, when
    // using a reference we are specifying so to speak a memory address, there would be no problem at
    // first, but when loading the users from the file their memory locations may change and generate
    // problems. then for more security we use the registration number, which we also ensure that it
    // is unique foreach user registered in the system.
    private final long createdBy;
    private final Date createdAt;
    private String header;
    private ArrayList<String> tags;
    private String body;
    private String color;
    private Priority priority;
    private Date updatedAt;
    private Date viewedAt;
    private boolean deleted;

    /**
     * Constructor of the class that is in charge of creating the note.Note and adding it to the list of
     * notes, to allow persistence and synchronization. Every note that exists in the program is
     * stored in the persistence.
     *
     * @param header     The title of the note.Note
     * @param body       The body of the note.Note
     * @param tags       A list of tags for the note
     * @param color      A string for the color of the note
     * @param priority   The priority of the note
     * @param created_by The registration number of the creating user
     */
    public Note(String header, String body, List<String> tags, String color, Priority priority, User created_by) throws IOException, ClassNotFoundException {
        this.header = header;
        this.body = body;
        this.setTags(tags); // Read the method to understand
        this.color = color;
        this.priority = priority;
        this.createdBy = created_by.getRegistrationNumber();
        this.deleted = false;

        this.updatedAt = new Date();
        this.createdAt = new Date();
        this.viewedAt = new Date();

        Notes.getInstance().addNote(this);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) throws IOException, ClassNotFoundException {
        this.header = header;
        Notes.getInstance().save();
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) throws IOException, ClassNotFoundException {
        this.color = color;

        this.updatedAt = new Date();
        Notes.getInstance().save();
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) throws IOException, ClassNotFoundException {
        this.priority = priority;
        Notes.getInstance().save();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) throws IOException, ClassNotFoundException {
        this.tags = new ArrayList<>();

        // It is done in this way to avoid that this value can be modified without going through this
        // method if not copied this way, we would just assign a reference to tags, so someone with that
        // reference could modify it without us knowing, and put the save file and this object out of
        // sync
        this.tags.addAll(tags);

        Notes.getInstance().save();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) throws IOException, ClassNotFoundException {
        this.body = body;

        this.updatedAt = new Date();
        Notes.getInstance().save();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getViewedAt() {
        return viewedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void markAsDeleted() throws IOException, ClassNotFoundException {
        deleted = true;
        updatedAt = new Date();

        Notes.getInstance().save();
    }

    public void restore() throws IOException, ClassNotFoundException {
        this.deleted = false;
        updatedAt = new Date();

        Notes.getInstance().save();
    }

    @Override
    public String toString() {

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // FIXME: Improve the visualization of the code
        String output = "\n" + header + "\n\n" + "Tags       : " + OutputFormatter.formatTags(tags) + "\nColor      : " + color + "\n" + "Prioridad  : " + priority + "\n" + "Creado     : " + format.format(createdAt) + "\n" + "Visitado   : " + format.format(viewedAt) + "\n" + "Modificado : " + format.format(updatedAt) + "\n\n" + body + "\n \n";

        this.viewedAt = new Date();

        try {
            Notes.getInstance().save();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return output;
    }
}
