package course.java.sdm.order;

import course.java.sdm.DTO.OrderDTO;

import java.util.Date;

public class OrderStatic {
    static private int id = 0;
    final private Date date;
    final private int storeId;
    final private String storeName;
    final private int numOfItems;
    final private double itemsPrice;
    final private double deliveryPrice;
    final private double totalOrderPrice;

    public OrderStatic(Date date, int storeId, String storeName, int numOfItems, double itemsPrice,
                       double deliveryPrice, double totalOrderPrice, boolean advanceId) {
        if(advanceId){
            id++;
        }
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

    public OrderDTO orderToOrderDTO(int orderId){
        return new OrderDTO(orderId,
                this.getDate(),
                this.getStoreId(),
                this.getStoreName(),
                this.getNumOfItems(),
                this.getItemsPrice(),
                this.getDeliveryPrice(),
                this.getTotalOrderPrice());
    }
}
