package sdm.customer;

import java.awt.*;
import java.util.ArrayList;

public class Customer {
    final private int id;
    final private String name;
    final private Point location;
    //TODO: ordersIdList, averageOrdersPrice, averageDeliveryPrice.


    public Customer(int id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }
}
