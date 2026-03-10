import java.util.*;

public class Q4 {
    private final Map<String, Set<String>> ngramIndex = new HashMap<>();
    private final Map<String, Integer> documentSize = new HashMap<>();
    private final int N = 5;

    public void indexDocument(String docId, String content) {
        String[] words = content.toLowerCase().split("\\s+");
        Set<String> ngrams = extractNgrams(words);
        documentSize.put(docId, ngrams.size());

        for (String ngram : ngrams) {
            ngramIndex.computeIfAbsent(ngram, k -> new HashSet<>()).add(docId);
        }
    }

    private Set<String> extractNgrams(String[] words) {
        Set<String> ngrams = new HashSet<>();
        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }
        return ngrams;
    }

    public void analyzeDocument(String content) {
        String[] words = content.toLowerCase().split("\\s+");
        Set<String> queryNgrams = extractNgrams(words);
        Map<String, Integer> matchCounts = new HashMap<>();

        for (String ngram : queryNgrams) {
            if (ngramIndex.containsKey(ngram)) {
                for (String docId : ngramIndex.get(ngram)) {
                    matchCounts.put(docId, matchCounts.getOrDefault(docId, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + queryNgrams.size() + " n-grams");
        for (Map.Entry<String, Integer> entry : matchCounts.entrySet()) {
            double similarity = (entry.getValue() * 100.0) / queryNgrams.size();
            System.out.print("Found " + entry.getValue() + " matching n-grams with " + entry.getKey());
            System.out.printf(" -> Similarity: %.1f%%", similarity);

            if (similarity > 50) System.out.println(" (PLAGIARISM DETECTED)");
            else if (similarity > 10) System.out.println(" (suspicious)");
            else System.out.println(" (clear)");
        }
    }

    public static void main(String[] args) {
        Q4 detector = new Q4();

        detector.indexDocument("essay_089.txt", "the quick brown fox jumps over the lazy dog");
        detector.indexDocument("essay_092.txt", "data structures and algorithms are fundamental to computer science studies");

        String studentSubmission = "data structures and algorithms are fundamental to modern computer science";
        detector.analyzeDocument(studentSubmission);
    }
}