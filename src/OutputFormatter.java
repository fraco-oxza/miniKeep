import java.util.*;

/**
 * The OutputFormatter class provides methods for formatting and displaying output in a specific format.
 * It includes methods for showing menus, prompts, success messages, error messages, and formatting lists of objects.
 */
public class OutputFormatter {
    public final int lineLength;
    public final int padding;

    /**
     * Constructs an OutputFormatter with the specified line length and padding.
     *
     * @param lineLength the maximum length of each line in the formatted output.
     * @param padding    the amount of padding to add to each line of the formatted output.
     */
    public OutputFormatter(int lineLength, int padding) {
        this.lineLength = lineLength;
        this.padding = padding;
    }

    /**
     * Displays a menu with the provided options.
     *
     * @param options the list of options to be displayed in the menu.
     */
    public static void showMenu(String... options) {
        for (String entry : options) {
            System.out.println("║ " + entry);
        }
    }

    /**
     * Displays a prompt message to the user.
     *
     * @param message the message to be displayed as the prompt.
     */
    public static void showPrompt(String message) {
        System.out.print("?? " + message + ": ");
    }

    /**
     * Displays a prompt message to the user and adds a new line.
     *
     * @param message the message to be displayed as the prompt.
     */
    public static void showPromptLn(String message) {
        showPrompt(message);
        System.out.println();
    }

    /**
     * Displays a success message to the user.
     *
     * @param message the success message to be displayed.
     * @param args    optional arguments to be formatted into the message.
     */
    public static void showSuccess(String message, Object... args) {
        System.out.println();
        System.out.println("## OK -> " + String.format(message, args));
        System.out.println();
    }


    /**
     * Displays an error message to the user.
     *
     * @param message the error message to be displayed.
     * @param args    optional arguments to be formatted into the message.
     */
    public static void showError(String message, Object... args) {
        System.out.println();
        System.out.println("!! ERROR ->" + String.format(message, args));
        System.out.println();
    }

    /**
     * Formats a list of tags into a single string.
     *
     * @param tags the list of tags to be formatted.
     * @return a formatted string containing the tags.
     */
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

    /**
     * Adjusts a line of text to fit within a maximum width by inserting line breaks at appropriate positions.
     *
     * @param text     the input text to be adjusted.
     * @param maxWidth the maximum width of the line.
     * @return the adjusted text with line breaks.
     */
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

