package DTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDTO {
    int id;
    final private Date date;
    int storeId;
    String storeName;
    final private int numOfItems;
    double itemsPrice;
    double deliveryPrice;
    double totalPrice;

    public OrderDTO(int id, Date date, int storeId, String storeName, int numOfItems,
                    double itemsPrice, double deliveryPrice, double totalPrice) {
        this.id = id;
        this.date = date;
        this.storeId = storeId;
        this.storeName = storeName;
        this.numOfItems = numOfItems;
        this.itemsPrice = itemsPrice;
        this.deliveryPrice = deliveryPrice;
        this.totalPrice = totalPrice;
    }

    public int getId() {
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

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM-HH:mm");
        return "{" +
                "id: " + id +
                ", date: " + dateFormat.format(date) +
                ", store id: " + storeId +
                ", store name: " + storeName +
                ", number of items: " + numOfItems +
                String.format(", items price: %.2f", itemsPrice) +
                String.format(", delivery price: %.2f", deliveryPrice) +
                String.format(", total price: %.2f", totalPrice) +
                '}';
    }

    public String toStringInStore() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM-HH:mm");
        return "{" +
                "date: " + dateFormat.format(date) +
                ", number of items: " + numOfItems +
                String.format(", items price: %.2f", itemsPrice) +
                String.format(", delivery price: %.2f", deliveryPrice) +
                String.format(", total price: %.2f", totalPrice) +
                '}';
    }
}
