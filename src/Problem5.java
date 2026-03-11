import java.util.*;

public class Problem5 {

    // pageUrl -> visit count
    private HashMap<String, Integer> pageViews;

    // pageUrl -> unique visitors
    private HashMap<String, HashSet<String>> uniqueVisitors;

    // source -> visit count
    private HashMap<String, Integer> trafficSources;

    public Problem5() {
        pageViews = new HashMap<>();
        uniqueVisitors = new HashMap<>();
        trafficSources = new HashMap<>();
    }

    // Process incoming event
    public void processEvent(String url, String userId, String source) {

        // Update page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Update traffic sources
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Get Top 10 pages
    private List<Map.Entry<String, Integer>> getTopPages() {

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        return list.subList(0, Math.min(10, list.size()));
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("\n===== REAL-TIME DASHBOARD =====\n");

        System.out.println("Top Pages:");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;

        for (Map.Entry<String, Integer> entry : topPages) {

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + views + " views (" + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {

            System.out.println(entry.getKey() + " → " + entry.getValue() + " visits");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Problem5 analytics = new Problem5();

        // Simulate page events
        analytics.processEvent("/article/breaking-news", "user_123", "google");
        analytics.processEvent("/article/breaking-news", "user_456", "facebook");
        analytics.processEvent("/sports/championship", "user_789", "google");
        analytics.processEvent("/sports/championship", "user_999", "direct");
        analytics.processEvent("/sports/championship", "user_123", "google");
        analytics.processEvent("/tech/ai-future", "user_555", "twitter");

        // Simulate dashboard refresh every 5 seconds
        analytics.getDashboard();
    }
}