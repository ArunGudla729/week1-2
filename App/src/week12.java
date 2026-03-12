import java.util.HashMap;

class TokenBucket {

    int maxTokens;
    int tokens;
    long lastRefillTime;
    int refillRate; // tokens added per second

    public TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // refill tokens based on elapsed time
    private void refill() {

        long now = System.currentTimeMillis();
        long elapsedSeconds = (now - lastRefillTime) / 1000;

        int tokensToAdd = (int) (elapsedSeconds * refillRate);

        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }

    // check if request allowed
    public synchronized boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    public int getRemainingTokens() {
        return tokens;
    }
}

public class week12 {

    // clientId → token bucket
    private HashMap<String, TokenBucket> clientBuckets = new HashMap<>();

    private static final int MAX_REQUESTS = 1000;
    private static final int REFILL_RATE = 1000 / 3600; // tokens per second

    public String checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId,
                new TokenBucket(MAX_REQUESTS, REFILL_RATE));

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {

            return "Allowed (" + bucket.getRemainingTokens()
                    + " requests remaining)";
        }

        return "Denied (0 requests remaining, try later)";
    }

    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = MAX_REQUESTS - bucket.getRemainingTokens();

        System.out.println("{used: " + used +
                ", limit: " + MAX_REQUESTS + "}");
    }

    public static void main(String[] args) {

        week12 limiter = new week12();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        limiter.getRateLimitStatus("abc123");
    }
}