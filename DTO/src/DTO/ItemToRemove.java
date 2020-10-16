package DTO;

public class ItemToRemove {
    private final int itemId;
    private final double amount;

    public ItemToRemove(int itemId, double amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    public int getItemId() {
        return itemId;
    }

    public double getAmount() {
        return amount;
    }
}
