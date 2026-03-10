import java.util.*;

public class Q5 {
    private final Map<String, Integer> pageViews = new HashMap<>();
    private final Map<String, Set<String>> uniqueVisitors = new HashMap<>();
    private final Map<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(String url, String userId, String source) {
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        uniqueVisitors.computeIfAbsent(url, k -> new HashSet<>()).add(userId);

        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    public void getDashboard() {
        System.out.println("\n--- Real-Time Dashboard ---");
        System.out.println("Top Pages:");

        PriorityQueue<Map.Entry<String, Integer>> topPages = new PriorityQueue<>(
                Comparator.comparingInt(Map.Entry::getValue)
        );

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {
            topPages.offer(entry);
            if (topPages.size() > 10) {
                topPages.poll();
            }
        }

        List<Map.Entry<String, Integer>> sortedTop = new ArrayList<>(topPages);
        sortedTop.sort((a, b) -> b.getValue() - a.getValue());

        int rank = 1;
        for (Map.Entry<String, Integer> entry : sortedTop) {
            String url = entry.getKey();
            int views = entry.getValue();
            int uniques = uniqueVisitors.get(url).size();
            System.out.println(rank + ". " + url + " - " + views + " views (" + uniques + " unique)");
            rank++;
        }
    }

    public static void main(String[] args) {
        Q5 dashboard = new Q5();

        dashboard.processEvent("/article/breaking-news", "user_123", "google");
        dashboard.processEvent("/article/breaking-news", "user_456", "facebook");
        dashboard.processEvent("/article/breaking-news", "user_123", "google");
        dashboard.processEvent("/sports/championship", "user_789", "direct");
        dashboard.processEvent("/sports/championship", "user_000", "google");

        dashboard.getDashboard();
    }
}