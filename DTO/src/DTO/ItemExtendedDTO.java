package DTO;

public class ItemExtendedDTO extends ItemDTO {
    final private double priceSum;
    final private String storeName;
    final private Integer storeId;
    final private Boolean onDiscount;

    public ItemExtendedDTO(int id, String name, String category, int numOfSellers,
                           double price, double numOfSales, String storeName,
                           Integer storeId, Boolean onDiscount, boolean isSoldByStore) {
        super(id, name, category, numOfSellers, price, numOfSales, isSoldByStore);
        this.storeName = storeName;
        this.storeId = storeId;
        this.onDiscount = onDiscount;
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

    public Boolean getOnDiscount() {
        return onDiscount;
    }
}
