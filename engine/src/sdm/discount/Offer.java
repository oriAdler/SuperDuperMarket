package sdm.discount;

public class Offer {
    private final int itemId;
    private final double amount;
    private final double price;

    public Offer(int itemId, double amount, double price) {
        this.itemId = itemId;
        this.amount = amount;
        this.price = price;
    }
}