    /**
     * Repeats a given pattern a specified number of times.
     *
     * @param pattern the pattern to be repeated.
     * @param nTimes  the number of times to repeat the pattern.
     * @return the resulting string after repeating the pattern.
     */
    public static String repeat(String pattern, int nTimes) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < nTimes; i++) {
            output.append(pattern);
        }
        return output.toString();
    }

    /**
     * Displays a text with a left border.
     *
     * @param text the text to display.
     */
    public static void showWithBorderLeft(String text) {
        System.out.println("║ " + text);
    }

    /**
     * Displays a single note.
     *
     * @param note the note to display.
     */
    public void showNote(Note note) {
        System.out.println(encloseIterable(Collections.singletonList(note)));
    }

    /**
     * Displays a single user.
     *
     * @param user the user to display.
     */
    public void showUser(User user) {
        System.out.println(encloseIterable(Collections.singletonList(user)));
    }

    /**
     * Displays a summary of the notes.
     *
     * @param notes the list of notes to display.
     */
    public void showShortNotes(List<Note> notes) {
        ArrayList<String> notesHeaders = new ArrayList<>();

        for (int i = 0; i < notes.size(); i++) {
            notesHeaders.add((i + 1) + ". " + notes.get(i).getHeader());
        }

        System.out.println(encloseIterable(notesHeaders));
    }

    /**
     * Displays the notes categorized by priority.
     *
     * @param notes the list of notes to display.
     */
    public void showNotesByPriority(List<Note> notes) {
        HashMap<Priority, List<Note>> notesCluster = new HashMap<>();

        // Initialize clusters for each priority level
        notesCluster.put(Priority.Low, new ArrayList<Note>());
        notesCluster.put(Priority.Normal, new ArrayList<Note>());
        notesCluster.put(Priority.High, new ArrayList<Note>());
        notesCluster.put(Priority.Critical, new ArrayList<Note>());

        // Categorize notes into respective clusters based on priority
        for (Note note : notes) {
            notesCluster.get(note.getPriority()).add(note);
        }

        // Display notes for each priority level
        showEncloseHeader("Prioridad Baja");
        showNotes(notesCluster.get(Priority.Low));

        showEncloseHeader("Prioridad Normal");
        showNotes(notesCluster.get(Priority.Normal));

        showEncloseHeader("Prioridad Alta");
        showNotes(notesCluster.get(Priority.High));

        showEncloseHeader("Prioridad Critica");
        showNotes(notesCluster.get(Priority.Critical));
    }

    /**
     * Displays the notes with the specified priority.
     *
     * @param notes    the list of notes to filter.
     * @param priority the priority to filter by.
     */
    public void showNotesWithPriority(List<Note> notes, Priority priority) {
        ArrayList<Note> filteredNotes = new ArrayList<>();

        for (Note note : notes) {
            if (note.getPriority() == priority) {
                filteredNotes.add(note);
            }
        }

        showEncloseHeader("Prioridad " + priority.toString());
        showNotes(filteredNotes);
    }


    /**
     * Displays the notes grouped by color.
     *
     * @param notes the list of notes to group.
     */
    public void showNotesByColor(List<Note> notes) {
        HashMap<String, List<Note>> notesCluster = new HashMap<>();

        // Group notes by color
        for (Note note : notes) {
            if (notesCluster.containsKey(note.getColor())) {
                notesCluster.get(note.getColor()).add(note);
            } else {
                notesCluster.put(note.getColor(), new ArrayList<>(Collections.singletonList(note)));
            }
        }

        // Display notes for each color group
        for (Map.Entry<String, List<Note>> pair : notesCluster.entrySet()) {

            if (pair.getKey().equals("")) showEncloseHeader("Sin color");
            else showEncloseHeader(pair.getKey());

            showNotes(pair.getValue());
        }
    }

    /**
     * Displays the notes grouped by tag.
     *
     * @param userNotes the list of user notes.
     */
    public void showNotesByTag(List<Note> userNotes) {
        HashMap<String, List<Note>> tagNotesMap = new HashMap<>();

        // Group notes by tag
        for (Note note : userNotes) {
            for (String tag : note.getTags()) {
                if (tagNotesMap.containsKey(tag)) {
                    tagNotesMap.get(tag).add(note);
                } else {
                    tagNotesMap.put(tag, new ArrayList<>(Collections.singletonList(note)));
                }
            }
        }

        // Display notes for each tag group
        for (Map.Entry<String, List<Note>> entry : tagNotesMap.entrySet()) {
            if (entry.getKey().equals("")) showEncloseHeader("Sin Tag");
            else showEncloseHeader(entry.getKey());

            showNotes(entry.getValue());
        }
    }

    /**
     * Displays the provided notes.
     *
     * @param notes the iterable collection of notes to display.
     */
    public void showNotes(Iterable<Note> notes) {
        System.out.println(encloseIterable(notes));
    }

    /**
     * Displays the provided text as an enclosed header.
     * <p>
     * The method constructs a formatted output using ASCII characters
     * to create a visually appealing header. The text is adjusted to fit
     * within the specified line length and padding. The resulting output
     * is printed to the console.
     * <p>
     * The provided text is adjusted to fit within the available space by
     * wrapping it if necessary. The adjustment takes into account the padding
     * on each side, ensuring that the header is visually centered.
     *
     * @param text the text to be displayed as the header.
     *             It will be enclosed within a border.
     */
    public void showEncloseHeader(String text) {
        StringBuilder output = new StringBuilder();

        output.append("╔");
        output.append(repeat("═", lineLength - 2));
        output.append("╗\n");

        text = adjustLine(text, lineLength - padding * 2 - 2);

        for (String line : text.split("\n")) {
            output.append("║");
            output.append(repeat(" ", padding));
            output.append(line);
            output.append(repeat(" ", lineLength - line.length() - padding - 2));
            output.append("║\n");
        }
        output.append("╚");
        output.append(repeat("═", lineLength - 2));
        output.append("╝\n");

        System.out.println(output);
    }

    /**
     * Creates an enclosed representation of the provided iterable objects.
     * <p>
     * The method constructs a formatted output using ASCII characters
     * to create an enclosed representation of the iterable objects.
     * Each object is formatted as a separate section within the enclosure.
     * <p>
     * The enclosure consists of a top line, bottom line, and divider lines
     * between each object. The top and bottom lines are formed by repeating
     * the character '━' for the full line length. The divider lines are
     * formed by repeating the character '─' for the full line length.
     * <p>
     * Each object is displayed within its own section. The section starts
     * with a side line ('┃') followed by the object's string representation.
     * The object's string representation is adjusted to fit within the
     * specified line length, taking into account the padding on each side.
     * If the object's string representation exceeds the line length, it will
     * be wrapped to the next line. Each line within the object's string
     * representation is visually centered within the section.
     * <p>
     * The resulting enclosed representation is returned as a string.
     * It can be used for displaying the iterable objects in a visually
     * organized and structured manner.
     *
     * @param objects the iterable objects to be enclosed.
     * @param <T>     the type of objects in the iterable.
     * @return a string representing the enclosed iterable.
     */
    public <T> String encloseIterable(Iterable<T> objects) {
        StringBuilder output = new StringBuilder();

        output.append("┏");
        output.append(repeat("━", lineLength - 2));
        output.append("┓\n");

        boolean isFirst = true;
        for (T object : objects) {
            if (!isFirst) {
                output.append("┠");
                output.append(repeat("─", lineLength - 2));
                output.append("┨\n");
            }

            for (String line : object.toString().split("\n")) {
                for (String formattedLine : adjustLine(line, lineLength - 2 - 2 * padding).split("\n")) {
                    output.append("┃").append(repeat(" ", padding));
                    output.append(formattedLine);
                    output.append(repeat(" ", lineLength - formattedLine.length() - padding - 2));
                    output.append("┃\n");
                }
            }

            isFirst = false;
        }

        output.append("┗");
        output.append(repeat("━", lineLength - 2));
        output.append("┛\n");

        return output.toString();
    }

    /**
     * Displays notes with a specific color.
     *
     * @param userNotes the list of user notes to filter.
     * @param color     the color to filter the notes by.
     */
    public void showNotesWithColor(List<Note> userNotes, String color) {
        ArrayList<Note> filtered = new ArrayList<>();

        for (Note note : userNotes) {
            if (note.getColor().equalsIgnoreCase(color)) {
                filtered.add(note);
            }
        }

        showEncloseHeader("Color: " + color);
        showNotes(filtered);
    }

    /**
     * Displays notes with a specific tag.
     *
     * @param userNotes the list of user notes to filter.
     * @param tag       the tag to filter the notes by.
     */
    public void showNotesWithTag(List<Note> userNotes, String tag) {
        ArrayList<Note> filtered = new ArrayList<>();

        for (Note note : userNotes) {
            for (String t : note.getTags()) {
                if (tag.equalsIgnoreCase(t)) {
                    filtered.add(note);
                    break;
                }
            }
        }

        showEncloseHeader("Tag: " + tag);
        showNotes(filtered);
    }
}
