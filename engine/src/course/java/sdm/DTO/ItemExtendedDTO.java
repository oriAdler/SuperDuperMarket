package course.java.sdm.DTO;

import course.java.sdm.item.PurchaseCategory;

public class ItemExtendedDTO extends ItemDTO {
    final private double priceSum;
    final private String storeName;
    final private Integer storeId;

    public ItemExtendedDTO(int id, String name, PurchaseCategory category, int numOfSellers,
                           double price, double numOfSales, String storeName, Integer storeId) {
        super(id, name, category, numOfSellers, price, numOfSales);
        this.storeName = storeName;
        this.storeId = storeId;
        this.priceSum = price * numOfSales;
    }

    public double getPriceSum() {
        return priceSum;
    }

    public String getStoreName() {
        return storeName;
    }

    public Integer getStoreId() {
        return storeId;
    }
}
