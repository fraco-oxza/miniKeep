package dev.fraco.minikeep.logic;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This class represent one Note in the system. when it is created it is
 * immediately added to the notes file through the Notes object, also when
 * changes are made to it is automatically updates Notes therefore it is saved
 * in the persistence
 */
public class Note implements Serializable {
    // In this field we use the user's registration number.Now, why do we use the
    // registration number instead of using a reference to the user, the answer
    // is because of the serialization, when using a reference we are specifying
    // so to speak a memory address, there would be no problem at first, but when
    // loading the users from the file their memory locations may change and
    // generate problems. then for more security we use the registration number,
    // which we also ensure that it is unique foreach user registered in the system.
    private final long createdBy;

    public List<Long> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Long> collaborators) throws IOException {
        this.collaborators = collaborators;
        Notes.getInstance().save();

    }

    private List<Long> collaborators;
    private final Date createdAt;
    private String header;
    private String tag;
    private String body;
    private String color;
    private Priority priority;
    private Date updatedAt;
    private Date reminder;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) throws IOException {
        isDone = done;
        Notes.getInstance().save();

    }

    private boolean isDone;
    private boolean deleted;

    /**
     * Constructor of the class that is in charge of creating the Note and adding
     * it to the list of notes, to allow persistence and synchronization. Every
     * note that exists in the program is stored in the persistence.
     *
     * @param header     The title of the Note
     * @param body       The body of the Note
     * @param tag        A tag for the note
     * @param color      A javafx color for the color of the note
     * @param priority   The priority of the note
     * @param created_by The registration number of the creating user
     * @throws IOException if an error occurs in the serialization process
     */
    public Note(String header, String body, String tag, String color, Priority priority, List<Long> collaborators, User created_by) throws IOException {
        this.header = header;
        this.body = body;
        this.tag = tag;
        this.color = color;
        this.priority = priority;
        this.createdBy = created_by.getRegistrationNumber();
        this.collaborators = collaborators;
        this.deleted = false;
        this.reminder = null;

        this.updatedAt = new Date();
        this.createdAt = new Date();

        Notes.getInstance().addNote(this);
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) throws IOException {
        this.reminder = reminder;
        Notes.getInstance().save();

    }

    /**
     * Getter to get the header of the note
     *
     * @return The header of the note
     */
    public String getHeader() {
        return header;
    }

    /**
     * Setter to set the header of the note and then update the notes files
     *
     * @param header The new header of the note
     * @throws IOException if an error occurs in the serialization process
     */
    public void setHeader(String header) throws IOException {
        this.header = header;

        this.updatedAt = new Date();
        Notes.getInstance().save();
    }

    /**
     * Getter to get the registration number of the note creator
     *
     * @return The registration number of the note creator
     */
    public long getCreatedBy() {
        return createdBy;
    }

    /**
     * Getter to get the color of the note
     *
     * @return The color of the note
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter to set the color of the note and then update the file of notes
     *
     * @param color The new color for the note
     * @throws IOException if an error occurs in the serialization process
     */
    public void setColor(String color) throws IOException {
        this.color = color;

        this.updatedAt = new Date();
        Notes.getInstance().save();
    }

    /**
     * Getter to get the priority of the note
     *
     * @return The priority of the note
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Setter to set the priority of the note and update the file of notes
     *
     * @param priority The new priority of the note
     * @throws IOException if an error occurs in the serialization process
     */
    public void setPriority(Priority priority) throws IOException {
        this.priority = priority;

        this.updatedAt = new Date();
        Notes.getInstance().save();
    }

    /**
     * Getter to get the date on which the note was created
     *
     * @return The date on which the note was created
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Getter to get the tag in the note
     *
     * @return The list of tags in the note
     */
    public String getTag() {
        return tag;
    }

    /**
     * Setter to set a new tag for the note
     *
     * @param tag The new tag
     * @throws IOException if an error occurs in the serialization process
     */
    public void setTag(String tag) throws IOException {
        this.tag = tag;
        Notes.getInstance().save();
    }

    /**
     * Getter to get the body of the note
     *
     * @return The body of the note
     */
    public String getBody() {
        return body;
    }

    /**
     * Setter to set a new body for the note
     *
     * @param body The new body for the note
     */
    public void setBody(String body) throws IOException {
        this.body = body;

        this.updatedAt = new Date();
        Notes.getInstance().save();
    }

    /**
     * Getter to get the date on which the note was last changed
     *
     * @return Date of the last update
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }


    /**
     * Getter to get the state of the note, return true if the note has been marked as deleted.
     *
     * @return The state of the note
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Method to set the deleted field to true, and then update the Notes files
     *
     * @throws IOException If an error occurs when trying to write to the file
     */
    public void markAsDeleted() throws IOException {
        deleted = true;
        updatedAt = new Date();

        Notes.getInstance().save();
    }

    /**
     * Method to set the deleted field to false, and then update the Notes files
     *
     * @throws IOException If an error occurs when trying to write to the file
     */
    public void restore() throws IOException {
        this.deleted = false;
        updatedAt = new Date();

        Notes.getInstance().save();
    }

}
