import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * This Java program counts word occurrences in a user-specified text (.txt)
 * input file. It outputs a single well-formed HTML (.html) document displaying
 * the name of the file in the heading, followed by a tag cloud listing the
 * words in lexicographic (alphabetical) order and their corresponding font
 * sizes.
 *
 * @author Jatin Mamtani
 */
public final class TagCloud {

    /**
     * ...
     */
    final int maxFontSize = 37;
    /**
     * ...
     */
    final int minFontSize = 11;

    /**
     * No argument constructor.
     */
    private TagCloud() {
        // no code needed here...
    }

    /**
     * Sorts {@code String} keys of {@code Map.Entry<String, Integer>} pairs in
     * lexicographic (alphabetic) order.
     *
     */
    public static class StringLT
            implements Comparator<Map.Entry<String, Integer>> {
        /**
         * ...
         */
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }

    }

    /**
     * Sorts {@code Integer} values of {@code Map.Entry<String, Integer>} pairs
     * in decreasing order.
     *
     */
    public static class IntegerLT
            implements Comparator<Map.Entry<String, Integer>> {
        /**
         * ...
         */
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }

    }

    /**
     * Gets all words from a user-specified text (.txt) file and creates a
     * {@cod Map<String, Integer> map of each word used and how many times it
     * occurs in the file.
     *
     * @param in
     * @param
     * @return wordCount: Map of (Word in txt File -> Number of time word
     *         occurs)
     *
     * @requires in.is_open
     */
    public static Map<String, Integer> wordScrape(BufferedReader in,
            Set<Character> separators) {
        Map<String, Integer> wordsAndCount = new HashMap<>();
        String line = null;
        try {
            line = in.readLine();
        } catch (IOException e) {
            System.out.println("Error reading first line");
        }
        int lineNumber = 1;
        while (line != null) {
            List<String> lineOfWords = nextWordsInLine(line, separators);
            for (String s : lineOfWords) {
                Integer v = wordsAndCount.put(s, 1);
                if (v != null) {
                    wordsAndCount.put(s, v + 1);
                }
            }
            try {
                line = in.readLine();
            } catch (IOException e) {
                System.out.println("Error reading line" + lineNumber);
            }
            lineNumber++;
        }
        return wordsAndCount;
    }

    /**
     * Sorts a Queue in alphabetical order
     *
     * @param map
     * @param tagCloudSize
     * @return
     */
    public static Queue<Map.Entry<String, Integer>> topWords(
            Map<String, Integer> map, int tagCloudSize) {
        Comparator<Map.Entry<String, Integer>> keyCompare = new StringLT();
        Comparator<Map.Entry<String, Integer>> valCompare = new IntegerLT();
        Queue<Map.Entry<String, Integer>> topCount = new PriorityQueue<>(
                valCompare);
        topCount.addAll(map.entrySet());
        Queue<Map.Entry<String, Integer>> topWords = new PriorityQueue<>(
                tagCloudSize, keyCompare);
        for (int i = 0; i < tagCloudSize && !topCount.isEmpty(); i++) {
            topWords.add(topCount.remove());
        }
        return topWords;
    }

    /**
     * Outputs the header and the title of the output HTML (.html) file.
     *
     * @param fileWriter
     * @param inputFileName
     * @param tagCloudSize
     */
    public static void outputHeaderAndTitle(PrintWriter fileWriter,
            String inputFileName, int tagCloudSize) {
        fileWriter.println("<html>");
        fileWriter.println("<title>Top " + tagCloudSize + " words in "
                + inputFileName + "</title>");
        fileWriter.println("<link href=" + '"' + "tagcloud.css" + '"' + " rel="
                + '"' + "stylesheet" + '"' + " type =" + '"' + "text/css" + '"'
                + ">");
        fileWriter.println("</head>");
        fileWriter.println("<body>");
        fileWriter.println("<h2>Top " + tagCloudSize + " Words in "
                + inputFileName + "</h2>");
        fileWriter.println("<hr>");
    }

    /**
     * Determines the {@code Integer} font size given the {@code Integer} tag
     * cloud size.
     *
     * @param tagCloudSize
     * @return fontSize
     */
    public static int computeFontSize(int tagCloudSize) {
        int fontSize = (37);
        fontSize += (tagCloudSize - 11);
        fontSize /= 37;
        fontSize += 11;
        return fontSize;
    }

    /**
     * Creates a {@code List} list of all words in a given {@code String} line.
     *
     * @param line
     * @param set
     * @return list containing all words.
     */
    public static List<String> nextWordsInLine(String line,
            Set<Character> set) {
        List<String> list = new LinkedList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (!set.contains(c)) {
                word.append(c);
            } else {
                if (!word.isEmpty()) {
                    list.add(word.toString());
                    word = new StringBuilder();
                }
            }
        }
        if (word.length() != 0) {
            list.add(word.toString());
        }
        return list;
    }

    /**
     * Main method.
     *
     * @param args
     *             the command line arguments; unused here
     */
    public static void main(String[] args) throws IOException {
        BufferedReader keyboard = new BufferedReader(
                new InputStreamReader(System.in));

        /*
         * Opening the input text (.txt) file in this section:
         */
        BufferedReader fileReader = null;
        boolean isValidInputFile = false;
        while (!isValidInputFile) {
            System.out.print(
                    "Enter the name of the input text (.txt) file here: ");
            String inputFileName;
            try {
                inputFileName = keyboard.readLine();
                fileReader = new BufferedReader(new FileReader(inputFileName));
                isValidInputFile = true;
            } catch (IOException e) {
                System.err.println(
                        "ERROR: cannot open the stream from the input text (.txt) file.");
            }
        }

        /*
         * Opening the output HTML (.html) file in this section:
         */
        PrintWriter fileWriter = null;
        System.out
                .print("Enter the name of the output HTML (.html) file here: ");
        String outputFileName = "";
        try {
            outputFileName = keyboard.readLine();
            fileWriter = new PrintWriter(
                    new BufferedWriter(new FileWriter(outputFileName)));
        } catch (IOException e) {
            System.err.println(
                    "ERROR: cannot open the stream to the output HTML (.html) file.");
            return;
        }

        System.out.print("Enter the size of the tag cloud here: ");
        int tagCloudSize = 0;
        boolean isValidTagCloudSize = false;
        while (!isValidTagCloudSize) {
            try {
                tagCloudSize = Integer.parseInt(keyboard.readLine());
                isValidTagCloudSize = true;
            } catch (IOException e) {
                // TODO - Return a valid error message here...
                System.err.println("ERROR");
            } catch (NumberFormatException e) {
                // TODO - Return a valid error message here...
                System.err.println("ERROR");
            }
        }

        try {
            keyboard.close();
        } catch (IOException e) {
            System.err.println("ERROR: cannot close the keyboard stream.");
        }

        char[] s = " \t\n\r,-.!?[]';:/()".toCharArray();
        Set<Character> set = new HashSet<>();
        for (char x : s) {
            set.add(x);
        }

        Map<String, Integer> map = wordScrape(fileReader, set);
        Queue<Map.Entry<String, Integer>> topWords = topWords(map,
                tagCloudSize);
        outputHeaderAndTitle(fileWriter, outputFileName, tagCloudSize);
        fileWriter.println("<div class=\"cdiv\">");
        fileWriter.println("<p class=\"cbox\">");
        while (topWords.size() > 0) {
            Map.Entry<String, Integer> pair = topWords.remove();
            String key = pair.getKey();
            int value = pair.getValue();
            int fontSize = computeFontSize(value);
            fileWriter.println("<span class=\"f" + fontSize
                    + " style=\"cursor:default\" title=\"count: " + value
                    + "\">" + key + "</span>");
        }
        fileWriter.println("</p>");
        fileWriter.println("</div>");
        fileWriter.println("</body>");
        fileWriter.println("</html>");

        /*
         * Closing the {input and output} stream(s) in this section:
         */
        try {
            fileReader.close();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println(
                    "ERROR: cannot close the {input and output} stream(s).");
        }

    }

}
