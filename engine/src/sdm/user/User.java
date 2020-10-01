package sdm.user;

import sdm.order.OrderStatic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    static private int idCounter = 0;
    final private int id;
    final private String name;  // name is unique
    final private Point location;
    final private List<Integer> ordersIdList;
    private double averageOrdersPrice;
    private double averageDeliveryPrice;
    private double averageItemsPrice;
    //final private String type;

    //TODO: delete old constructor, type attribute
    public User(int id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.ordersIdList = new ArrayList<>();
        averageOrdersPrice = 0;
        averageDeliveryPrice = 0;
        averageItemsPrice = 0;
    }

    public User(String name, Point location, String type) {
        this.id = idCounter++;
        this.name = name;
        this.location = location;
        this.ordersIdList = new ArrayList<>();
        averageOrdersPrice = 0;
        averageDeliveryPrice = 0;
        averageItemsPrice = 0;
        //this.type = type;
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