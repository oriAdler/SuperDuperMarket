package DTO;

import java.util.List;

public class DiscountDTO {
    private final String name;
    private final int itemId;
    private final String itemName;
    private final double amount;
    private final String operator;
    private final List<OfferDTO> offerList;

    public DiscountDTO(String name, int itemId, String itemName, double amount, String operator, List<OfferDTO> offerList) {
        this.name = name;
        this.itemId = itemId;
        this.itemName = itemName;
        this.amount = amount;
        this.operator = operator;
        this.offerList = offerList;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Buy %.2f %s and get %s", amount, itemName, operator);
    }

    public List<OfferDTO> getOfferList() {
        return offerList;
    }

    public String getOperator() {
        return operator;
    }

    public int getItemId() {
        return itemId;
    }

    public double getAmount() {
        return amount;
    }
}
