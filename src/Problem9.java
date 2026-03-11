import java.util.*;

public class Problem9 {

    // Transaction class
    static class Transaction {
        int id;
        double amount;
        String merchant;
        String account;
        long timestamp; // milliseconds

        Transaction(int id, double amount, String merchant, String account, long timestamp) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "id:" + id + ", amount:" + amount + ", merchant:" + merchant + ", account:" + account;
        }
    }

    List<Transaction> transactions;

    public Problem9() {
        transactions = new ArrayList<>();
    }

    // Add transaction
    public void addTransaction(Transaction tx) {
        transactions.add(tx);
    }

    // Classic Two-Sum
    public List<List<Transaction>> findTwoSum(double target) {

        List<List<Transaction>> result = new ArrayList<>();
        Map<Double, Transaction> map = new HashMap<>();

        for (Transaction tx : transactions) {
            double complement = target - tx.amount;

            if (map.containsKey(complement)) {
                result.add(Arrays.asList(map.get(complement), tx));
            }

            map.put(tx.amount, tx);
        }

        return result;
    }

    // Two-Sum within time window (in milliseconds)
    public List<List<Transaction>> findTwoSumTimeWindow(double target, long windowMillis) {

        List<List<Transaction>> result = new ArrayList<>();
        Map<Double, List<Transaction>> map = new HashMap<>();

        for (Transaction tx : transactions) {

            double complement = target - tx.amount;

            if (map.containsKey(complement)) {
                for (Transaction t : map.get(complement)) {
                    if (Math.abs(tx.timestamp - t.timestamp) <= windowMillis) {
                        result.add(Arrays.asList(t, tx));
                    }
                }
            }

            map.putIfAbsent(tx.amount, new ArrayList<>());
            map.get(tx.amount).add(tx);
        }

        return result;
    }

    // K-Sum (recursive)
    public List<List<Transaction>> findKSum(int k, double target) {
        List<List<Transaction>> result = new ArrayList<>();
        findKSumHelper(transactions, k, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void findKSumHelper(List<Transaction> txs, int k, double target, int start,
                                List<Transaction> current, List<List<Transaction>> result) {

        if (k == 0 && Math.abs(target) < 1e-6) {
            result.add(new ArrayList<>(current));
            return;
        }
        if (k == 0 || start >= txs.size()) return;

        for (int i = start; i < txs.size(); i++) {
            current.add(txs.get(i));
            findKSumHelper(txs, k - 1, target - txs.get(i).amount, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    // Detect duplicates: same amount, same merchant, different accounts
    public Map<String, List<String>> detectDuplicates() {

        Map<String, Map<Double, Set<String>>> map = new HashMap<>();

        for (Transaction tx : transactions) {
            map.putIfAbsent(tx.merchant, new HashMap<>());
            Map<Double, Set<String>> merchantMap = map.get(tx.merchant);

            merchantMap.putIfAbsent(tx.amount, new HashSet<>());
            merchantMap.get(tx.amount).add(tx.account);
        }

        Map<String, List<String>> duplicates = new HashMap<>();

        for (String merchant : map.keySet()) {
            for (double amount : map.get(merchant).keySet()) {
                Set<String> accounts = map.get(merchant).get(amount);
                if (accounts.size() > 1) {
                    duplicates.put(merchant + " $" + amount, new ArrayList<>(accounts));
                }
            }
        }

        return duplicates;
    }

    public static void main(String[] args) {

        Problem9 system = new Problem9();

        // Sample transactions
        system.addTransaction(new Transaction(1, 500, "Store A", "acc1", 10 * 60 * 1000));
        system.addTransaction(new Transaction(2, 300, "Store B", "acc2", 10 * 15 * 60 * 1000));
        system.addTransaction(new Transaction(3, 200, "Store C", "acc3", 10 * 30 * 60 * 1000));
        system.addTransaction(new Transaction(4, 500, "Store A", "acc2", 10 * 40 * 60 * 1000));

        // Classic Two-Sum
        System.out.println("Two-Sum (target=500):");
        for (List<Transaction> pair : system.findTwoSum(500)) {
            System.out.println(pair);
        }

        // Two-Sum within 1 hour
        System.out.println("\nTwo-Sum within 1 hour:");
        for (List<Transaction> pair : system.findTwoSumTimeWindow(500, 60 * 60 * 1000)) {
            System.out.println(pair);
        }

        // K-Sum (k=3, target=1000)
        System.out.println("\nK-Sum (k=3, target=1000):");
        for (List<Transaction> kset : system.findKSum(3, 1000)) {
            System.out.println(kset);
        }

        // Duplicate detection
        System.out.println("\nDuplicate Transactions:");
        Map<String, List<String>> dups = system.detectDuplicates();
        for (String key : dups.keySet()) {
            System.out.println(key + " → accounts: " + dups.get(key));
        }
    }
}