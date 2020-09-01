package sdm.item;

public enum PurchaseCategory {

    WEIGHT("Weight"), QUANTITY("Quantity");

    final private String name;

    PurchaseCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
