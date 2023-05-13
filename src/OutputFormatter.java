import java.util.*;

public class OutputFormatter {
    public static final int lineLength = 50;
    public static final int padding = 4;
    public static final int leftMargin = 0;

    public static void showNote(Note note) {
        System.out.println(encloseIterable(Collections.singletonList(note)));
    }

    public static void showShortNotes(List<Note> notes) {
        ArrayList<String> notesHeaders = new ArrayList<>();

        for (int i = 0; i < notes.size(); i++) {
            notesHeaders.add((i + 1) + ". " + notes.get(i).getHeader());
        }

        System.out.println(encloseIterable(notesHeaders));
    }

    public static void showNotesByPriority(List<Note> notes) {
        HashMap<Priority, List<Note>> notesCluster = new HashMap<>();

        notesCluster.put(Priority.Low, new ArrayList<Note>());
        notesCluster.put(Priority.Normal, new ArrayList<Note>());
        notesCluster.put(Priority.High, new ArrayList<Note>());
        notesCluster.put(Priority.Critical, new ArrayList<Note>());

        for (Note note : notes) {
            notesCluster.get(note.getPriority()).add(note);
        }

        OutputFormatter.showEncloseHeader("Prioridad Baja");
        OutputFormatter.showNotes(notesCluster.get(Priority.Low));

        OutputFormatter.showEncloseHeader("Prioridad Normal");
        OutputFormatter.showNotes(notesCluster.get(Priority.Normal));

        OutputFormatter.showEncloseHeader("Prioridad Alta");
        OutputFormatter.showNotes(notesCluster.get(Priority.High));

        OutputFormatter.showEncloseHeader("Prioridad Critica");
        OutputFormatter.showNotes(notesCluster.get(Priority.Critical));
    }

    public static void showNotesByColor(List<Note> notes) {
        HashMap<String, List<Note>> notesCluster = new HashMap<>();

        for (Note note : notes) {
            if (notesCluster.containsKey(note.getColor())) {
                notesCluster.get(note.getColor()).add(note);
            } else {
                notesCluster.put(note.getColor(), new ArrayList<>(Collections.singletonList(note)));
            }
        }

        for (Map.Entry<String, List<Note>> pair : notesCluster.entrySet()) {

            if (pair.getKey().equals("")) OutputFormatter.showEncloseHeader("Sin color");
            else OutputFormatter.showEncloseHeader(pair.getKey());

            OutputFormatter.showNotes(pair.getValue());
        }
    }

    public static void showNotesByTag(List<Note> userNotes) {
        HashMap<String, List<Note>> tagNotesMap = new HashMap<>();

        for (Note note : userNotes) {
            for (String tag : note.getTags()) {
                if (tagNotesMap.containsKey(tag)) {
                    tagNotesMap.get(tag).add(note);
                } else {
                    tagNotesMap.put(tag, new ArrayList<>(Collections.singletonList(note)));
                }
            }
        }

        for (Map.Entry<String, List<Note>> entry : tagNotesMap.entrySet()) {
            if (entry.getKey().equals("")) OutputFormatter.showEncloseHeader("Sin Tag");
            else OutputFormatter.showEncloseHeader(entry.getKey());

            OutputFormatter.showNotes(entry.getValue());
        }
    }

    public static void showNotes(Iterable<Note> notes) {
        System.out.println(encloseIterable(notes));
    }

    public static void showMenu(String... options) {
        for (String entry : options) {
            System.out.println(repeat(" ", leftMargin) + "║ " + entry);
        }
    }

    public static void showPrompt(String message) {
        System.out.print(repeat(" ", leftMargin) + "?? " + message + ": ");
    }

    public static void showSuccess(String message, Object... args) {
        System.out.println();
        System.out.println(repeat(" ", leftMargin) + "## OK -> " + String.format(message, args));
        System.out.println();
    }

    public static void showError(String message, Object... args) {
        System.out.println();
        System.out.println(repeat(" ", leftMargin) + "!! ERROR ->" + String.format(message, args));
        System.out.println();
    }

    public static String formatTags(List<String> tags) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < tags.size(); i++) {
            output.append(tags.get(i));
            if (i != tags.size() - 1) {
                output.append(", ");
            }
        }
        return output.toString();
    }

    public static String adjustLine(String text, int maxWidth) {
        StringBuilder output = new StringBuilder();

        while (text.length() > maxWidth) {
            int last = text.substring(0, maxWidth).lastIndexOf(" ");
            output.append(text, 0, last);
            output.append("\n");
            text = text.substring(last + 1);
        }
        output.append(text);

        return output.toString();
    }

    public static String repeat(String pattern, int nTimes) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < nTimes; i++) {
            output.append(pattern);
        }
        return output.toString();
    }

    public static void showEncloseHeader(String text) {
        StringBuilder output = new StringBuilder();

        output.append(repeat(" ", leftMargin)).append("╔");
        output.append(repeat("═", lineLength - 2));
        output.append("╗\n");

        text = adjustLine(text, lineLength - padding * 2 - 2);

        for (String line : text.split("\n")) {
            output.append(repeat(" ", leftMargin)).append("║");
            output.append(repeat(" ", padding));
            output.append(line);
            output.append(repeat(" ", lineLength - line.length() - padding - 2));
            output.append("║\n");
        }
        output.append(repeat(" ", leftMargin)).append("╚");
        output.append(repeat("═", lineLength - 2));
        output.append("╝\n");

        System.out.println(output);
    }

    public static <T> String encloseIterable(Iterable<T> objects) {
        StringBuilder output = new StringBuilder();

        output.append(repeat(" ", leftMargin)).append("┏");
        output.append(repeat("━", lineLength - 2));
        output.append("┓\n");

        boolean isFirst = true;
        for (T object : objects) {
            if (!isFirst) {
                output.append(repeat(" ", leftMargin)).append("┠");
                output.append(repeat("─", lineLength - 2));
                output.append("┨\n");
            }

            for (String line : object.toString().split("\n")) {
                for (String formattedLine : adjustLine(line, lineLength - 2 - 2 * padding).split("\n")) {
                    output.append(repeat(" ", leftMargin)).append("┃").append(repeat(" ", padding));
                    output.append(formattedLine);
                    output.append(repeat(" ", lineLength - formattedLine.length() - padding - 2));
                    output.append("┃\n");
                }
            }

            isFirst = false;
        }

        output.append(repeat(" ", leftMargin)).append("┗");
        output.append(repeat("━", lineLength - 2));
        output.append("┛\n");

        return output.toString();
    }
}
