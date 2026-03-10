package src;

import java.util.*;

public class Q1 {
    private HashMap<String, Integer> users = new HashMap<>();
    private HashMap<String, Integer> attempts = new HashMap<>();
    private int nextId = 1;

    public boolean checkAvailability(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !users.containsKey(username);
    }

    public void registerUser(String username) {
        if (!users.containsKey(username)) {
            users.put(username, nextId++);
        }
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        int suffix = 1;
        while (suggestions.size() < 3) {
            String candidate = username + suffix;
            if (!users.containsKey(candidate)) {
                suggestions.add(candidate);
            }
            suffix++;
        }
        return suggestions;
    }

    public String getMostAttempted() {
        String mostPopular = null;
        int max = -1;
        for (Map.Entry<String, Integer> entry : attempts.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                mostPopular = entry.getKey();
            }
        }
        return mostPopular;
    }

    public static void main(String[] args) {
        Q1 sys = new Q1();

        sys.registerUser("john_doe");

        System.out.println("john_doe available: " + sys.checkAvailability("john_doe"));
        System.out.println("jane_smith available: " + sys.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe: " + sys.suggestAlternatives("john_doe"));

        sys.checkAvailability("admin");
        sys.checkAvailability("admin");
        System.out.println("Most attempted: " + sys.getMostAttempted());
    }
}
