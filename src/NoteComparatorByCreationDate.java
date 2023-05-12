import java.util.Comparator;

public class NoteComparatorByCreationDate implements Comparator<Note> {
    @Override
    public int compare(Note o1, Note o2) {
        return o1.getCreated_at().compareTo(o2.getCreated_at());
    }
}
