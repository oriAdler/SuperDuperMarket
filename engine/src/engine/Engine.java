package engine;

import DTO.*;
import sdm.SuperDuperMarket;
import sdm.customer.Customer;

import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface Engine {
    void loadDataFromFile(String pathName);
    boolean validFileIsNotLoaded();

    SuperDuperMarket getSDM();
    List<StoreDTO> getAllStoreList();
    List<ItemDTO> getAllItemList();
    List<OrderDTO> getOrdersHistory();
    List<CustomerDTO> getAllCustomersList();
}
