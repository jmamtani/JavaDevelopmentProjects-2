import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;

/**
 * This Java program counts word occurrences in a user-specified text (.txt)
 * input file. It outputs a single well-formed HTML (.html) document displaying
 * the name of the file in the heading, followed by a tag cloud listing the
 * words in lexicographic (alphabetical) order and their corresponding font
 * sizes.
 *
 * @author Jatin Mamtani
 */
public final class TagCloudGenerator {

    /**
     * ...
     */
    private TagCloudGenerator() {
    }

    /**
     * Sorts {@code String} keys of {@code Map.Pair<String, Integer>} pairs in
     * lexicographic (alphabetic) order.
     *
     */
    public static class StringLT
            implements Comparator<Map.Pair<String, Integer>> {
        /**
         * ...
         */
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o1.key().compareTo(o2.key());
        }

    }

    /**
     * Sorts {@code Integer} values of {@code Map.Pair<String, Integer>} pairs
     * in decreasing order.
     *
     */
    public static class IntegerLT
            implements Comparator<Map.Pair<String, Integer>> {
        /**
         * ...
         */
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o2.value().compareTo(o1.value());
        }

    }

    /**
     * Gets all words from .txt file and makes a map of each word used and how
     * many times it occurs in the file.
     *
     * @param in
     * @param
     * @return wordCount: Map of (Word in txt File -> Number of time word
     *         occurs)
     *
     * @requires in.is_open
     */
    public static Map<String, Integer> wordScrape(SimpleReader in,
            Set<Character> separators) {
        Map<String, Integer> wordCount = new Map1L<String, Integer>();
        while (!in.atEOS()) {
            String line = in.nextLine();
            Queue<String> wordsInLine = nextWordsInLine(line, separators);
            for (String word : wordsInLine) {
                String lowerCase = word.toLowerCase();
                if (wordCount.hasKey(lowerCase)) {
                    wordCount.replaceValue(lowerCase,
                            wordCount.value(lowerCase) + 1);
                } else {
                    wordCount.add(lowerCase, 1);
                }
            }
        }
        return wordCount;
    }

    /**
     * Makes a sequence of each individual word in {@code} line.
     *
     * @param line
     * @return words: Queue of all Strings in the line sequence.
     */
    public static Queue<String> nextWordsInLine(String line,
            Set<Character> separators) {
        Queue<String> words = new Queue1L<String>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (!separators.contains(c)) {
                word.append(c);
            } else {
                if (!word.isEmpty()) {
                    words.enqueue(word.toString());
                    word = new StringBuilder();
                }
            }
        }
        if (word.length() != 0) {
            words.enqueue(word.toString());
        }

        return words;
    }

    /**
     *
     * @param m
     * @param n
     * @return alphaSort: a SortingMachine(true, StringLT, {Map.Pairs with
     *         largest N values in m})
     */
    public static SortingMachine<Map.Pair<String, Integer>> topWords(
            Map<String, Integer> m, int n) {
        Comparator<Map.Pair<String, Integer>> stringOrder = new StringLT();
        Comparator<Map.Pair<String, Integer>> integerOrder = new IntegerLT();
        SortingMachine<Map.Pair<String, Integer>> countSort = new SortingMachine1L<>(
                integerOrder);
        SortingMachine<Map.Pair<String, Integer>> alphaSort = new SortingMachine1L<>(
                stringOrder);
        for (Map.Pair<String, Integer> pair : m) {
            countSort.add(pair);
        }
        countSort.changeToExtractionMode();
        for (int i = 0; i < n && countSort.size() > 0; i++) {
            alphaSort.add(countSort.removeFirst());
        }
        return alphaSort;
    }

    /**
     * Outputs header and the title of an html file.
     */
    public static void headerAndTitle(SimpleWriter out, String title, int n) {
        out.println("<html>");
        out.println("<title>Top " + n + " words in " + title + "</title>");
        out.println("<link href=" + '"' + "tagcloud.css" + '"' + " rel=" + '"'
                + "stylesheet" + '"' + " type =" + '"' + "text/css" + '"'
                + ">");
        out.println("</head>");

        out.println("<body>");
        out.println("<h2>Top " + n + " Words in " + title + "</h2>");
        out.println("<hr>");
    }

    /**
     * Determines the fontSize using the word count occurrence.
     *
     * @param wordCount
     * @return the fontSize
     */
    public static int computeFontSize(int wordCount) {
        int fontSize = (37);
        fontSize += (wordCount - 11);
        fontSize /= 37;
        fontSize += 11;
        return fontSize;
    }

    /**
     * Main method.
     *
     * @param args
     *             the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader input = new SimpleReader1L();
        SimpleWriter output = new SimpleWriter1L();

        output.print("Enter the input text (.txt) file name: ");
        String inputFileName = input.nextLine();
        SimpleReader in = new SimpleReader1L(inputFileName);

        output.print("Enter the output HTML (.html) file name: ");
        SimpleWriter outputFileName = new SimpleWriter1L(input.nextLine());

        output.print("Enter the size of the word cloud: ");
        int n = Integer.parseInt(input.nextLine());
        input.close();

        /*
         * Scrapes all words into map. Then the entries with the largest N key
         * values of the map are put into a SortingMachine that compares them
         * alphabetically.
         */
        char[] separators = " \t\n\r,-.!?[]';:/()".toCharArray();
        Set<Character> set = new Set1L<>();
        for (char x : separators) {
            set.add(x);
        }
        Map<String, Integer> m = wordScrape(in, set);

        SortingMachine<Map.Pair<String, Integer>> topWords = topWords(m, n);

        // Constructing the final page
        headerAndTitle(outputFileName, inputFileName, n);
        outputFileName.println("<div class=\"cdiv\">");
        outputFileName.println("<p class=\"cbox\">");
        topWords.changeToExtractionMode();
        while (topWords.size() > 0) {
            Map.Pair<String, Integer> pair = topWords.removeFirst();
            String key = pair.key();
            int value = pair.value();
            outputFileName.println("<span class=\"f" + computeFontSize(value)
                    + "\"" + " style=\"cursor:default\" title=\"count: " + value
                    + "\">" + key + "</span>");
        }
        outputFileName.println("</p>");
        outputFileName.println("</div>");
        outputFileName.println("</body>");
        outputFileName.println("</html>");

        output.close();
        in.close();
        outputFileName.close();
    }

}
