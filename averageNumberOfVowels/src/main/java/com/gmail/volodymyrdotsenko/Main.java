package com.gmail.volodymyrdotsenko;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads all words from text file and determines the average number of vowels per word grouped by:
 * set of vowels present in a word and length of the word
 * Result will be written to the output file (OUTPUT.TXT)
 * <p>
 * Created by Volodymyr Dotsenko on 31.05.16.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("Start task to calculate average number of vowels...");

        if (args.length == 0) {
            System.out.println("Error: You must set full path to file!");

            System.exit(0);
        }

        String fileName = args[0];

        try {
            System.out.println("Reading file " + fileName);
            String content = readFileToString(fileName);

            System.out.println("Obtained " + content.length() + " characters");

            new WordProcessor(content).process()
                    .print("OUTPUT.TXT");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static String readFileToString(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        }
    }
}
