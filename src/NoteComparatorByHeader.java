import java.util.Comparator;

public class NoteComparatorByHeader implements Comparator<Note> {
    @Override
    public int compare(Note o1, Note o2) {
        return o1.getHeader().compareTo(o2.getHeader());
    }
}
