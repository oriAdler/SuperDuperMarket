package course.java.sdm.order;

import course.java.sdm.DTO.OrderDTO;
import course.java.sdm.item.Item;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderDynamic extends OrderStatic {
    Map<Integer, OrderStatic> storeIdToOrder;

    public OrderDynamic(Date date, int storeId, String storeName, int numOfItems, double itemsPrice,
                        double deliveryPrice, double totalOrderPrice, Map<Integer, OrderStatic> storeIdToOrder,
                        boolean advanceId, List<Item> itemList) {
        super(date, storeId, "Super Duper Market", numOfItems, itemsPrice,
                deliveryPrice, totalOrderPrice, advanceId, itemList);
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
