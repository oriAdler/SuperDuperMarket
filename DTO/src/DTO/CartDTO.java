package DTO;

import java.util.List;

public class CartDTO {
    final private List<ItemExtendedDTO> items;
    final private double distanceFromStoreToCustomer;
    final private double PPK;
    final private double deliveryPrice;
    final private double totalItemsPrice;
    final private double totalOrderPrice;

    public CartDTO(List<ItemExtendedDTO> items, double distanceFromStoreToCustomer,
                   double PPK, double deliveryPrice) {
        this.items = items;
        this.distanceFromStoreToCustomer = distanceFromStoreToCustomer;
        this.PPK = PPK;
        this.deliveryPrice = deliveryPrice;
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
}
