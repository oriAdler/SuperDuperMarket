package course.java.sdm.engine;

import course.java.sdm.DTO.*;
import course.java.sdm.SuperDuperMarket;
import course.java.sdm.exception.invalidCustomerLocationException;
import course.java.sdm.item.Item;
import course.java.sdm.item.PurchaseCategory;
import course.java.sdm.jaxb.schema.generated.SDMItem;
import course.java.sdm.jaxb.schema.generated.SDMSell;
import course.java.sdm.jaxb.schema.generated.SDMStore;
import course.java.sdm.jaxb.schema.generated.SuperDuperMarketDescriptor;
import course.java.sdm.order.Order;
import course.java.sdm.store.Store;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class EngineImpl implements Engine{
    private SuperDuperMarket superDuperMarket;
    private boolean validFileLoaded;
    static public final int MIN_RANGE = 1;
    static public final int MAX_RANGE = 50;

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
        superDuperMarket.getStores()
                .forEach((id, store) -> storesDTOList.add(new StoreDTO(id,
                        store.getName(),
                        getItemsSoldByStore(store),
                        getStoreOrdersList(id),   //note: update relevant orders data
                        store.getPPK(),
                        store.getTotalDeliveryIncome()
                        )));
        return storesDTOList;
    }

    private List<OrderDTO> getStoreOrdersList(int storeId){
        List<OrderDTO> orderDTOList = new ArrayList<>();
        superDuperMarket.getStores()
                .get(storeId)
                .getOrders()
                .forEach((orderId)-> {
                    Order order =  superDuperMarket.getOrders().get(orderId);
                    orderDTOList.add(new OrderDTO(orderId,
                            order.getDate(),
                            storeId,
                            superDuperMarket.getStores().get(storeId).getName(),
                            order.getNumOfItems(),
                            order.getItemsPrice(),
                            order.getDeliveryPrice(),
                            order.getTotalOrderPrice()));
                });
        return orderDTOList;
    }

    private List<ItemDTO> getItemsSoldByStore(Store store){
        List<ItemDTO> itemsDTOList = new ArrayList<>();
        store.getItems()
                .forEach((id, price) -> itemsDTOList.add(new ItemDTO(id,
                superDuperMarket.getItems().get(id).getName(),
                superDuperMarket.getItems().get(id).getPurchaseCategory(),
                -1, //note: update, inside store num of sellers is irrelevant
                price,
                store.getSalesCounter().get(id))));
        return itemsDTOList;
    }

    @Override
    public List<ItemDTO> getAllItemList() {
        List<ItemDTO> itemsDTOList = new ArrayList<>();
        superDuperMarket.getItems()
                .forEach((id, item) -> itemsDTOList.add(new ItemDTO(id,
                        item.getName(),
                        item.getPurchaseCategory(),
                        getNumOfSellersById(id),
                        getAveragePriceById(id),
                        getNumOfSalesById(id))));
        return itemsDTOList;
    }

    private int getNumOfSellersById(int id){
        return (int) superDuperMarket.getStores()
                .values()
                .stream()
                .filter(store->store.getItems().containsKey(id))
                .count();
    }   // note: how horrible is a cast to int?

    private double getNumOfSalesById(int id){
        return superDuperMarket.getStores()
                .values()
                .stream()
                .filter(store->store.getSalesCounter().containsKey(id))
                .mapToDouble(store->store.getSalesCounter().get(id))
                .sum();
    }

    private double getAveragePriceById(int id){
       OptionalDouble averagePrice = superDuperMarket.getStores()
                .values()
                .stream()
                .filter(store -> store.getItems().containsKey(id))
                .mapToDouble(store -> store.getItems().get(id))
                .average();
        return averagePrice.isPresent() ? averagePrice.getAsDouble() : -1;
    }

    @Override
    public List<OrderDTO> getOrdersHistory() {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        superDuperMarket.getStores()
                .keySet()
                .forEach((storeId)->orderDTOList.addAll(getStoreOrdersList(storeId)));
        return orderDTOList;
    }

    @Override
    public boolean storeExistById(int id) {
        return superDuperMarket.getStores().containsKey(id);
    }

    @Override
    public void checkCustomerLocationIsValid(Point location) {
        //note: it's weird to use here XmlFileHandler
        if(!XmlFileHandler.inRange(location)){
            throw new invalidCustomerLocationException(
                    "Invalid input: customer location must be between [1,50]");
        }
        else {
            //get only one element from stream, why to use list?
            List<Store> storeSameLocation = superDuperMarket.getStores()
                    .values()
                    .stream()
                    .filter(store -> store.getLocation().equals(location))
                    .limit(1)
                    .collect(Collectors.toList());
            if(!storeSameLocation.isEmpty()){
                throw new invalidCustomerLocationException(String.format(
                        "Invalid input: Store '%s' is already in location [%.0f,%.0f]\n" +
                                "Customer and store in same location is forbidden",
                        storeSameLocation.get(0).getName(), location.getX(), location.getY()));
            }
        }
    }

    @Override
    public boolean isItemSoldByStore(int storeId, int itemId) {
        return superDuperMarket.getStores().get(storeId).getItems().containsKey(itemId);
    }

    @Override
    public int getItemPriceInStore(int storeId, int itemId) {
        return superDuperMarket.getStores().get(storeId).getItems().get(itemId);
    }

    @Override
    public PurchaseCategory getItemPurchaseCategory(int itemId) {
        return superDuperMarket.getItems().get(itemId).getPurchaseCategory();
    }

    @Override
    public CartDTO summarizeStaticOrder(Map<Integer, Double> itemsToAddToCart, int storeId, Point customerLocation) {
        List<ItemExtendedDTO> itemExtendedDTOList = new ArrayList<>();
        Map<Integer, Item> itemsSDM = superDuperMarket.getItems();
        itemsToAddToCart.forEach((id, amount)->itemExtendedDTOList.add(new ItemExtendedDTO(id,
                itemsSDM.get(id).getName(),
                itemsSDM.get(id).getPurchaseCategory(),
                -1, //note: putting -1 to say value has no meaning
                superDuperMarket.getStores().get(storeId).getItems().get(id),
                amount)));
        return new CartDTO(itemExtendedDTOList,
                calculateDistanceFromStoreToCustomer(
                        superDuperMarket.getStores().get(storeId).getLocation(), customerLocation),
                superDuperMarket.getStores().get(storeId).getPPK());
    }

    @Override
    public void executeStaticOrder(CartDTO cart, Date date, int storeId) {
        //Add order to Super Duper Market System:
        Order order = new Order(date, storeId,
                superDuperMarket.getStores().get(storeId).getName(),
                cart.getItems().size(), cart.getTotalItemsPrice(),
                cart.getDeliveryPrice(), cart.getTotalOrderPrice());
        superDuperMarket.getOrders().put(Order.getId(), order);
        //Update store's orders list & total delivery income:
        Store store = superDuperMarket.getStores().get(storeId);
        store.getOrders().add(Order.getId());
        store.setTotalDeliveryIncome(store.getTotalDeliveryIncome()+order.getDeliveryPrice());
        //Update store's sales counter:
        cart.getItems()
                .forEach((item)->store.getSalesCounter()
                        .replace(item.getId(),
                                store.getSalesCounter().get(item.getId()) + item.getNumOfSales()));
    }

    //Calculate distance via pythagoras theorem [D = SQRT( (|X1-X2|)^2 + (|Y1-Y2|)^2)]
    private double calculateDistanceFromStoreToCustomer(Point store, Point customer){
        return Math.sqrt(Math.pow(
                Math.abs(store.getX()-customer.getX()), 2)
                + Math.pow(
                        Math.abs(store.getY()-customer.getY()), 2));
    }

    @Override
    public boolean isValidFileLoaded() {
        return validFileLoaded;
    }

    @Override
    public boolean isItemExistInSystem(int itemId) {
        return superDuperMarket.getItems().containsKey(itemId);
    }

    @Override
    public String getStoreNameById(int storeId) {
        return superDuperMarket.getStores().get(storeId).getName();
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

        this.superDuperMarket = new SuperDuperMarket(storesMap, itemsMap);
    }
}
