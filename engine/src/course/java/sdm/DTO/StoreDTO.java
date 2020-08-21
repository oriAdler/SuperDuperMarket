package course.java.sdm.DTO;

import java.util.List;

public class StoreDTO {
    final private int Id;
    final private String name;
    final private List<ItemDTO> items;
    final private List<OrderDTO> orders;
    final private double PPK;
    final private double totalDeliveryIncome;

    public StoreDTO(int id, String name, List<ItemDTO> items,
                    List<OrderDTO> orders, double PPK, double totalDeliveryIncome) {
        Id = id;
        this.name = name;
        this.items = items;
        this.orders = orders;
        this.PPK = PPK;
        this.totalDeliveryIncome = totalDeliveryIncome;
    }

    public int getId() {
        return Id;
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
}
