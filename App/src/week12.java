import java.util.*;

class VideoData {
    String videoId;
    String content;

    public VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // access order
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

public class week12 {

    // L1 (Memory Cache)
    private LRUCache<String, VideoData> L1;

    // L2 (SSD Cache)
    private LRUCache<String, VideoData> L2;

    // L3 (Database simulation)
    private HashMap<String, VideoData> database;

    // access count for promotion
    private HashMap<String, Integer> accessCount;

    // statistics
    private int l1Hits = 0;
    private int l2Hits = 0;
    private int l3Hits = 0;

    public week12() {

        L1 = new LRUCache<>(10000);
        L2 = new LRUCache<>(100000);
        database = new HashMap<>();
        accessCount = new HashMap<>();

        // simulate database videos
        for (int i = 1; i <= 200000; i++) {
            database.put("video_" + i,
                    new VideoData("video_" + i, "VideoContent" + i));
        }
    }

    public VideoData getVideo(String videoId) {

        // L1 lookup
        if (L1.containsKey(videoId)) {

            l1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");

            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 lookup
        if (L2.containsKey(videoId)) {

            l2Hits++;
            System.out.println("L2 Cache HIT (5ms)");

            VideoData video = L2.get(videoId);

            promoteToL1(video);

            return video;
        }

        System.out.println("L2 Cache MISS");

        // L3 database
        if (database.containsKey(videoId)) {

            l3Hits++;
            System.out.println("L3 Database HIT (150ms)");

            VideoData video = database.get(videoId);

            L2.put(videoId, video);

            accessCount.put(videoId,
                    accessCount.getOrDefault(videoId, 0) + 1);

            return video;
        }

        return null;
    }

    // promotion logic
    private void promoteToL1(VideoData video) {

        int count = accessCount.getOrDefault(video.videoId, 0) + 1;
        accessCount.put(video.videoId, count);

        if (count > 2) {
            L1.put(video.videoId, video);
            System.out.println("Promoted to L1 Cache");
        }
    }

    // cache statistics
    public void getStatistics() {

        int total = l1Hits + l2Hits + l3Hits;

        System.out.println("\nCache Statistics:");

        System.out.println("L1 Hit Rate: "
                + (l1Hits * 100.0 / total) + "%");

        System.out.println("L2 Hit Rate: "
                + (l2Hits * 100.0 / total) + "%");

        System.out.println("L3 Hit Rate: "
                + (l3Hits * 100.0 / total) + "%");
    }

    public static void main(String[] args) {

        week12 cache = new week12();

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}