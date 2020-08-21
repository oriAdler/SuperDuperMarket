package course.java.sdm.order;

import java.util.Date;

public class Order {
    static private int id = 0;
    final private Date date;
    final private int storeId;
    final private String storeName;
    final private int numOfItems;
    final private double itemsPrice;
    final private double deliveryPrice;
    final private double totalOrderPrice;

    public Order(Date date, int storeId, String storeName, int numOfItems,
                 double itemsPrice, double deliveryPrice, double totalOrderPrice) {
        id++;
        this.date = date;
        this.storeId = storeId;
        this.storeName = storeName;
        this.numOfItems = numOfItems;
        this.itemsPrice = itemsPrice;
        this.deliveryPrice = deliveryPrice;
        this.totalOrderPrice = totalOrderPrice;
    }

    public static int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getNumOfItems() {
        return numOfItems;
    }

    public double getItemsPrice() {
        return itemsPrice;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }
}
