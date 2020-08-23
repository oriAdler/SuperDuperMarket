package course.java.sdm.order;

import java.util.Date;
import java.util.List;

public class OrderDynamic extends OrderStatic {
    List<OrderStatic> orderStaticList;

    public OrderDynamic(Date date, int storeId, String storeName, int numOfItems, double itemsPrice,
                        double deliveryPrice, double totalOrderPrice, List<OrderStatic> orderStaticList,
                        boolean advanceId) {
        super(date, -1, "Super Duper Market", numOfItems, itemsPrice,
                deliveryPrice, totalOrderPrice, advanceId);
        this.orderStaticList = orderStaticList;
    }
}
