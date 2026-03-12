import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    public PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class week12 {

    // pageUrl → visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl → unique visitors
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source → count
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    // Process incoming event
    public void processEvent(PageEvent event) {

        // Update page views
        pageViews.put(event.url, pageViews.getOrDefault(event.url, 0) + 1);

        // Update unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // Update traffic sources
        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }

    // Get Top 10 pages
    private List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Map.Entry.comparingByValue());

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {

            pq.offer(entry);

            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<Map.Entry<String, Integer>> result = new ArrayList<>(pq);
        result.sort((a, b) -> b.getValue() - a.getValue());

        return result;
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("Top Pages:");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;

        for (Map.Entry<String, Integer> page : topPages) {

            int unique = uniqueVisitors.get(page.getKey()).size();

            System.out.println(rank + ". " + page.getKey()
                    + " - " + page.getValue() + " views ("
                    + unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;

        for (int count : trafficSources.values()) {
            total += count;
        }

        for (String source : trafficSources.keySet()) {

            double percent =
                    (trafficSources.get(source) * 100.0) / total;

            System.out.println(source + ": "
                    + String.format("%.1f", percent) + "%");
        }
    }

    public static void main(String[] args) {

        week12 analytics = new week12();

        // Simulated events
        analytics.processEvent(new PageEvent("/article/breaking-news",
                "user_123", "google"));

        analytics.processEvent(new PageEvent("/article/breaking-news",
                "user_456", "facebook"));

        analytics.processEvent(new PageEvent("/sports/championship",
                "user_789", "google"));

        analytics.processEvent(new PageEvent("/sports/championship",
                "user_101", "direct"));

        analytics.processEvent(new PageEvent("/sports/championship",
                "user_789", "google"));

        analytics.getDashboard();
    }
}