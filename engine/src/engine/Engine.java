package engine;

import DTO.*;
import sdm.SuperDuperMarket;

import java.nio.file.Path;
import java.util.List;

public interface Engine {
    void loadDataFromFile(String pathName);
    boolean validFileIsNotLoaded();

    SuperDuperMarket getSDM();
    List<StoreDTO> getAllStoreList();
    List<ItemDTO> getAllItemList();
    List<OrderDTO> getOrdersHistory();

    void saveOrders(Path path);
    void loadOrders(Path path);
}
