import java.util.*;

public class Problem1 {

    // username -> userId
    private HashMap<String, Integer> usernameMap;

    // username -> attempt frequency
    private HashMap<String, Integer> attemptFrequency;

    public Problem1() {
        usernameMap = new HashMap<>();
        attemptFrequency = new HashMap<>();
    }

    // Check if username is available
    public boolean checkAvailability(String username) {

        // Track attempt frequency
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    // Register username
    public boolean registerUsername(String username, int userId) {

        if (usernameMap.containsKey(username)) {
            return false;
        }

        usernameMap.put(username, userId);
        return true;
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        // Append numbers
        for (int i = 1; i <= 5; i++) {
            String newName = username + i;

            if (!usernameMap.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        // Replace "_" with "."
        if (username.contains("_")) {
            String alt = username.replace("_", ".");

            if (!usernameMap.containsKey(alt)) {
                suggestions.add(alt);
            }
        }

        // Add prefix
        String prefix = "the_" + username;
        if (!usernameMap.containsKey(prefix)) {
            suggestions.add(prefix);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String mostAttempted = null;
        int maxAttempts = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > maxAttempts) {
                maxAttempts = entry.getValue();
                mostAttempted = entry.getKey();
            }
        }

        return mostAttempted + " (" + maxAttempts + " attempts)";
    }

    public static void main(String[] args) {

        Problem1 checker = new Problem1();

        // Register some usernames
        checker.registerUsername("john_doe", 101);
        checker.registerUsername("admin", 102);
        checker.registerUsername("alex99", 103);

        // Check availability
        System.out.println("john_doe available? " +
                checker.checkAvailability("john_doe"));

        System.out.println("jane_smith available? " +
                checker.checkAvailability("jane_smith"));

        // Suggest alternatives
        System.out.println("\nSuggestions for john_doe:");
        List<String> suggestions = checker.suggestAlternatives("john_doe");

        for (String s : suggestions) {
            System.out.println(s);
        }

        // Simulate attempts
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");

        checker.checkAvailability("john_doe");
        checker.checkAvailability("john_doe");

        // Most attempted username
        System.out.println("\nMost attempted username:");
        System.out.println(checker.getMostAttempted());
    }
}