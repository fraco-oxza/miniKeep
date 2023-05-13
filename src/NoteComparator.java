import java.util.Comparator;

enum NoteParameter {
  Header,
  CreationDate,
}

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
      case Header:
        result = o1.getHeader().compareTo(o2.getHeader());
        break;
    }

    return result;
  }
}
