import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
}

class Query {
    String text;
    int frequency;

    public Query(String text, int frequency) {
        this.text = text;
        this.frequency = frequency;
    }
}

public class week12 {

    private TrieNode root = new TrieNode();

    // query → frequency
    private HashMap<String, Integer> frequencyMap = new HashMap<>();

    // insert query into trie
    private void insertQuery(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEnd = true;
    }

    // update frequency
    public void updateFrequency(String query) {

        int freq = frequencyMap.getOrDefault(query, 0) + 1;
        frequencyMap.put(query, freq);

        insertQuery(query);
    }

    // collect queries starting with prefix
    private void dfs(TrieNode node, String prefix, List<String> results) {

        if (node.isEnd) {
            results.add(prefix);
        }

        for (char c : node.children.keySet()) {
            dfs(node.children.get(c), prefix + c, results);
        }
    }

    // search suggestions
    public List<Query> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }

            node = node.children.get(c);
        }

        List<String> matches = new ArrayList<>();
        dfs(node, prefix, matches);

        // min-heap for top 10
        PriorityQueue<Query> pq =
                new PriorityQueue<>(Comparator.comparingInt(q -> q.frequency));

        for (String query : matches) {

            int freq = frequencyMap.get(query);

            pq.offer(new Query(query, freq));

            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<Query> result = new ArrayList<>(pq);
        result.sort((a, b) -> b.frequency - a.frequency);

        return result;
    }

    public static void main(String[] args) {

        week12 system = new week12();

        // simulate previous search history
        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java 21 features");

        List<Query> suggestions = system.search("jav");

        int rank = 1;

        for (Query q : suggestions) {

            System.out.println(rank + ". " + q.text +
                    " (" + q.frequency + " searches)");

            rank++;
        }

        // update frequency after new search
        system.updateFrequency("java 21 features");

        System.out.println("\nUpdated frequency for 'java 21 features'");
    }
}