import java.util.*;

public class week12 {

    // productId -> stockCount
    private HashMap<String, Integer> stockMap;

    // productId -> waiting list (FIFO)
    private HashMap<String, LinkedHashMap<Integer, Integer>> waitingList;

    public week12() {
        stockMap = new HashMap<>();
        waitingList = new HashMap<>();
    }

    // Add product to inventory
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new LinkedHashMap<>());
    }

    // Check stock availability
    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        }

        // Add to waiting list
        LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);

        int position = queue.size() + 1;
        queue.put(userId, position);

        return "Added to waiting list, position #" + position;
    }

    // Display waiting list
    public void showWaitingList(String productId) {

        LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);

        for (Map.Entry<Integer, Integer> entry : queue.entrySet()) {
            System.out.println("User " + entry.getKey() +
                    " -> Position " + entry.getValue());
        }
    }

    public static void main(String[] args) {

        week12 manager = new week12();

        // Add product with 100 units
        manager.addProduct("IPHONE15_256GB", 100);

        // Check stock
        System.out.println("Stock Available: "
                + manager.checkStock("IPHONE15_256GB"));

        // Simulate purchases
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate stock exhaustion
        for (int i = 1; i <= 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", 20000 + i);
        }

        // User after stock finished
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));

        // Show waiting list
        manager.showWaitingList("IPHONE15_256GB");
    }
}