package sdm.order;

import DTO.OrderDTO;
import sdm.item.Item;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OrderDynamic extends OrderStatic {
    Map<Integer, OrderStatic> storeIdToOrder;

    public OrderDynamic(LocalDate date, int storeId, String storeName, int numOfItems, double itemsPrice,
                        double deliveryPrice, double totalOrderPrice, Map<Integer, OrderStatic> storeIdToOrder,
                        boolean advanceId, List<Item> itemList, int customerId) {
        super(date, storeId, "Super Duper Market", numOfItems, itemsPrice,
                deliveryPrice, totalOrderPrice, advanceId, itemList, customerId);
        this.storeIdToOrder = storeIdToOrder;
    }

    @Override
    public OrderDTO convertOrderToOrderDTO(int orderId) {
        return super.convertOrderToOrderDTO(orderId);
    }

    public Map<Integer, OrderStatic> getStoreIdToOrder() {
        return storeIdToOrder;
    }
}
