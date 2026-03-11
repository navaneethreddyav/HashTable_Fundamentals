// Problem 4: Plagiarism Detection System
import java.util.*;

public class Problem4 {

    // n-gram → set of document IDs
    private HashMap<String, Set<String>> ngramIndex;

    // documentId → list of its n-grams
    private HashMap<String, List<String>> documentNgrams;

    private int N = 5; // 5-grams

    public Problem4() {
        ngramIndex = new HashMap<>();
        documentNgrams = new HashMap<>();
    }

    // Extract n-grams from text
    private List<String> extractNgrams(String text) {

        List<String> ngrams = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }

            ngrams.add(sb.toString().trim());
        }

        return ngrams;
    }

    // Add document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = extractNgrams(text);

        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }
    }

    // Analyze a document for plagiarism
    public void analyzeDocument(String docId) {

        List<String> ngrams = documentNgrams.get(docId);

        HashMap<String, Integer> matchCounts = new HashMap<>();

        for (String gram : ngrams) {

            if (ngramIndex.containsKey(gram)) {

                for (String otherDoc : ngramIndex.get(gram)) {

                    if (!otherDoc.equals(docId)) {

                        matchCounts.put(otherDoc,
                                matchCounts.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Analyzing Document: " + docId);
        System.out.println("Extracted " + ngrams.size() + " n-grams\n");

        for (String otherDoc : matchCounts.keySet()) {

            int matches = matchCounts.get(otherDoc);

            double similarity =
                    (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with " + otherDoc);

            System.out.printf("Similarity: %.2f%% ", similarity);

            if (similarity > 60) {
                System.out.println("(PLAGIARISM DETECTED)");
            }
            else if (similarity > 10) {
                System.out.println("(Suspicious)");
            }
            else {
                System.out.println("(Low similarity)");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {

        Problem4 detector = new Problem4();

        String essay1 = "machine learning is a field of artificial intelligence that allows computers to learn from data";

        String essay2 = "machine learning is a field of artificial intelligence that allows systems to learn automatically";

        String essay3 = "football is a popular sport played by many teams around the world";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_123.txt", essay1);

        detector.analyzeDocument("essay_123.txt");
    }
}