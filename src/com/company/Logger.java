package com.company;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    public static void log(String message) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("src/com/company/resources/individual-tax-numbers.txt", false), true);
        out.write(message);
        out.close();
    }
}