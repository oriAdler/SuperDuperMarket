package DTO;

import java.util.List;

public class StoreDTO implements LocationDTO{
    final private int id;
    final private String name;
    final private List<ItemDTO> items;
    final private List<OrderDTO> orders;
    final private Integer xLocation;
    final private Integer yLocation;
    final private double PPK;
    final private double totalDeliveryIncome;
    final private String ownerName;

    private String newItemPrice;

    public StoreDTO(int id, String name, List<ItemDTO> items,
                    List<OrderDTO> orders, Integer xLocation, Integer yLocation,
                    double PPK, double totalDeliveryIncome, String ownerName) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.orders = orders;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.PPK = PPK;
        this.totalDeliveryIncome = totalDeliveryIncome;
        this.ownerName = ownerName;

        newItemPrice = "0";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public double getPPK() {
        return PPK;
    }

    public double getTotalDeliveryIncome() {
        return totalDeliveryIncome;
    }

    public Integer getxLocation() {
        return xLocation;
    }

    public Integer getyLocation() {
        return yLocation;
    }

    public String getNewItemPrice() {
        return newItemPrice;
    }

    public void setNewItemPrice(String newItemPrice) {
        this.newItemPrice = newItemPrice;
    }

    @Override
    public String toString() {
        return name;
    }
}
