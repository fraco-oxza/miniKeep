public class EncloseString {
    private static final int lineLength = 50;
    private static final int padding = 4;
    private static final int leftMargin = 2;

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

    public static String encloseLevel1(String text) {
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

        return output.toString();
    }

    public static <T> String encloseIterable(Iterable<T> objects) {
        StringBuilder output = new StringBuilder();

        output.append(repeat(" ", leftMargin)).append("┏");
        output.append(repeat("━", lineLength - 2));
        output.append("┓\n");

        boolean isFirst = true;
        for (T object : objects) {
            if (!isFirst) {
                output.append(repeat(" ", leftMargin)).append("┣");
                output.append(repeat("━", lineLength - 2));
                output.append("┫\n");
            }

            for (String line : object.toString().split("\n")) {
                for (String formatedLine : adjustLine(line, lineLength - 2 - 2 * padding).split("\n")) {
                    output.append(repeat(" ", leftMargin)).append("┃").append(repeat(" ", padding));
                    formatedLine = formatedLine + repeat(" ", lineLength - formatedLine.length() - padding - 2);
                    output.append(formatedLine);
                    output.append("┃\n");
                }
            }
            ;
            isFirst = false;
        }

        output.append(repeat(" ", leftMargin)).append("┗");
        output.append(repeat("━", lineLength - 2));
        output.append("┛\n");

        return output.toString();
    }
}
