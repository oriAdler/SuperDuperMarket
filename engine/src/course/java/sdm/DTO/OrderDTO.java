package course.java.sdm.DTO;

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
}
