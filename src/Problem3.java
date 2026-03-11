import java.util.*;

public class Problem3 {

    // DNS Entry class
    class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, int ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private HashMap<String, DNSEntry> cache;
    private LinkedList<String> lruList;
    private int capacity;

    private int hits = 0;
    private int misses = 0;

    public Problem3(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        lruList = new LinkedList<>();
    }

    // Simulate upstream DNS lookup
    private String queryUpstreamDNS(String domain) {

        HashMap<String, String> dnsDatabase = new HashMap<>();
        dnsDatabase.put("google.com", "172.217.14.206");
        dnsDatabase.put("youtube.com", "142.250.190.14");
        dnsDatabase.put("amazon.com", "176.32.103.205");

        return dnsDatabase.getOrDefault(domain, "0.0.0.0");
    }

    // Resolve domain name
    public String resolve(String domain) {

        // Cache HIT case
        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;

                // update LRU
                lruList.remove(domain);
                lruList.addFirst(domain);

                return "Cache HIT → " + entry.ipAddress;
            }
            else {
                cache.remove(domain);
                lruList.remove(domain);
                System.out.println("Cache EXPIRED for " + domain);
            }
        }

        // Cache MISS
        misses++;

        String ip = queryUpstreamDNS(domain);
        System.out.println("Cache MISS → Querying upstream DNS");

        addToCache(domain, ip, 10); // TTL = 10 seconds

        return ip;
    }

    // Add entry to cache
    private void addToCache(String domain, String ip, int ttl) {

        if (cache.size() >= capacity) {
            String leastUsed = lruList.removeLast();
            cache.remove(leastUsed);
        }

        DNSEntry entry = new DNSEntry(domain, ip, ttl);

        cache.put(domain, entry);
        lruList.addFirst(domain);
    }

    // Remove expired entries
    public void cleanExpiredEntries() {

        Iterator<Map.Entry<String, DNSEntry>> iterator = cache.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, DNSEntry> item = iterator.next();

            if (item.getValue().isExpired()) {
                lruList.remove(item.getKey());
                iterator.remove();
            }
        }
    }

    // Cache statistics
    public void getCacheStats() {

        int total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        Problem3 dnsCache = new Problem3(3);

        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("google.com"));
        System.out.println(dnsCache.resolve("youtube.com"));

        Thread.sleep(11000); // wait for TTL to expire

        System.out.println(dnsCache.resolve("google.com"));

        dnsCache.getCacheStats();
    }
}