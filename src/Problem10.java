import java.util.*;

public class Problem10 {

    // Video data class (could store metadata)
    static class VideoData {
        String videoId;
        String content; // simulated content

        VideoData(String videoId, String content) {
            this.videoId = videoId;
            this.content = content;
        }
    }

    // L1 Cache: in-memory, LinkedHashMap for LRU
    private LinkedHashMap<String, VideoData> L1Cache;
    private int L1Capacity = 10000;

    // L2 Cache: simulated SSD, HashMap + access count
    private HashMap<String, VideoData> L2Cache;
    private HashMap<String, Integer> L2AccessCount;
    private int L2Capacity = 100000;
    private int L2PromoteThreshold = 3; // promote to L1 after 3 accesses

    // Simulated L3 Database
    private HashMap<String, VideoData> L3DB;

    // Statistics
    private int L1Hits = 0, L1Misses = 0;
    private int L2Hits = 0, L2Misses = 0;
    private int L3Hits = 0, L3Misses = 0;
    private double L1AvgTime = 0.5;
    private double L2AvgTime = 5;
    private double L3AvgTime = 150;

    public Problem10() {

        L1Cache = new LinkedHashMap<String, VideoData>(L1Capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1Capacity;
            }
        };

        L2Cache = new HashMap<>();
        L2AccessCount = new HashMap<>();
        L3DB = new HashMap<>();
    }

    // Add video to database
    public void addVideoToDB(String videoId, String content) {
        L3DB.put(videoId, new VideoData(videoId, content));
    }

    // Fetch video
    public VideoData getVideo(String videoId) {

        // Check L1
        if (L1Cache.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 Cache HIT (" + L1AvgTime + "ms)");
            return L1Cache.get(videoId);
        }
        L1Misses++;

        // Check L2
        if (L2Cache.containsKey(videoId)) {
            L2Hits++;
            int count = L2AccessCount.getOrDefault(videoId, 0) + 1;
            L2AccessCount.put(videoId, count);
            System.out.println("L1 Cache MISS (" + L1AvgTime + "ms)");
            System.out.println("L2 Cache HIT (" + L2AvgTime + "ms)");

            // Promote to L1 if threshold reached
            if (count >= L2PromoteThreshold) {
                L1Cache.put(videoId, L2Cache.get(videoId));
                System.out.println("Promoted to L1");
            }

            return L2Cache.get(videoId);
        }
        L2Misses++;

        // Fetch from L3
        if (L3DB.containsKey(videoId)) {
            L3Hits++;
            System.out.println("L1 Cache MISS (" + L1AvgTime + "ms)");
            System.out.println("L2 Cache MISS (" + L2AvgTime + "ms)");
            System.out.println("L3 Database HIT (" + L3AvgTime + "ms)");

            // Add to L2
            if (L2Cache.size() >= L2Capacity) {
                // simple eviction: remove random key
                String keyToRemove = L2Cache.keySet().iterator().next();
                L2Cache.remove(keyToRemove);
                L2AccessCount.remove(keyToRemove);
            }
            L2Cache.put(videoId, L3DB.get(videoId));
            L2AccessCount.put(videoId, 1);

            return L3DB.get(videoId);
        }
        L3Misses++;

        System.out.println("Video not found in any cache or DB");
        return null;
    }

    // Print statistics
    public void getStatistics() {

        int totalRequests = L1Hits + L1Misses;
        int totalRequestsAll = totalRequests + L2Misses + L3Misses;

        double overallHitRate = (double)(L1Hits + L2Hits + L3Hits) / totalRequestsAll * 100;
        double avgTime = ((L1Hits * L1AvgTime) + (L2Hits * L2AvgTime) + (L3Hits * L3AvgTime)) /
                (L1Hits + L2Hits + L3Hits);

        System.out.println("\nCache Statistics:");
        System.out.printf("L1: Hit Rate %.2f%%, Avg Time %.2fms\n", (double)L1Hits / totalRequests * 100, L1AvgTime);
        System.out.printf("L2: Hit Rate %.2f%%, Avg Time %.2fms\n", (double)L2Hits / totalRequests * 100, L2AvgTime);
        System.out.printf("L3: Hit Rate %.2f%%, Avg Time %.2fms\n", (double)L3Hits / totalRequests * 100, L3AvgTime);
        System.out.printf("Overall Hit Rate: %.2f%%, Avg Time: %.2fms\n", overallHitRate, avgTime);
    }

    public static void main(String[] args) {

        Problem10 cacheSystem = new Problem10();

        // Add videos to DB
        cacheSystem.addVideoToDB("video_123", "Movie A");
        cacheSystem.addVideoToDB("video_999", "Movie B");

        // First request
        System.out.println("Fetching video_123 (first request)");
        cacheSystem.getVideo("video_123");

        // Second request
        System.out.println("\nFetching video_123 (second request)");
        cacheSystem.getVideo("video_123");

        // Request for video not in cache
        System.out.println("\nFetching video_999");
        cacheSystem.getVideo("video_999");

        // Show statistics
        cacheSystem.getStatistics();
    }
}