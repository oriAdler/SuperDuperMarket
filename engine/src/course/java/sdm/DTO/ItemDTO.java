package course.java.sdm.DTO;

import course.java.sdm.item.PurchaseCategory;

public class ItemDTO {
    final private int id;
    final private String name;
    final private PurchaseCategory category;
    final private int numOfSellers;
    final private double price;
    final private double numOfSales;

    public ItemDTO(int id, String name, PurchaseCategory category,
                   int numOfSellers, double price, double numOfSales) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.numOfSellers = numOfSellers;
        this.price = price;
        this.numOfSales = numOfSales;
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

    public int getNumOfSellers() {
        return numOfSellers;
    }

    public double getPrice() {
        return price;
    }

    public double getNumOfSales() {
        return numOfSales;
    }

    @Override
    public String toString() {
        return "{" +
                "Id: " + id +
                ", Name: '" + name + '\'' +
                ", Purchase category: " + category.toString() +
                ", Number of sellers: " + numOfSellers +
                ", Price: " + String.format("%.2f", price) +
                ", Number of sales: " + (category.equals(PurchaseCategory.QUANTITY) ?
                String.format("%.0f", numOfSales) + " Units" : String.format("%.2f", numOfSales) + " Kg") +
                '}';
    }

    public String toStringInStore(){
        return "{" +
                "Id: " + id +
                ", Name: '" + name + '\'' +
                ", Purchase category: " + category.toString() +
                ", Price: " + String.format("%.2f", price) +
                ", Number of sales: " + (category.equals(PurchaseCategory.QUANTITY) ?
                String.format("%.0f", numOfSales) + " Units" : String.format("%.2f", numOfSales) + " Kg") +
                '}';
    }
}
