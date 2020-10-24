package engine.users;

import sdm.order.OrderStatic;

import java.util.ArrayList;
import java.util.List;

public class Customer extends UserImpl {
    final private List<Integer> ordersIdList;
    private double averageOrdersPrice;
    private double averageDeliveryPrice;
    private double averageItemsPrice;

    public Customer(int id, String name) {
        super(id, name, "Customer");

        this.ordersIdList = new ArrayList<>();
        averageOrdersPrice = 0;
        averageDeliveryPrice = 0;
        averageItemsPrice = 0;
    }

    public List<Integer> getOrdersIdList() {
        return ordersIdList;
    }

    public double getAverageOrdersPrice() {
        return averageOrdersPrice;
    }

    public double getAverageDeliveryPrice() {
        return averageDeliveryPrice;
    }

    public double getAverageItemsPrice() {
        return averageItemsPrice;
    }

    public void addNewOrder(OrderStatic order, Integer orderId){
        averageOrdersPrice =
                (averageOrdersPrice * ordersIdList.size() + order.getTotalOrderPrice())
                        / (ordersIdList.size() + 1);

        averageDeliveryPrice = (averageDeliveryPrice * ordersIdList.size() + order.getDeliveryPrice())
                / (ordersIdList.size() + 1);
        ordersIdList.add(orderId);

        averageItemsPrice = averageOrdersPrice - averageDeliveryPrice;
    }
}
