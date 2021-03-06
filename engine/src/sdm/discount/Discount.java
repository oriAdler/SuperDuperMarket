package sdm.discount;

import java.util.List;

public class Discount {
    private final String name;
    private final int itemId;
    private final double amount;
    private final String operator;
    private final List<Offer> offerList;

    public Discount(String name, int itemId, double amount, String operator, List<Offer> offerList) {
        this.name = name;
        this.itemId = itemId;
        this.amount = amount;
        this.operator = operator;
        this.offerList = offerList;
    }

    public String getName() {
        return name;
    }

    public int getItemId() {
        return itemId;
    }

    public double getAmount() {
        return amount;
    }

    public String getOperator() {
        return operator;
    }

    public List<Offer> getOfferList() {
        return offerList;
    }
}
