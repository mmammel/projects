import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MarkovSentenceGenerator {

    private static final String START = "__START__";
    private static final String END = "__END__";
    private Map<String, List<String>> wordMap = new HashMap<>();
    private Random random = new Random();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java MarkovSentenceGenerator <filename>");
            return;
        }

        MarkovSentenceGenerator generator = new MarkovSentenceGenerator();
        generator.buildModel(args[0]);

        for (int i = 0; i < 5; i++) {
            String sentence = generator.generateSentence();
            System.out.println(sentence);
        }
    }

    public void buildModel(String filename) throws IOException {
        String text = Files.readString(Path.of(filename));
        text = text.replaceAll("[^a-zA-Z0-9.?!' ]+", " ");
        String[] tokens = text.split("\\s+");

        String prev = START;
        for (String word : tokens) {
            if (word.endsWith(".") || word.endsWith("!") || word.endsWith("?")) {
                String cleaned = word.replaceAll("[.!?]", "");
                addWord(prev, cleaned);
                addWord(cleaned, END);
                prev = START;
            } else {
                addWord(prev, word);
                prev = word;
            }
        }
    }

    private void addWord(String from, String to) {
        wordMap.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    public String generateSentence() {
        List<String> sentence = new ArrayList<>();
        String current = START;

        while (true) {
            List<String> nextWords = wordMap.getOrDefault(current, List.of(END));
            String next = nextWords.get(random.nextInt(nextWords.size()));
            if (next.equals(END)) break;
            sentence.add(next);
            current = next;
        }

        return String.join(" ", sentence) + ".";
    }
}
