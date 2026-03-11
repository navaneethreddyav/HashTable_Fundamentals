import java.util.*;

public class Problem7 {

    // Trie Node
    class TrieNode {
        HashMap<Character, TrieNode> children;
        boolean isEndOfQuery;

        TrieNode() {
            children = new HashMap<>();
            isEndOfQuery = false;
        }
    }

    private TrieNode root;

    // query -> frequency
    private HashMap<String, Integer> frequencyMap;

    public Problem7() {
        root = new TrieNode();
        frequencyMap = new HashMap<>();
    }

    // Insert query into Trie
    public void insertQuery(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEndOfQuery = true;

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);
    }

    // DFS to collect queries
    private void collectQueries(TrieNode node, String prefix, List<String> results) {

        if (node.isEndOfQuery) {
            results.add(prefix);
        }

        for (char c : node.children.keySet()) {
            collectQueries(node.children.get(c), prefix + c, results);
        }
    }

    // Get node for prefix
    private TrieNode getPrefixNode(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return null;
            }

            node = node.children.get(c);
        }

        return node;
    }

    // Search autocomplete suggestions
    public List<String> search(String prefix) {

        TrieNode node = getPrefixNode(prefix);

        List<String> results = new ArrayList<>();

        if (node == null) {
            return results;
        }

        collectQueries(node, prefix, results);

        // Sort by frequency
        results.sort((a, b) -> frequencyMap.get(b) - frequencyMap.get(a));

        if (results.size() > 10) {
            return results.subList(0, 10);
        }

        return results;
    }

    // Update frequency for searched query
    public void updateFrequency(String query) {

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);
    }

    public static void main(String[] args) {

        Problem7 autocomplete = new Problem7();

        // Insert queries
        autocomplete.insertQuery("java tutorial");
        autocomplete.insertQuery("javascript");
        autocomplete.insertQuery("java download");
        autocomplete.insertQuery("java 21 features");
        autocomplete.insertQuery("java interview questions");

        // Update frequency
        autocomplete.updateFrequency("java 21 features");
        autocomplete.updateFrequency("java 21 features");

        // Search prefix
        List<String> suggestions = autocomplete.search("jav");

        System.out.println("Autocomplete results:");

        int rank = 1;

        for (String s : suggestions) {
            System.out.println(rank + ". " + s +
                    " (" + autocomplete.frequencyMap.get(s) + " searches)");
            rank++;
        }
    }
}