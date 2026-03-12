import java.util.*;

public class week12 {

    // n-gram → set of document IDs
    private HashMap<String, Set<String>> ngramIndex;

    // documentId → total number of ngrams
    private HashMap<String, Integer> documentNgramCount;

    private int N = 5; // size of n-gram (5 words)

    public week12() {
        ngramIndex = new HashMap<>();
        documentNgramCount = new HashMap<>();
    }

    // Break text into words
    private List<String> tokenize(String text) {
        return Arrays.asList(text.toLowerCase().split("\\s+"));
    }

    // Generate n-grams
    private List<String> generateNGrams(List<String> words) {

        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.size() - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words.get(i + j)).append(" ");
            }

            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    // Add a document to database
    public void addDocument(String documentId, String content) {

        List<String> words = tokenize(content);
        List<String> ngrams = generateNGrams(words);

        documentNgramCount.put(documentId, ngrams.size());

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());

            ngramIndex.get(gram).add(documentId);
        }
    }

    // Analyze new document for plagiarism
    public void analyzeDocument(String documentId, String content) {

        List<String> words = tokenize(content);
        List<String> ngrams = generateNGrams(words);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String doc : ngramIndex.get(gram)) {

                    matchCount.put(doc, matchCount.getOrDefault(doc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            int total = documentNgramCount.get(doc);

            double similarity = (matches * 100.0) / total;

            System.out.println("Found " + matches +
                    " matching n-grams with \"" + doc + "\"");

            System.out.println("Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 50) {
                System.out.println("PLAGIARISM DETECTED\n");
            } else if (similarity > 10) {
                System.out.println("Suspicious similarity\n");
            }
        }
    }

    public static void main(String[] args) {

         week12 detector = new week12();

        String essay1 = "Artificial intelligence is transforming the world "
                + "by enabling machines to learn from data and improve over time";

        String essay2 = "Artificial intelligence is transforming the world "
                + "by enabling machines to learn from data and improve quickly";

        String essay3 = "Climate change is a serious global challenge "
                + "affecting ecosystems and human life";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_050.txt", essay3);

        String newEssay = "Artificial intelligence is transforming the world "
                + "by enabling machines to learn from data and improve over time";

        detector.analyzeDocument("essay_123.txt", newEssay);
    }
}