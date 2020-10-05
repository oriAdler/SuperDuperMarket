package DTO;

public class RegionDTO {
    private final String ownerName;
    private final String regionName;
    private final int numOfItemsType;
    private final int numOfStores;
    private final int numOfOrders;
    private final double ordersAveragePrice;

    public RegionDTO(String ownerName, String regionName, int numOfItemsType,
                     int numOfStores, int numOfOrders, double ordersAveragePrice) {
        this.ownerName = ownerName;
        this.regionName = regionName;
        this.numOfItemsType = numOfItemsType;
        this.numOfStores = numOfStores;
        this.numOfOrders = numOfOrders;
        this.ordersAveragePrice = ordersAveragePrice;
    }
}
