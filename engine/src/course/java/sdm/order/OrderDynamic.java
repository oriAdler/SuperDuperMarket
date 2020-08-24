package course.java.sdm.order;

import course.java.sdm.DTO.OrderDTO;

import java.util.Date;
import java.util.Map;

public class OrderDynamic extends OrderStatic {
    Map<Integer, OrderStatic> storeIdToOrder;

    public OrderDynamic(Date date, int storeId, String storeName, int numOfItems, double itemsPrice,
                        double deliveryPrice, double totalOrderPrice, Map<Integer, OrderStatic> storeIdToOrder,
                        boolean advanceId) {
        super(date, storeId, "Super Duper Market", numOfItems, itemsPrice,
                deliveryPrice, totalOrderPrice, advanceId);
        this.storeIdToOrder = storeIdToOrder;
    }

    @Override
    public OrderDTO orderToOrderDTO(int orderId) {
        return super.orderToOrderDTO(orderId);
    }

    public Map<Integer, OrderStatic> getStoreIdToOrder() {
        return storeIdToOrder;
    }
}
