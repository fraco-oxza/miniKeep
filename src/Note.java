import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Note implements Serializable {
    private String header;
    private ArrayList<String> tags;
    private String body;
    private String color;
    private Priority priority;

    public long getCreated_by() {
        return created_by;
    }

    private final long created_by;
    private Date created_at, updated_at, viewed_at;
    private boolean deleted;

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append(header).append("\n\n");

        if (tags != null) {
            output.append("Tags       : ");
            for (int i = 0; i < tags.size(); i++) {
                output.append(tags.get(i));
                if (i != tags.size() - 1) {
                    output.append(", ");
                }
            }
        }

        output.append("Color      : ").append(color).append("\n");

        output.append("Prioridad  : ");
        switch (priority) {
            case Low:
                output.append("Baja");
                break;
            case Normal:
                output.append("Normal");
                break;
            case High:
                output.append("Alta");
                break;
            case Critical:
                output.append("Critica");
                break;
        }
        output.append("\n");

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        output.append("Creado     : ").append(format.format(created_at)).append("\n");
        output.append("Visitado   : ").append(format.format(viewed_at)).append("\n");
        output.append("Modificado : ").append(format.format(updated_at)).append("\n\n");

        output.append(body);

        this.viewed_at = new Date();
        return output.toString();
    }

    public Note(String header, String body, String color, Priority priority, int created_by) {
        this.header = header;
        this.body = body;
        this.color = color;
        this.priority = priority;
        this.created_by = created_by;
        this.deleted = false;

        this.updated_at = new Date();
        this.created_at = new Date();
        this.viewed_at = new Date();

        Notes.getInstance().addNote(this);
    }

    public void setHeader(String header) {
        this.header = header;
        Notes.getInstance().save();
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;

        Notes.getInstance().save();
    }

    public void setBody(String body) {
        this.body = body;

        this.updated_at = new Date();
        Notes.getInstance().save();
    }

    public void setColor(String color) {
        this.color = color;

        this.updated_at = new Date();
        Notes.getInstance().save();
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        Notes.getInstance().save();
    }

    public void markAsDeleted() {
        deleted = true;
        updated_at = new Date();

        Notes.getInstance().save();
    }

    public void restore() {
        this.deleted = false;
        updated_at = new Date();

        Notes.getInstance().save();
    }
}
