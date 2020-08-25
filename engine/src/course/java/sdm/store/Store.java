package course.java.sdm.store;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store implements Serializable {
    final private int Id;
    final private String name;
    final private Map<Integer, Integer> itemIdToPrice;
    final private Map<Integer, Double> itemIdToNumberOfSales;
    final private Point location;
    final private double PPK;
    final private List<Integer> orders;
    private double totalDeliveryIncome;

    public Store(int id, String name, Map<Integer, Integer> itemIdToPrice, Point location, double PPK) {
        this.Id = id;
        this.name = name;
        this.itemIdToPrice = itemIdToPrice;
        itemIdToNumberOfSales = new HashMap<>();
        itemIdToPrice.forEach((key, value) -> itemIdToNumberOfSales.put(key, (double) 0));
        this.location = location;
        this.PPK = PPK;
        this.orders = new ArrayList<>();
        this.totalDeliveryIncome = 0;
    }

    public Map<Integer, Integer> getItemIdToPrice() {
        return itemIdToPrice;
    }

    public Map<Integer, Double> getItemIdToNumberOfSales() {
        return itemIdToNumberOfSales;
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

    public double calculateDeliveryPrice(Point customerLocation){
        return PPK * calculateDistanceFromStoreToCustomer(customerLocation);
    }

    //Calculate distance via pythagoras theorem [D = SQRT( (|X1-X2|)^2 + (|Y1-Y2|)^2)]
    public double calculateDistanceFromStoreToCustomer(Point customer){
        return Math.sqrt(Math.pow(
                Math.abs(location.getX()-customer.getX()), 2)
                + Math.pow(
                Math.abs(location.getY()-customer.getY()), 2));
    }
}
