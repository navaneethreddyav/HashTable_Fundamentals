import java.util.*;

public class Problem6 {

    // Token Bucket class
    class TokenBucket {
        int tokens;
        int maxTokens;
        double refillRate; // tokens per second
        long lastRefillTime;

        TokenBucket(int maxTokens, double refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }

        // Refill tokens based on elapsed time
        void refill() {
            long now = System.currentTimeMillis();
            double seconds = (now - lastRefillTime) / 1000.0;

            int tokensToAdd = (int) (seconds * refillRate);

            if (tokensToAdd > 0) {
                tokens = Math.min(maxTokens, tokens + tokensToAdd);
                lastRefillTime = now;
            }
        }

        // Consume token
        boolean allowRequest() {
            refill();

            if (tokens > 0) {
                tokens--;
                return true;
            }

            return false;
        }
    }

    // clientId -> TokenBucket
    private HashMap<String, TokenBucket> clients;

    private int MAX_REQUESTS = 1000;
    private double REFILL_RATE = 1000.0 / 3600; // tokens per second

    public Problem6() {
        clients = new HashMap<>();
    }

    // Check rate limit
    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new TokenBucket(MAX_REQUESTS, REFILL_RATE));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.tokens + " requests remaining)";
        }
        else {
            return "Denied (Rate limit exceeded)";
        }
    }

    // Rate limit status
    public void getRateLimitStatus(String clientId) {

        if (!clients.containsKey(clientId)) {
            System.out.println("Client not found");
            return;
        }

        TokenBucket bucket = clients.get(clientId);

        int used = bucket.maxTokens - bucket.tokens;

        System.out.println("Used: " + used);
        System.out.println("Limit: " + bucket.maxTokens);
        System.out.println("Remaining: " + bucket.tokens);
    }

    public static void main(String[] args) {

        Problem6 rateLimiter = new Problem6();

        String client = "abc123";

        // Simulate requests
        for (int i = 0; i < 5; i++) {
            System.out.println(rateLimiter.checkRateLimit(client));
        }

        System.out.println();

        rateLimiter.getRateLimitStatus(client);
    }
}