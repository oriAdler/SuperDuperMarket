package sdm.customer;

import sdm.order.OrderStatic;

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
    private double averageItemsPrice;

    public Customer(int id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.ordersIdList = new ArrayList<>();
        averageOrdersPrice = 0;
        averageDeliveryPrice = 0;
        averageItemsPrice = 0;
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

    public double getAverageItemsPrice() {
        return averageItemsPrice;
    }

    public void addNewOrder(OrderStatic order, Integer orderId){
        averageOrdersPrice =
                (averageOrdersPrice * ordersIdList.size() + order.getTotalOrderPrice())
                        / (ordersIdList.size() + 1);

        averageDeliveryPrice = (averageDeliveryPrice * ordersIdList.size() + order.getDeliveryPrice())
                / (ordersIdList.size() + 1);
        ordersIdList.add(orderId);

        averageItemsPrice = averageOrdersPrice - averageDeliveryPrice;
    }
}
