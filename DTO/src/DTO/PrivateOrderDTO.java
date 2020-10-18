package DTO;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PrivateOrderDTO {
    private final int id;
    private final LocalDate localDate;
    private final String customerName;
    private final Point location;
    private final int numOfStores;
    private final int numOfItems;
    private final double itemsPrice;
    private final double deliveryPrice;
    private final double totalPrice;
    private final List<ItemExtendedDTO> itemsList;

    public PrivateOrderDTO(int id, LocalDate localDate, String customerName, Point location, int numOfStores, int numOfItems,
                           double itemsPrice, double deliveryPrice, double totalPrice, List<ItemExtendedDTO> itemsList) {
        this.id = id;
        this.localDate = localDate;
        this.customerName = customerName;
        this.location = location;
        this.numOfStores = numOfStores;
        this.numOfItems = numOfItems;
        this.itemsPrice = itemsPrice;
        this.deliveryPrice = deliveryPrice;
        this.totalPrice = totalPrice;
        this.itemsList = itemsList;
    }
}
