import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Creates a word occurrence counter and outputs the words with occurrence in a
 * html file.
 *
 * @author Jatin Mamtani
 *
 */
public final class WordOcr {

    /**
     * Set to a private constructor to prevent being instantiated.
     */

    private WordOcr() {

    }

    /**
     * Creates the html file that stores the table of words and the number of
     * occurrences.
     *
     * @param outFile
     *            the output file
     *
     * @param wordMap
     *            the {@code Map} storing words and the occurrences as the value
     *            associated
     *
     * @param keys
     *            containing the {@code wordMap} keys
     *
     * @param inFile
     *            the text input file
     *
     *
     */

    public static void getTable(SimpleWriter outFile,
            Map<String, Integer> wordMap, Queue<String> keys,
            SimpleReader inFile) {
        assert wordMap != null : "Violation: wordMap is not null";
        assert keys != null : "Violation: keys is not null";
        assert outFile.isOpen() : "Violation: outFile is open";
        assert inFile.isOpen() : "Violation: inFile is open";

        /**
         * Generating the output tags by creating header file names.
         *
         * @param out
         *            to store the outputs in a single file
         */

        outFile.println("<html>");
        outFile.println("<head>");
        outFile.println(
                "<title> Words Counted below: " + inFile.name() + "</title>");
        outFile.println("<body>");

        outFile.println("<h2>Words occurrence: " + inFile.name() + "</h2>");
        outFile.println("<hr />");

        outFile.println("<table border = \"2\">");
        outFile.println("<tr>");
        outFile.println("<th>Words</th>");
        outFile.println("<th>Occurrence</th>");
        outFile.println("</tr>");

        while (wordMap.iterator().hasNext()) {
            Pair<String, Integer> pair = wordMap.remove(keys.dequeue());
            outFile.println("<tr>");
            outFile.println("<td>" + pair.key() + "</td>");
            outFile.println("<td>" + pair.value() + "</td>");
            outFile.println("</tr>");
        }
        outFile.println("</table>");
        outFile.println("</body>");
        outFile.println("</html>");
    }

    /**
     * using a spring comparator to traverse through the entire string.
     */

    private static class StringCmp implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    }

    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        int i = position;
        if (!separators.contains(text.charAt(position))) {
            while (i < text.length() && !separators.contains(text.charAt(i))) {
                i++;
            }
        } else {
            while (i < text.length() && separators.contains(text.charAt(i))) {
                i++;
            }
        }
        return text.substring(position, i);
    }

    public static void getSeparators(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";
        charSet.clear();
        for (int i = 0; i < str.length() - 1; i++) {
            charSet.add(str.charAt(i));
        }
    }

    public static void getWords(SimpleReader inFile,
            Map<String, Integer> wordMap) {
        assert inFile.isOpen() : "Violation of : inFile is open";
        assert wordMap != null : "Violation of: wordMap is not null";
        wordMap.clear();
        int startPos = 0;
        Set<Character> exclude = new Set1L<Character>();
        String separators = " \t,.'-`~: ";
        getSeparators(separators, exclude);
        while (!inFile.atEOS()) {
            String line = inFile.nextLine();
            startPos = 0;
            while (startPos < line.length()) {
                String nextItem = nextWordOrSeparator(line, startPos, exclude);
                if (!exclude.contains(nextItem.charAt(0))) {
                    if (!wordMap.hasKey(nextItem)) {
                        wordMap.add(nextItem, 1);
                    } else {
                        int wordCount = wordMap.value(nextItem);
                        wordCount++;
                        wordMap.replaceValue(nextItem, wordCount);
                    }
                }
                startPos += nextItem.length();
            }
        }
    }

    public static void wordSort(Map<String, Integer> wordMap,
            Comparator<String> order, Queue<String> key) {
        assert wordMap != null : "Violation of : wordMap is not null";
        assert order != null : "Violation of : order is not null";
        assert key != null : "Violation of : key is not null";

        Map<String, Integer> sortedMap = wordMap.newInstance();
        Queue<String> sortedQueue = key.newInstance();
        key.clear();

        while (wordMap.iterator().hasNext()) {

            Pair<String, Integer> tempPair = wordMap.removeAny();
            sortedMap.add(tempPair.key(), tempPair.value());
            key.enqueue(tempPair.key());
        }

        key.sort(order);

        while (key.iterator().hasNext()) {
            String tempKey = key.dequeue();
            Pair<String, Integer> orderedPair = sortedMap.remove(tempKey);
            wordMap.add(orderedPair.key(), orderedPair.value());
            sortedQueue.enqueue(tempKey);
        }
        key.transferFrom(sortedQueue);
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */

    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();

        Map<String, Integer> words = new Map1L<String, Integer>();
        Comparator<String> order = new StringCmp();
        Queue<String> keyQueue = new Queue1L<String>();

        out.println("Please enter the name of the input file: ");

        SimpleReader inFile = new SimpleReader1L("data/" + in.nextLine());

        out.println("Please enter the name of the output file: ");
        SimpleWriter outFile = new SimpleWriter1L("data/" + in.nextLine());
        getWords(inFile, words);
        wordSort(words, order, keyQueue);
        getTable(outFile, words, keyQueue, inFile);

        in.close();
        out.close();
        inFile.close();
        outFile.close();
    }
}
