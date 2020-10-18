package sdm.item;

import DTO.ItemDTO;

import java.io.Serializable;

public class Item implements Serializable {
    final private int id;
    final private String name;
    final private PurchaseCategory category;
    final private double numberOfSales;

    public Item(int id, String name, String purchaseCategory, double numberOfSales) {
        this.id = id;
        this.name = name;
        this.category = PurchaseCategory.valueOf(purchaseCategory.toUpperCase());
        this.numberOfSales = numberOfSales;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PurchaseCategory getPurchaseCategory() {
        return category;
    }

    public double getNumberOfSales() {
        return numberOfSales;
    }
}
