package engine;

import DTO.*;
import sdm.SuperDuperMarket;

import java.io.InputStream;
import java.util.List;

public interface Engine {
    void loadDataFromFile(InputStream fileInputStream, String regionName);
    boolean isRegionNameExist(String regionName);

    void loadDataFromFile(String pathName);
    boolean validFileIsNotLoaded();

    //SuperDuperMarket getSDM();
    //List<StoreDTO> getAllStoreList();
    //List<ItemDTO> getAllItemList();
    //List<OrderDTO> getOrdersHistory();
    //List<CustomerDTO> getAllCustomersList();
}
