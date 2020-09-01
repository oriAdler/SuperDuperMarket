package DTO;

import java.util.List;
import java.util.stream.Collectors;

public class StoreDTO {
    final private int id;
    final private String name;
    final private List<ItemDTO> items;
    final private List<OrderDTO> orders;
    final private double PPK;
    final private double totalDeliveryIncome;

    public StoreDTO(int id, String name, List<ItemDTO> items,
                    List<OrderDTO> orders, double PPK, double totalDeliveryIncome) {
        this.id = id;
        this.name = name;
        this.items = items;
        this.orders = orders;
        this.PPK = PPK;
        this.totalDeliveryIncome = totalDeliveryIncome;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public double getPPK() {
        return PPK;
    }

    public double getTotalDeliveryIncome() {
        return totalDeliveryIncome;
    }

    @Override
    public String toString() {
        return "---" + name + "---" + "\n" +
                "Id: " + id + "\n" +
                "--Items-- \n" +
                items.stream()
                        .map(ItemDTO::toStringInStore)
                        .collect(Collectors.joining("\n")) +
                "\n" + "----------------------------------------\n" +
                "Orders: \n" +
                (orders.isEmpty() ? "No orders have been placed" :
                        orders.stream()
                                .map(OrderDTO::toStringInStore)
                                .collect(Collectors.joining("\n"))) +
                "\n" + "----------------------------------------\n" +
                String.format("PPK: %.2f", PPK) + "\n" +
                String.format("Total delivery income: %.2f", totalDeliveryIncome) + "\n" +
                "----------------------------------------\n";
    }
}
