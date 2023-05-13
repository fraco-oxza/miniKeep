import java.io.IOException;

public interface Persistent {
    void load() throws IOException, ClassNotFoundException;

    void save() throws IOException;
}
