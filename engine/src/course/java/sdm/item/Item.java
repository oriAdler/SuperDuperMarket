package course.java.sdm.item;

public class Item {
    private int id;
    private String name;
    private String purchaseCategory;
    private double price;

    public Item(int id, String name, String purchaseCategory) {
        this.id = id;
        this.name = name;
        this.purchaseCategory = purchaseCategory;
    }



    public void setPrice(double price) {
        this.price = price;
    }
}
