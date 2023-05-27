import java.util.Comparator;

/**
 * The NoteComparator class is a comparator implementation used for comparing Note objects
 * based on a specified NoteParameter.
 */
public class NoteComparator implements Comparator<Note> {
    private final NoteParameter toCompare;

    /**
     * Constructs a NoteComparator object with the specified NoteParameter.
     *
     * @param toCompare the NoteParameter to compare the Note objects
     */
    NoteComparator(NoteParameter toCompare) {
        super();
        this.toCompare = toCompare;
    }

    /**
     * Compares two Note objects based on the specified NoteParameter.
     *
     * @param o1 the first Note object to compare
     * @param o2 the second Note object to compare
     * @return an integer value indicating the comparison result
     */
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
