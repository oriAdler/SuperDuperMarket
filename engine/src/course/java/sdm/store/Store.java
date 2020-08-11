package course.java.sdm.store;

import course.java.sdm.item.Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Store {
    private int Id;
    private String name;
    private Map<Integer, Item> items;
    private Point location;
    private double PPK;
    private int totalShipmentsIncome;
    private int numOfSales;

    public Store(int id, String name, Map<Integer, Item> items, Point location, double PPK) {
        this.Id = id;
        this.name = name;
        this.items = items;
        this.location = location;
        this.PPK = PPK;
    }
}
