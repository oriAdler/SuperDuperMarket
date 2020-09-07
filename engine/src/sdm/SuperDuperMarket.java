package sdm;

import DTO.CartDTO;
import DTO.CustomerDTO;
import DTO.StoreDTO;
import sdm.item.PurchaseCategory;
import sdm.order.OrderStatic;

import java.awt.*;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

public interface SuperDuperMarket {
    void checkCustomerLocationIsValid(Point location);
    boolean isStoreExistById(int id);
    boolean isItemSoldByStore(int storeId, int itemId);
    boolean itemNotExistInSystem(int itemId);

    int getItemPriceInStore(int storeId, int itemId);
    PurchaseCategory getItemPurchaseCategory(int itemId);
    String getStoreNameById(int storeId);
    String getItemNameById(int itemId);

    void removeItemFromStore(int storeId, int itemId);
    void addItemToStore(int storeId, int itemId, int itemNewPrice);
    void updateItemPriceInStore(int storeId, int itemId, int itemNewPrice);

    void executeStaticOrder(CartDTO cart, Date date, int storeId);
    void executeDynamicOrder(CartDTO cart, Date orderDate, Point customerLocation);
    CartDTO summarizeStaticOrder(Map<Integer, Double> itemsMap, int storeId, Point customerLocation);
    CartDTO summarizeDynamicOrder(Map<Integer, Double> itemsMap, Point customerLocation);

    void addOrdersFromFileToSDM(Map<Integer, OrderStatic> orderIdToOrder);
    void calculateDeliveryPrice(Integer storeId, Integer customerId, Consumer<Double> deliveryPrice);
}