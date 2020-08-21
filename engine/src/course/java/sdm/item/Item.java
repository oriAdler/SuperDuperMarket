package course.java.sdm.item;

public class Item {
    final private int id;
    final private String name;
    final private PurchaseCategory category;
    final private int numberOfSales;

    public Item(int id, String name, String purchaseCategory) {
        this.id = id;
        this.name = name;
        this.category = PurchaseCategory.valueOf(purchaseCategory.toUpperCase());
        this.numberOfSales = 0;
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

    public int getNumberOfSales() {
        return numberOfSales;
    }
}
