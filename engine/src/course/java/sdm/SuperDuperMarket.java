package course.java.sdm;

import course.java.sdm.order.Order;
import course.java.sdm.item.Item;
import course.java.sdm.store.Store;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperDuperMarket {
    private Map<Integer, Store> stores;
    private Map<Integer, Item> items;
    private Map<Integer, Order> orders;

    public SuperDuperMarket(Map<Integer, Store> stores, Map<Integer, Item> items) {
        this.stores = stores;
        this.items = items;
        this.orders = new HashMap<>();
    }

    public Map<Integer, Store> getStores() {
        return stores;
    }

    public Map<Integer, Item> getItems() {
        return items;
    }

    public Map<Integer, Order> getOrders() {
        return orders;
    }
}
