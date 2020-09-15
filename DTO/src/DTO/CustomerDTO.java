package DTO;

public class CustomerDTO {
    final private int id;
    final private String name;
    final private int xLocation;
    final private int yLocation;
    final private int numberOfOrders;
    final private double averageItemsPrice;
    final private double averageDeliveryPrice;


    public CustomerDTO(int id, String name, int xLocation, int yLocation,
                       int numberOfOrders, double averageItemsPrice, double averageDeliveryPrice) {
        this.id = id;
        this.name = name;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.numberOfOrders = numberOfOrders;
        this.averageItemsPrice = averageItemsPrice;
        this.averageDeliveryPrice = averageDeliveryPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getXLocation() {
        return xLocation;
    }

    public int getYLocation() {
        return yLocation;
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
