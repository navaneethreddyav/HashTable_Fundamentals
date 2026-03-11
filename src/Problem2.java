import java.util.*;

public class Problem2 {

    // productId -> stock count
    private HashMap<String, Integer> inventory;

    // productId -> waiting list of userIds (FIFO)
    private HashMap<String, LinkedList<Integer>> waitingList;

    public Problem2() {
        inventory = new HashMap<>();
        waitingList = new HashMap<>();
    }

    // Add product to inventory
    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock availability
    public String checkStock(String productId) {
        if (!inventory.containsKey(productId)) {
            return "Product not found";
        }

        int stock = inventory.get(productId);
        return stock + " units available";
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {

        if (!inventory.containsKey(productId)) {
            return "Product not found";
        }

        int stock = inventory.get(productId);

        if (stock > 0) {
            stock--;
            inventory.put(productId, stock);

            return "Success! " + stock + " units remaining";
        }
        else {
            LinkedList<Integer> queue = waitingList.get(productId);
            queue.add(userId);

            return "Added to waiting list, position #" + queue.size();
        }
    }

    // Display waiting list
    public void showWaitingList(String productId) {

        LinkedList<Integer> queue = waitingList.get(productId);

        if (queue.isEmpty()) {
            System.out.println("Waiting list is empty");
            return;
        }

        System.out.println("Waiting List:");
        for (Integer user : queue) {
            System.out.println("UserID: " + user);
        }
    }

    public static void main(String[] args) {

        Problem2 manager = new Problem2();

        // Add product with limited stock
        manager.addProduct("IPHONE15_256GB", 3);

        // Check stock
        System.out.println(manager.checkStock("IPHONE15_256GB"));

        // Simulate purchases
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 11111));

        // Stock finished
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 22222));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 33333));

        // Show waiting list
        manager.showWaitingList("IPHONE15_256GB");
    }
}