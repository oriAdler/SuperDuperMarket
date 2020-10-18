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
    final private double totalItemsIncome;
    final private String ownerName;
    final private int numOfOrders;
    final private List<PrivateOrderDTO> orderDTOList;

    private String newItemPrice;

    public StoreDTO(int id, String name, List<ItemDTO> items,
                    List<OrderDTO> orders, Integer xLocation, Integer yLocation,
                    double PPK, double totalDeliveryIncome, double totalItemsIncome, String ownerName, List<PrivateOrderDTO> orderDTOList) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.orders = orders;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.PPK = PPK;
        this.totalDeliveryIncome = totalDeliveryIncome;
        this.totalItemsIncome = totalItemsIncome;
        this.ownerName = ownerName;
        this.numOfOrders = orders.size();
        this.orderDTOList = orderDTOList;

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

    public double getTotalItemsIncome() {
        return totalItemsIncome;
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
