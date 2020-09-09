package sdm.discount;

import java.util.List;

public class Discount {
    private final String name;
    private final int itemId;
    private final double amount;
    private final Operator operator;
    private final List<Offer> offerList;

    public Discount(String name, int itemId, double amount, Operator operator, List<Offer> offerList) {
        this.name = name;
        this.itemId = itemId;
        this.amount = amount;
        this.operator = operator;
        this.offerList = offerList;
    }
}
