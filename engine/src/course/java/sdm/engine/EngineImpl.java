package course.java.sdm.engine;

import course.java.sdm.DTO.*;
import course.java.sdm.SuperDuperMarket;
import course.java.sdm.SuperDuperMarketImpl;
import course.java.sdm.item.Item;
import course.java.sdm.jaxb.schema.generated.SDMItem;
import course.java.sdm.jaxb.schema.generated.SDMSell;
import course.java.sdm.jaxb.schema.generated.SDMStore;
import course.java.sdm.jaxb.schema.generated.SuperDuperMarketDescriptor;
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
        superDuperMarketImpl.getStoreIdToStore()
                .keySet()
                .forEach((storeId)->orderDTOList.addAll(getStoreOrdersList(storeId)));
        return orderDTOList;
    }

    private List<OrderDTO> getStoreOrdersList(int storeId){
        List<OrderDTO> orderDTOList = new ArrayList<>();
        superDuperMarketImpl.getStoreIdToStore()
                .get(storeId)
                .getOrders()
                .forEach((id)-> {
                    OrderStatic orderStatic =  superDuperMarketImpl.getOrderIdToOrder().get(id);
                    orderDTOList.add(new OrderDTO(id,
                            orderStatic.getDate(),
                            orderStatic.getStoreId(),
                            superDuperMarketImpl.getStoreIdToStore().get(id).getName(),
                            orderStatic.getNumOfItems(),
                            orderStatic.getItemsPrice(),
                            orderStatic.getDeliveryPrice(),
                            orderStatic.getTotalOrderPrice()));
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

    @Override
    public CartDTO summarizeStaticOrder(Map<Integer, Double> itemsToAddToCart, int storeId, Point customerLocation) {
        List<ItemExtendedDTO> itemExtendedDTOList = new ArrayList<>();
        Map<Integer, Item> itemsMap = superDuperMarketImpl.getItemIdToItem();
        Store store = superDuperMarketImpl.getStoreIdToStore().get(storeId);

        itemsToAddToCart.forEach((itemId, amount)->itemExtendedDTOList.add(new ItemExtendedDTO(
                itemId,
                itemsMap.get(itemId).getName(),
                itemsMap.get(itemId).getPurchaseCategory(),
                -1, //note: putting -1 to say value has no meaning
                store.getItemIdToPrice().get(itemId),
                amount,
                store.getName(),
                storeId)));

        return new CartDTO(itemExtendedDTOList,
                store.calculateDistanceFromStoreToCustomer(customerLocation),
                store.getPPK(),
                store.calculateDeliveryPrice(customerLocation));
    }

    @Override
    public CartDTO summarizeDynamicOrder(Map<Integer, Double> itemsToAddToCart, Point customerLocation) {
        List<ItemExtendedDTO> itemExtendedDTOList = new ArrayList<>();
        Map<Integer, Store> storesMap = superDuperMarketImpl.getStoreIdToStore();

        itemsToAddToCart.forEach((itemId, amount)->{
            double itemMinPrice = Double.MAX_VALUE;
            Integer storeId = -1;   //note: fix

            for(Map.Entry<Integer,Store> entry : storesMap.entrySet()){
                Store currentStore = entry.getValue();
                if(currentStore.getItemIdToPrice().containsKey(itemId) &&
                        currentStore.getItemIdToPrice().get(itemId)<itemMinPrice){
                    itemMinPrice = currentStore.getItemIdToPrice().get(itemId);
                    storeId = entry.getKey();
                }
            }

            itemExtendedDTOList.add(new ItemExtendedDTO(itemId,
                    superDuperMarketImpl.getItemIdToItem().get(itemId).getName(),
                    superDuperMarketImpl.getItemIdToItem().get(itemId).getPurchaseCategory(),
                    -1,
                    superDuperMarketImpl.getStoreIdToStore().get(storeId).getItemIdToPrice().get(itemId),
                    amount,
                    superDuperMarketImpl.getStoreIdToStore().get(storeId).getName(),
                    storeId));
        });
        //note: change all -1's to 1's, in order to prevent bugs
        return new CartDTO(itemExtendedDTOList, -1, -1,
                superDuperMarketImpl.calculateDeliveryPriceDynamicOrder(
                        itemExtendedDTOList, customerLocation));
    }

    @Override
    public boolean validFileIsNotLoaded() {
        return !validFileLoaded;
    }

    @Override
    public SuperDuperMarket getSDM() {
        return superDuperMarketImpl;
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
