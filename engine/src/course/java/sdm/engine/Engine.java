package course.java.sdm.engine;

import course.java.sdm.DTO.ItemDTO;
import course.java.sdm.DTO.CartDTO;
import course.java.sdm.DTO.OrderDTO;
import course.java.sdm.DTO.StoreDTO;
import course.java.sdm.item.PurchaseCategory;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Engine {
    void loadDataFromFile(String pathName);
    List<StoreDTO> getAllStoreList();
    List<ItemDTO> getAllItemList();
    List<OrderDTO> getOrdersHistory();

    boolean storeExistById(int id);
    void checkCustomerLocationIsValid(Point location);
    boolean isItemSoldByStore(int storeId, int itemId);
    boolean isValidFileLoaded();
    boolean isItemExistInSystem(int itemId);

    int getItemPriceInStore(int storeId, int itemId);
    PurchaseCategory getItemPurchaseCategory(int itemId);
    String getStoreNameById(int storeId);

    CartDTO summarizeStaticOrder(Map<Integer, Double> itemsMap, int storeId, Point customerLocation);
    void executeStaticOrder(CartDTO cart, Date date, int storeId);
}
