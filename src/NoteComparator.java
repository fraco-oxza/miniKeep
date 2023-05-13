import java.util.Comparator;

public class NoteComparator implements Comparator<Note> {
    private final NoteParameter toCompare;

    NoteComparator(NoteParameter toCompare) {
        super();
        this.toCompare = toCompare;
    }

    @Override
    public int compare(Note o1, Note o2) {
        int result = 0;

        switch (toCompare) {
            case CreationDate:
                result = o1.getCreatedAt().compareTo(o2.getCreatedAt());
                break;
            case ViewDate:
                result = o1.getViewedAt().compareTo(o2.getViewedAt());
                break;
            case UpdateDate:
                result = o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
                break;
            case Header:
                result = o1.getHeader().compareTo(o2.getHeader());
                break;
        }

        return result;
    }
}
