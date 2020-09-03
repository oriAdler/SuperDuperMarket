package DTO;

public class CustomerDTO {
    final private int id;
    final private String name;
    final private Integer xLocation;
    final private Integer yLocation;
    final private int numberOfOrders;
    final private double averageOrdersPrice;
    final private double averageDeliveryPrice;

    public CustomerDTO(int id, String name, Integer xLocation, Integer yLocation,
                       int numberOfOrders, double averageOrdersPrice, double averageDeliveryPrice) {
        this.id = id;
        this.name = name;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.numberOfOrders = numberOfOrders;
        this.averageOrdersPrice = averageOrdersPrice;
        this.averageDeliveryPrice = averageDeliveryPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getxLocation() {
        return xLocation;
    }

    public Integer getyLocation() {
        return yLocation;
    }

    public int getNumberOfOrders() {
        return numberOfOrders;
    }

    public double getAverageOrdersPrice() {
        return averageOrdersPrice;
    }

    public double getAverageDeliveryPrice() {
        return averageDeliveryPrice;
    }
}
