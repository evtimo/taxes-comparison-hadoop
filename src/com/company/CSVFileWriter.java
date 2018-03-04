package com.company;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVFileWriter {

    private static final char DELIMITER = ',';

    private static String adjustToCSVformat(String originalString) {
        String convertedString = originalString;
        if (convertedString.contains("\"")) {
            convertedString = convertedString.replace("\"", "\"\"");
        }
        return convertedString;
    }

    public static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DELIMITER, ' ');
    }

    public static void writeLine(Writer writer, List<String> values, char delimiters, char customQuote) throws IOException {
        boolean isElementfirst = true;
        if (delimiters == ' ') {
            delimiters = DELIMITER;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            if (!isElementfirst) {
                stringBuilder.append(delimiters);
            }
            if (customQuote != ' ') {
                stringBuilder.append(customQuote).append(adjustToCSVformat(value)).append(customQuote);
            } else {
                stringBuilder.append(adjustToCSVformat(value));
            }
            isElementfirst = false;
        }
        stringBuilder.append("\n");
        writer.append(stringBuilder.toString());
    }

}