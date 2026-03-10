import java.util.*;

public class Q3 {

    class DNSEntry {
        String ip;
        long expiryTime;

        DNSEntry(String ip, long ttlSeconds) {
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private final int capacity;
    private final LinkedHashMap<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;

    public Q3(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > Q3.this.capacity;
            }
        };
    }

    public String resolve(String domain) {
        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            return entry.ip;
        }

        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
        }

        misses++;
        String ip = queryUpstream(domain);
        cache.put(domain, new DNSEntry(ip, 300));
        return ip;
    }

    private String queryUpstream(String domain) {
        return "172.217.14." + (new Random().nextInt(255));
    }

    public void getCacheStats() {
        double total = hits + misses;
        double hitRate = (total == 0) ? 0 : (hits / total) * 100;
        System.out.println("Hits: " + hits + ", Misses: " + misses);
        System.out.printf("Hit Rate: %.2f%%\n", hitRate);
    }

    public void cleanExpired() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    public static void main(String[] args) throws InterruptedException {
        Q3 dns = new Q3(2);

        System.out.println("Resolving google.com: " + dns.resolve("google.com"));
        System.out.println("Resolving google.com (Cached): " + dns.resolve("google.com"));

        dns.getCacheStats();
    }
}