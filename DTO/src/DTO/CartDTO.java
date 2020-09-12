package DTO;

import java.util.List;

public class CartDTO {
    final private List<ItemExtendedDTO> items;
    final private double distanceFromStoreToCustomer;
    final private double PPK;
    final private double deliveryPrice;
    final private double totalItemsPrice;
    final private double totalOrderPrice;

    final private Integer storeId;
    final private String storeName;
    final private Integer storeXLocation;
    final private Integer storeYLocation;
    final private Integer itemsNumber;

    public CartDTO(List<ItemExtendedDTO> items, double distanceFromStoreToCustomer,
                   double PPK, double deliveryPrice, Integer storeId, String storeName,
                   Integer storeXLocation, Integer storeYLocation) {
        this.items = items;
        itemsNumber = items.size();
        this.distanceFromStoreToCustomer = distanceFromStoreToCustomer;
        this.PPK = PPK;
        this.deliveryPrice = deliveryPrice;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeXLocation = storeXLocation;
        this.storeYLocation = storeYLocation;
        this.totalItemsPrice = calculateTotalItemsPrice();
        this.totalOrderPrice = this.totalItemsPrice + this.deliveryPrice;
    }

    private double calculateTotalItemsPrice(){
        return  items.stream()
                .mapToDouble(ItemExtendedDTO::getPriceSum)
                .sum();
    }

    public List<ItemExtendedDTO> getItems() {
        return items;
    }

    public double getDistanceFromStoreToCustomer() {
        return distanceFromStoreToCustomer;
    }

    public double getPPK() {
        return PPK;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public double getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public Integer getStoreXLocation() {
        return storeXLocation;
    }

    public Integer getStoreYLocation() {
        return storeYLocation;
    }

    public Integer getItemsNumber() {
        return itemsNumber;
    }
}
