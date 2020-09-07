package DTO;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

import java.util.Objects;

public class ItemDTO {
    final private int id;
    final private String name;
    final private String category;
    final private int numOfSellers;
    private String price;
    final private double numOfSales;
    private String amount;

    public ItemDTO(int id, String name, String category,
                   int numOfSellers, double price, double numOfSales) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.numOfSellers = numOfSellers;
        this.price = String.format("%.2f", price);
        this.numOfSales = numOfSales;

        this.amount = ("0");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory(){
        return category;
    }

    // Backward compatibility
    public String getPurchaseCategory() {
        return category;
    }

    public int getNumOfSellers() {
        return numOfSellers;
    }

    public String getPrice() {
        return price;
    }

    public double getNumOfSales() {
        return numOfSales;
    }

    // Features for Desktop Application:


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDTO itemDTO = (ItemDTO) o;
        return id == itemDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
