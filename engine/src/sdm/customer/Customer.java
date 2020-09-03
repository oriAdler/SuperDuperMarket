package sdm.customer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    final private int id;
    final private String name;
    final private Point location;
    final private List<Integer> ordersIdList;
    private double averageOrdersPrice;
    private double averageDeliveryPrice;

    public Customer(int id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.ordersIdList = new ArrayList<>();
        averageOrdersPrice = 0;
        averageDeliveryPrice = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public List<Integer> getOrdersIdList() {
        return ordersIdList;
    }

    public double getAverageOrdersPrice() {
        return averageOrdersPrice;
    }

    public double getAverageDeliveryPrice() {
        return averageDeliveryPrice;
    }
}
