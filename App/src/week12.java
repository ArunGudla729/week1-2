import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class week12 {

    private int capacity;
    private LinkedHashMap<String, DNSEntry> cache;
    private int hits = 0;
    private int misses = 0;

    public week12(int capacity) {
        this.capacity = capacity;

        // accessOrder = true → enables LRU behavior
        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > week12.this.capacity;
            }
        };
    }

    // Simulate upstream DNS lookup
    private String queryUpstreamDNS(String domain) {
        // Fake IP generation for demo
        return "172.217." + new Random().nextInt(100) + "." + new Random().nextInt(255);
    }

    public String resolve(String domain) {

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                System.out.println("Cache HIT → " + entry.ipAddress);
                return entry.ipAddress;
            }

            // expired
            cache.remove(domain);
            System.out.println("Cache EXPIRED");
        }

        // cache miss
        misses++;

        String ip = queryUpstreamDNS(domain);

        // TTL = 300 seconds
        DNSEntry entry = new DNSEntry(domain, ip, 300);

        cache.put(domain, entry);

        System.out.println("Cache MISS → Query upstream → " + ip);

        return ip;
    }

    public void removeExpiredEntries() {

        Iterator<Map.Entry<String, DNSEntry>> iterator = cache.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<String, DNSEntry> entry = iterator.next();

            if (entry.getValue().isExpired()) {
                iterator.remove();
            }
        }
    }

    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) {

        week12 dnsCache = new week12(5);

        dnsCache.resolve("google.com");
        dnsCache.resolve("google.com");

        dnsCache.resolve("github.com");
        dnsCache.resolve("stackoverflow.com");

        dnsCache.getCacheStats();
    }
}