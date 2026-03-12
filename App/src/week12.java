import java.util.*;

public class week12 {

    // Stores username -> userId
    private HashMap<String, Integer> usernameMap;

    // Stores username -> number of attempts
    private HashMap<String, Integer> attemptCount;

    public week12() {
        usernameMap = new HashMap<>();
        attemptCount = new HashMap<>();
    }

    // Register a username
    public void registerUser(String username, int userId) {
        usernameMap.put(username, userId);
    }

    // Check if username is available
    public boolean checkAvailability(String username) {

        // Update attempt frequency
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);

        if (usernameMap.containsKey(username)) {
            return false; // already taken
        }
        return true; // available
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        // Append numbers
        for (int i = 1; i <= 5; i++) {
            String newName = username + i;

            if (!usernameMap.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        // Replace underscore with dot
        String modified = username.replace("_", ".");
        if (!usernameMap.containsKey(modified)) {
            suggestions.add(modified);
        }

        return suggestions;
    }

    // Get the most attempted username
    public String getMostAttempted() {

        String mostAttempted = "";
        int max = 0;

        for (String name : attemptCount.keySet()) {

            if (attemptCount.get(name) > max) {
                max = attemptCount.get(name);
                mostAttempted = name;
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }

    // Main method for testing
    public static void main(String[] args) {

        week12 system = new week12();

        // Existing users
        system.registerUser("john_doe", 1001);
        system.registerUser("admin", 1002);
        system.registerUser("alex123", 1003);

        // Check availability
        System.out.println("john_doe available? " + system.checkAvailability("john_doe"));
        System.out.println("jane_smith available? " + system.checkAvailability("jane_smith"));

        // Suggest alternatives
        System.out.println("Suggestions for john_doe:");
        List<String> suggestions = system.suggestAlternatives("john_doe");

        for (String s : suggestions) {
            System.out.println(s);
        }

        // Simulate attempts
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        // Most attempted username
        System.out.println("Most attempted username: " + system.getMostAttempted());
    }
}