package course.java.sdm.engine;

import course.java.sdm.DTO.*;
import course.java.sdm.SuperDuperMarket;
import course.java.sdm.SuperDuperMarketImpl;
import course.java.sdm.item.Item;
import course.java.sdm.jaxb.schema.generated.SDMItem;
import course.java.sdm.jaxb.schema.generated.SDMSell;
import course.java.sdm.jaxb.schema.generated.SDMStore;
import course.java.sdm.jaxb.schema.generated.SuperDuperMarketDescriptor;
import course.java.sdm.order.OrderDynamic;
import course.java.sdm.order.OrderStatic;
import course.java.sdm.store.Store;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EngineImpl implements Engine{

    static public final int MIN_RANGE = 1;
    static public final int MAX_RANGE = 50;
    private SuperDuperMarketImpl superDuperMarketImpl;
    private boolean validFileLoaded;

    public EngineImpl() {
        this.validFileLoaded = false;
    }

    @Override
    public void loadDataFromFile(String pathName) {
        SuperDuperMarketDescriptor SDMDescriptor = XmlFileHandler.generateJaxbClasses(pathName);
        XmlFileHandler.checkValidXmlFile(SDMDescriptor);
        convertJaxbGeneratedClassesToProjectClasses(SDMDescriptor);
        validFileLoaded = true;
    }

    @Override
    public boolean validFileIsNotLoaded() {
        return !validFileLoaded;
    }

    @Override
    public SuperDuperMarket getSDM() {
        return superDuperMarketImpl;
    }

    @Override
    public List<StoreDTO> getAllStoreList() {
        List<StoreDTO> storesDTOList = new ArrayList<>();
        superDuperMarketImpl.getStoreIdToStore()
                .forEach((id, store) -> storesDTOList.add(new StoreDTO(id,
                        store.getName(),
                        getItemsSoldByStore(store),
                        getStoreOrdersList(id),   //note: update relevant orders data
                        store.getPPK(),
                        store.getTotalDeliveryIncome()
                        )));
        return storesDTOList;
    }

    @Override
    public List<ItemDTO> getAllItemList() {
        List<ItemDTO> itemsDTOList = new ArrayList<>();
        superDuperMarketImpl.getItemIdToItem()
                .forEach((id, item) -> itemsDTOList.add(new ItemDTO(id,
                        item.getName(),
                        item.getPurchaseCategory(),
                        superDuperMarketImpl.getNumOfSellersById(id),
                        superDuperMarketImpl.getAveragePriceById(id),
                        superDuperMarketImpl.getNumOfSalesById(id))));
        return itemsDTOList;
    }

    @Override
    public List<OrderDTO> getOrdersHistory() {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        superDuperMarketImpl.getOrderIdToOrder()
                .forEach((key, value) -> orderDTOList.add(value.orderToOrderDTO(key)));
        return orderDTOList;
    }

    //Gets a store's id and returns a list of orders from this store.
    private List<OrderDTO> getStoreOrdersList(int storeId){
        List<OrderDTO> orderDTOList = new ArrayList<>();

        superDuperMarketImpl.getStoreIdToStore()
                .get(storeId)
                .getOrders()
                .forEach((orderId)-> {
                    OrderStatic currentOrder = superDuperMarketImpl.getOrderIdToOrder().get(orderId);
                    if(currentOrder instanceof OrderDynamic){
                        orderDTOList.add(
                                ((OrderDynamic)currentOrder)    //Casting to Order Dynamic
                                        .getStoreIdToOrder().get(storeId)   //Get store's part in order
                                        .orderToOrderDTO(orderId));     //Convert to DTO object
                    }
                    else{
                        orderDTOList.add(currentOrder.orderToOrderDTO(orderId));
                    }
                });
        return orderDTOList;
    }

    private List<ItemDTO> getItemsSoldByStore(Store store){
        List<ItemDTO> itemsDTOList = new ArrayList<>();
        store.getItemIdToPrice()
                .forEach((id, price) -> itemsDTOList.add(new ItemDTO(id,
                superDuperMarketImpl.getItemIdToItem().get(id).getName(),
                superDuperMarketImpl.getItemIdToItem().get(id).getPurchaseCategory(),
                -1, //note: update, inside store num of sellers is irrelevant
                price,
                store.getItemIdToNumberOfSales().get(id))));
        return itemsDTOList;
    }

    private void convertJaxbGeneratedClassesToProjectClasses(SuperDuperMarketDescriptor superDuperMarketDescriptor)
    {
        //Convert items list:
        Map<Integer, Item> itemsMap = new HashMap<>();
        List<SDMItem> SDMItemsList = superDuperMarketDescriptor.getSDMItems().getSDMItem();
        for(SDMItem item : SDMItemsList){
            itemsMap.put(item.getId(), new Item(item.getId(), item.getName(), item.getPurchaseCategory()));
        }
        //Convert stores list:
        Map<Integer, Store> storesMap = new HashMap<>();
        List<SDMStore> SDMStoresList = superDuperMarketDescriptor.getSDMStores().getSDMStore();
        for(SDMStore store : SDMStoresList){
            Map<Integer, Integer> storeItems = new HashMap<>();
            List<SDMSell> SDMPrices = store.getSDMPrices().getSDMSell();
            for (SDMSell sell : SDMPrices) {
                storeItems.put(sell.getItemId(), sell.getPrice());
            }
            storesMap.put(store.getId(), new Store(store.getId(), store.getName(), storeItems,
                    new Point(store.getLocation().getX(), store.getLocation().getY()), store.getDeliveryPpk()));
        }

        this.superDuperMarketImpl = new SuperDuperMarketImpl(storesMap, itemsMap);
    }
}
