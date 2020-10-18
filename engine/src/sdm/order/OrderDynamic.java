package sdm.order;

import DTO.ItemExtendedDTO;
import DTO.OrderDTO;
import sdm.item.Item;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

public class OrderDynamic extends OrderStatic {
    Map<Integer, OrderStatic> storeIdToOrder;

    public OrderDynamic(LocalDate date, int storeId, String storeName, int numOfItems, double itemsPrice,
                        double deliveryPrice, double totalOrderPrice, Map<Integer, OrderStatic> storeIdToOrder,
                        boolean advanceId, List<Item> itemList, int customerId, Point customerLocation, String customerName) {
        super(date, storeId, "Super Duper Market", numOfItems, itemsPrice,
                deliveryPrice, totalOrderPrice, advanceId, itemList, customerId, customerLocation, customerName);
        this.storeIdToOrder = storeIdToOrder;
    }

    @Override
    public OrderDTO convertOrderToOrderDTO(int orderId) {
        return super.convertOrderToOrderDTO(orderId);
    }

    public Map<Integer, OrderStatic> getStoreIdToOrder() {
        return storeIdToOrder;
    }

    public double getItemsAveragePrice() {
        OptionalDouble averagePrice = storeIdToOrder
                .values()
                .stream()
                .mapToDouble(OrderStatic::getItemsPrice)
                .average();
        return averagePrice.isPresent() ? averagePrice.getAsDouble() : 0;
    }

    public int getNumOfStores(){
        return storeIdToOrder.size();
    }

    public int getNumOfItems(){
        return storeIdToOrder.values()
                .stream()
                .mapToInt(OrderStatic::getNumOfItems)
                .sum();
    }

    public double getItemsPrice(){
        return storeIdToOrder.values()
                .stream()
                .mapToDouble(OrderStatic::getItemsPrice)
                .sum();
    }

    public double getDeliveryPrice(){
        return storeIdToOrder.values()
                .stream()
                .mapToDouble(OrderStatic::getDeliveryPrice)
                .sum();
    }

    public double getTotalOrderPrice(){
        return storeIdToOrder.values()
                .stream()
                .mapToDouble(OrderStatic::getTotalOrderPrice)
                .sum();
    }

    public List<ItemExtendedDTO> getItemExtendedDTOList(){
        List<ItemExtendedDTO> itemExtendedDTOList = new ArrayList<>();

        storeIdToOrder.values()
                .forEach(store -> itemExtendedDTOList.addAll(store.getCart().getItems()));

        return itemExtendedDTOList;
    }
}
