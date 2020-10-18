package sdm.order;

import DTO.CartDTO;
import DTO.ItemExtendedDTO;
import DTO.OrderDTO;
import sdm.item.Item;

import java.awt.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class OrderStatic implements Serializable {
    static private int id = 0;
    final private LocalDate date;
    final private int storeId;
    final private String storeName;
    final private int numOfItems;
    final private double itemsPrice;
    final private double deliveryPrice;
    final private double totalOrderPrice;
    final private List<Item> itemList;
    final private int customerId;
    private CartDTO cart;
    final private Point customerLocation;

    public OrderStatic(LocalDate date, int storeId, String storeName, int numOfItems, double itemsPrice,
                       double deliveryPrice, double totalOrderPrice, boolean advanceId,
                       List<Item> itemList, int customerId, Point customerLocation) {
        this.customerLocation = customerLocation;
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
        this.itemList = itemList;
        this.customerId = customerId;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        OrderStatic.id = id;
    }

    public LocalDate getDate() {
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

    public List<Item> getItemList() {
        return itemList;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Point getCustomerLocation() {
        return customerLocation;
    }

    public OrderDTO convertOrderToOrderDTO(int orderId){
        return new OrderDTO(orderId,
                this.getDate(),
                this.getStoreId(),
                this.getStoreName(),
                this.getNumOfItems(),
                this.getItemsPrice(),
                this.getDeliveryPrice(),
                this.getTotalOrderPrice());
    }

    public void setCart(CartDTO cart) {
        this.cart = cart;
    }

    public CartDTO getCart() {
        return cart;
    }

    public double getItemsAveragePrice() {
        return this.getItemsPrice();
    }

    public int getNumOfStores(){
        return 1;
    }

    public List<ItemExtendedDTO> getItemExtendedDTOList(){
        return this.cart.getItems();
    }
}
