package DTO;

public class OfferDTO {
    private final int itemId;
    private final String itemName;
    private final double amount;
    private final double price;
    private final int storeId;

    public OfferDTO(int itemId, String itemName, double amount, double price, int storeId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.amount = amount;
        this.price = price;
        this.storeId = storeId;
    }

    public int getItemId() {
        return itemId;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getItemName() {
        return itemName;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s for %.2f each", amount, itemName, price);
    }
}
