import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;

    public Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class week12 {

    // Classic Two-Sum
    public static void findTwoSum(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction match = map.get(complement);

                System.out.println("Two-Sum Pair: (" +
                        match.id + ", " + t.id + ")");
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum with 1-hour window
    public static void findTwoSumWithTimeWindow(List<Transaction> transactions,
                                                int target, long windowMillis) {

        HashMap<Integer, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                for (Transaction prev : map.get(complement)) {

                    if (Math.abs(t.time - prev.time) <= windowMillis) {

                        System.out.println("Time Window Pair: (" +
                                prev.id + ", " + t.id + ")");
                    }
                }
            }

            map.putIfAbsent(t.amount, new ArrayList<>());
            map.get(t.amount).add(t);
        }
    }

    // Duplicate detection
    public static void detectDuplicates(List<Transaction> transactions) {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (String key : map.keySet()) {

            List<Transaction> list = map.get(key);

            if (list.size() > 1) {

                System.out.print("Duplicate transactions: ");

                for (Transaction t : list) {
                    System.out.print("ID " + t.id + " ");
                }

                System.out.println();
            }
        }
    }

    // K-Sum (recursive)
    public static void findKSum(int[] nums, int target, int k,
                                int start, List<Integer> path) {

        if (k == 2) {

            HashSet<Integer> set = new HashSet<>();

            for (int i = start; i < nums.length; i++) {

                int complement = target - nums[i];

                if (set.contains(complement)) {

                    List<Integer> result = new ArrayList<>(path);
                    result.add(nums[i]);
                    result.add(complement);

                    System.out.println("K-Sum match: " + result);
                }

                set.add(nums[i]);
            }

            return;
        }

        for (int i = start; i < nums.length; i++) {

            path.add(nums[i]);

            findKSum(nums, target - nums[i], k - 1, i + 1, path);

            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1, 500, "Store A",
                "acc1", 1000));

        transactions.add(new Transaction(2, 300, "Store B",
                "acc2", 1100));

        transactions.add(new Transaction(3, 200, "Store C",
                "acc3", 1200));

        transactions.add(new Transaction(4, 500, "Store A",
                "acc2", 1300));

        System.out.println("Two-Sum:");
        findTwoSum(transactions, 500);

        System.out.println("\nTwo-Sum with Time Window:");
        findTwoSumWithTimeWindow(transactions,
                500, 3600000);

        System.out.println("\nDuplicate Detection:");
        detectDuplicates(transactions);

        System.out.println("\nK-Sum (3 numbers):");

        int[] nums = {500, 300, 200, 400, 100};

        findKSum(nums, 1000, 3, 0, new ArrayList<>());
    }
}