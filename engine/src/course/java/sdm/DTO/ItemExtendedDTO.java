package course.java.sdm.DTO;

import course.java.sdm.item.PurchaseCategory;

public class ItemExtendedDTO extends ItemDTO {
    final private double priceSum;

    public ItemExtendedDTO(int id, String name, PurchaseCategory category, int numOfSellers,
                           double price, double numOfSales) {
        super(id, name, category, numOfSellers, price, numOfSales);
        this.priceSum = price * numOfSales;
    }

    public double getPriceSum() {
        return priceSum;
    }
}
