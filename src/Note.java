import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Note implements Serializable {
    private String header;
    private ArrayList<String> tags;
    private String body;
    private String color;
    private Priority priority;

    public long getCreatedBy() {
        return createdBy;
    }

    private final long createdBy;
    private final Date createdAt;
    private Date updatedAt;
    private Date viewedAt;
    private boolean deleted;

    @Override
    public String toString() {

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String output = "\n" + header + "\n\n" +
                "Tags       : " + OutputFormatter.formatTags(tags) +
                "\nColor      : " + color + "\n" +
                "Prioridad  : " + priority +
                "\n" +
                "Creado     : " + format.format(createdAt) + "\n" +
                "Visitado   : " + format.format(viewedAt) + "\n" +
                "Modificado : " + format.format(updatedAt) + "\n\n" +
                body + "\n \n";

        this.viewedAt = new Date();
        Notes.getInstance().save();

        return output;
    }

    public String getHeader() {
        return header;
    }

    public String getColor() {
        return color;
    }

    public Priority getPriority() {
        return priority;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getBody() {
        return body;
    }

    public Note(String header, String body, List<String> tags, String color, Priority priority, long created_by) {
        this.header = header;
        this.body = body;
        this.setTags(tags); // Read the method to understand
        this.color = color;
        this.priority = priority;
        this.createdBy = created_by;
        this.deleted = false;

        this.updatedAt = new Date();
        this.createdAt = new Date();
        this.viewedAt = new Date();

        Notes.getInstance().addNote(this);
    }

    public void setHeader(String header) {
        this.header = header;
        Notes.getInstance().save();
    }

    public void setTags(List<String> tags) {
        this.tags = new ArrayList<>();

        // It is done in this way to avoid that this value can be modified without going through this method
        // if not copied this way, we would just assign a reference to tags, so someone with that reference could modify
        // it without us knowing, and put the save file and this object out of sync
        this.tags.addAll(tags);

        Notes.getInstance().save();
    }

    public void setBody(String body) {
        this.body = body;

        this.updatedAt = new Date();
        Notes.getInstance().save();
    }

    public void setColor(String color) {
        this.color = color;

        this.updatedAt = new Date();
        Notes.getInstance().save();
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        Notes.getInstance().save();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void markAsDeleted() {
        deleted = true;
        updatedAt = new Date();

        Notes.getInstance().save();
    }

    public void restore() {
        this.deleted = false;
        updatedAt = new Date();

        Notes.getInstance().save();
    }
}
