package com.company;

import java.io.*;

public class Logger {

    public static void configure() throws IOException {
        boolean isFileExists = false;
        FileReader fileReader = new FileReader("src/com/company/resources/individual-tax-numbers.txt");
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String firstLineOfFile = bufferedReader.readLine();
            if (firstLineOfFile != null ||
                    (firstLineOfFile.length() != 0 && bufferedReader.readLine() != null)) {
                isFileExists = true;
            }
        }
        fileReader.close();
        if (isFileExists) {
            clearFile();
        }
    }

    public static void clearFile() throws IOException {
        FileWriter fileWriter = new FileWriter("src/com/company/resources/individual-tax-numbers.txt", false);
        PrintWriter printWriter = new PrintWriter(fileWriter, false);
        printWriter.flush();
        printWriter.close();
        fileWriter.close();
    }

    public static void log(String message) throws IOException {
        PrintWriter outputFile = new PrintWriter(new FileWriter("src/com/company/resources/individual-tax-numbers.txt", true), true);
        outputFile.write(message);
        outputFile.close();
    }
}