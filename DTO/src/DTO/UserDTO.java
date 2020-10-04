package DTO;

public class UserDTO implements LocationDTO{
    final private int id;
    final private String name;
    final private int numberOfOrders;
    final private double averageItemsPrice;
    final private double averageDeliveryPrice;
    final private String type;

    public UserDTO(int id, String name, int numberOfOrders, double averageItemsPrice, double averageDeliveryPrice, String type) {
        this.id = id;
        this.name = name;
        this.numberOfOrders = numberOfOrders;
        this.averageItemsPrice = averageItemsPrice;
        this.averageDeliveryPrice = averageDeliveryPrice;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public double getAverageItemsPrice() {
        return averageItemsPrice;
    }

    public double getAverageDeliveryPrice() {
        return averageDeliveryPrice;
    }

    @Override
    public String toString() {
        return name;
    }
}
