import java.util.*;
import java.util.List;

public class OrderManager {
    private static Map<String, List<String>> activeOrders = new HashMap<>();
    private static Map<String, Double> orderTotals = new HashMap<>();
    
    public static void addOrder(String table, List<String> items, double total) {
        activeOrders.put(table, new ArrayList<>(items));
        orderTotals.put(table, total);
    }
    
    public static void addItemsToOrder(String table, List<String> newItems, double additionalTotal) {
        if (activeOrders.containsKey(table)) {
            activeOrders.get(table).addAll(newItems);
            orderTotals.put(table, orderTotals.get(table) + additionalTotal);
        }
    }
    
    public static Set<String> getActiveOrderTables() {
        return activeOrders.keySet();
    }
    
    public static List<String> getOrderItems(String table) {
        return activeOrders.getOrDefault(table, new ArrayList<>());
    }
    
    public static double getOrderTotal(String table) {
        return orderTotals.getOrDefault(table, 0.0);
    }
    
    public static void closeOrder(String table) {
        activeOrders.remove(table);
        orderTotals.remove(table);
    }
    
    public static boolean hasActiveOrders() {
        return !activeOrders.isEmpty();
    }
}