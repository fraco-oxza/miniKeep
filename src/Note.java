import java.time.LocalDateTime;
import java.util.ArrayList;

public class Note {
    private String header;
    private ArrayList<String> tags;
    private String body;
    private String color;
    private Priority priority;
    private long created_by;
    private LocalDateTime created_at;
    private boolean deleted;

    @Override
    public String toString() {
        return "Note{" +
                "header='" + header + '\'' +
                ", tags=" + tags +
                ", body='" + body + '\'' +
                ", color='" + color + '\'' +
                ", priority=" + priority +
                ", created_by=" + created_by +
                ", created_at=" + created_at +
                ", deleted=" + deleted +
                '}';
    }

    public Note(String header, String body, String color, Priority priority, int created_by) {
        this.header = header;
        this.body = body;
        this.color = color;
        this.priority = priority;
        this.created_by = created_by;
        this.deleted = false;
        created_at = LocalDateTime.now();
    }
}
