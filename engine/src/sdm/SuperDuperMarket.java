package sdm;

import DTO.*;
import sdm.item.PurchaseCategory;
import sdm.order.OrderStatic;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
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

    Map<String, TransactionDTO> executeStaticOrder(CartDTO cart, LocalDate date, int customerId, Point customerLocation, String userName, Map<String, List<String>> userNameToNotification);
    Map<String, TransactionDTO> executeDynamicOrder(List<CartDTO> cartList, LocalDate orderDate, Integer customerId, Point customerLocation, String userName, Map<String, List<String>> userNameToNotification);
    CartDTO summarizeStaticOrder(Map<Integer, Double> itemsMap, List<OfferDTO> offersToAddToCart, int storeId, Integer customerId, Point customerLocation);
    List<CartDTO> summarizeDynamicOrder(Map<Integer, Double> itemsMap, List<OfferDTO> offersToAddToCart, Integer customerId, Point customerLocation);
    List<CartDTO> getDetailedOrder(Integer orderId);

    void addOrdersFromFileToSDM(Map<Integer, OrderStatic> orderIdToOrder);
    void calculateDeliveryPrice(Integer storeId, Integer customerId, Point customerLocation, Consumer<Double> deliveryPrice);
    Double calculateDeliveryPrice(Integer storeId, Integer customerId, Point customerLocation);
    Double calculateDistanceStoreToCustomer(Integer storeId, Integer customerId, Point customerLocation);

    List<DiscountDTO> getStoreDiscounts(Integer storeId);
    List<DiscountDTO> getStoreDiscounts(Map <Integer, Double> itemIdToAmount, Integer storeId);
    List<DiscountDTO> getDiscounts(Map<Integer, Double> itemIdToAmount);

    List<ItemDTO> getStoreItems(int storeId);
    //List<ItemDTO> getItemsNotSoldByStore(int storeId);

    double findMaxXCoordinate();
    double findMaxYCoordinate();

    boolean isLocationOccupied(Point location);
    void createNewStore(String storeName, int ppk, Point location, Map<Integer, Integer> itemIdToPrice, String storeOwner);
    boolean isItemExistById(int id);
    //void addItemToSDM(int itemId, String itemName, String purchaseMethod, Map<Integer, Integer> storeIdToItemPrice);
    void addItemToSDM(String itemName, String purchaseMethod, Map<Integer, Integer> storeIdToItemPrice);
    boolean isDiscountNameExist(String discountName);
    void addDiscountToStore(int storeId, String discountName, int itemId, double itemAmount,
                            String discountType, List<OfferDTO> offerDTOList);

    List<StoreDTO> getAllStoreList(String userName);
    List<StoreDTO> getOwnerStoreList(String userName);
    List<ItemDTO> getAllItemList();
    List<OrderDTO> getOrdersHistory();
    List<PrivateOrderDTO> getCustomerOrdersHistory(int customerId);

    RegionDTO superDuperMarketToRegionDTO();

    void addFeedbackToStore(int storeId, String userName, LocalDate date, int rating, String feedback);
    List<FeedbackDTO> getVendorFeedbacks(String ownerName);
    int getItemsNumberInRegion();
    String getRegionOwnerName();
    String getStoreOwnerName(int storeId);
}
