package course.java.sdm.store;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {
    final private int Id;
    final private String name;
    private Map<Integer, Integer> items;
    private Map<Integer, Double> salesCounter;
    final private Point location;
    final private double PPK;
    private List<Integer> orders;
    private double totalDeliveryIncome;

    public Store(int id, String name, Map<Integer, Integer> items, Point location, double PPK) {
        this.Id = id;
        this.name = name;
        this.items = items;
        salesCounter = new HashMap<>();
        items.forEach((key, value) -> salesCounter.put(key, (double) 0));
        this.location = location;
        this.PPK = PPK;
        this.orders = new ArrayList<>();
        this.totalDeliveryIncome = 0;
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public Map<Integer, Double> getSalesCounter() {
        return salesCounter;
    }

    public String getName() {
        return name;
    }

    public double getPPK() {
        return PPK;
    }

    public Point getLocation() {
        return location;
    }

    public List<Integer> getOrders() {
        return orders;
    }

    public double getTotalDeliveryIncome() {
        return totalDeliveryIncome;
    }

    public void setTotalDeliveryIncome(double totalDeliveryIncome) {
        this.totalDeliveryIncome = totalDeliveryIncome;
    }
}
