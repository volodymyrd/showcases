package com.gmail.volodymyrdotsenko;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Determines the average number of vowels per word grouped by:
 * set of vowels present in a word and length of the word
 * <p>
 * Created by Volodymyr Dotsenko on 31.05.16.
 */
public class WordProcessor {

    public WordProcessor(String content) {
        if (content == null || content.isEmpty())
            throw new IllegalArgumentException("Parameter content must be set");

        this.content = content;
    }

    /**
     * process
     *
     * @return WordProcessor to chain call
     */
    public WordProcessor process() {
        String[] words = content.split("[\\s\\W]+");

        System.out.println("Content parsed: " + Arrays.toString(words));

        for (String w : words) {
            wordProcess(w);
        }

        return this;
    }

    /**
     * Print result to console
     */
    public void print() {
        results.forEach((k, v) -> System.out.println(v));
    }

    /**
     * Print result to file
     *
     * @param filename
     */
    public void print(String filename) {
        try (FileWriter fw = new FileWriter(filename)) {
            results.forEach((k, v) -> {
                try {
                    fw.write(v.toString());
                    fw.write(System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println("Error write to file");
        }
    }

    static class Result {
        private int length;
        private final List<Integer> counts = new ArrayList<>();
        private final Set<String> set = new HashSet<>();

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("(")
                    .append("{")
                    .append(set.stream().map(e -> e).collect(Collectors.joining(", ")))
                    .append("}, ")
                    .append(length)
                    .append(")->")
                    .append(String.format("%.1f",
                            1.0 * counts.stream().mapToInt(Integer::intValue).sum() / counts.size()))
                    .toString();
        }
    }

    private final String content;

    private final Map<String, Result> results = new HashMap<>();

    Map<String, Result> getResults() {
        return results;
    }

    /**
     * Regex to find vowels
     */
    private Pattern pattern = Pattern.compile("[aeiou]");

    private void wordProcess(String word) {

        Matcher matcher = pattern.matcher(word);

        Result resultTmp = new Result();

        int count = 0;
        while (matcher.find()) {
            count++;
            resultTmp.set.add(matcher.group());
        }

        resultTmp.length = word.length();

        String key = getKey(resultTmp.length, resultTmp.set);

        Result result = results.get(key);

        if (result == null) {
            resultTmp.counts.add(count);
            results.put(key, resultTmp);
        } else {
            result.counts.add(count);
        }
    }

    /**
     * Calculate key of group set of vowels present in a word and length of the word
     *
     * @param length
     * @param set
     * @return key of group
     */
    private String getKey(int length, Set<String> set) {
        return String.valueOf(length + set.hashCode());
    }
}