package course.java.sdm;

import course.java.sdm.DTO.CartDTO;
import course.java.sdm.DTO.ItemExtendedDTO;
import course.java.sdm.engine.XmlFileHandler;
import course.java.sdm.exception.invalidCustomerLocationException;
import course.java.sdm.exception.invalidItemException;
import course.java.sdm.item.PurchaseCategory;
import course.java.sdm.order.OrderDynamic;
import course.java.sdm.order.OrderStatic;
import course.java.sdm.item.Item;
import course.java.sdm.store.Store;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SuperDuperMarketImpl implements SuperDuperMarket {
    static public final int ONE_ITEM = 1;

    final private Map<Integer, Store> storeIdToStore;
    final private Map<Integer, Item> itemIdToItem;
    final private Map<Integer, OrderStatic> orderIdToOrder;

    public SuperDuperMarketImpl(Map<Integer, Store> storeIdToStore, Map<Integer, Item> itemIdToItem) {
        this.storeIdToStore = storeIdToStore;
        this.itemIdToItem = itemIdToItem;
        this.orderIdToOrder = new HashMap<>();
    }

    public Map<Integer, Store> getStoreIdToStore() {
        return storeIdToStore;
    }

    public Map<Integer, Item> getItemIdToItem() {
        return itemIdToItem;
    }

    public Map<Integer, OrderStatic> getOrderIdToOrder() {
        return orderIdToOrder;
    }

    public int getNumOfSellersById(int id){
        return (int) storeIdToStore
                .values()
                .stream()
                .filter(store->store.getItemIdToPrice().containsKey(id))
                .count(); //note: explicit casting?
    }

    public double getNumOfSalesById(int id){
        return storeIdToStore
                .values()
                .stream()
                .filter(store->store.getItemIdToNumberOfSales().containsKey(id))
                .mapToDouble(store->store.getItemIdToNumberOfSales().get(id))
                .sum();
    }

    public double getAveragePriceById(int id){
        OptionalDouble averagePrice = storeIdToStore
                .values()
                .stream()
                .filter(store -> store.getItemIdToPrice().containsKey(id))
                .mapToDouble(store -> store.getItemIdToPrice().get(id))
                .average();
        return averagePrice.isPresent() ? averagePrice.getAsDouble() : -1;
    }

    public double calculateDeliveryPriceDynamicOrder(List<ItemExtendedDTO> itemsList, Point customerLocation){
        //Get stores Id without duplications:
        Set<Integer> storesIdList = itemsList
                .stream()
                .map(ItemExtendedDTO::getStoreId)
                .collect(Collectors.toSet());

        //Sum delivery price for each store:
        Map<Integer, Store> storesMap = storeIdToStore;
        return storesIdList
                .stream()
                .mapToDouble(storeId->storeIdToStore.get(storeId).calculateDeliveryPrice(customerLocation))
                .sum();
    }

    @Override
    public boolean isStoreExistById(int id) {
        return storeIdToStore.containsKey(id);
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
            List<Store> storeSameLocation = storeIdToStore
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
        return storeIdToStore.get(storeId).getItemIdToPrice().containsKey(itemId);
    }

    @Override
    public boolean itemNotExistInSystem(int itemId) {
        return !itemIdToItem.containsKey(itemId);
    }

    @Override
    public int getItemPriceInStore(int storeId, int itemId) {
        return storeIdToStore.get(storeId).getItemIdToPrice().get(itemId);
    }

    @Override
    public PurchaseCategory getItemPurchaseCategory(int itemId) {
        return itemIdToItem.get(itemId).getPurchaseCategory();
    }

    @Override
    public String getStoreNameById(int storeId) {
        return storeIdToStore.get(storeId).getName();
    }

    @Override
    public void removeItemFromStore(int storeId, int itemId) {
        //Item is the last one in the chosen store
        if(storeIdToStore.get(storeId).getItemIdToPrice().size() == 1){
            throw new invalidItemException(String.format(
                    "Can't complete operation because '%s' is the only item in '%s'",
                    itemIdToItem.get(itemId).getName(),
                    storeIdToStore.get(storeId).getName()));
        }
        //Item is sold by more than one store -> remove item
        else if(storeIdToStore.values()
                .stream()
                .filter(store -> store.getItemIdToPrice().containsKey(itemId))
                .count() > ONE_ITEM){
            storeIdToStore.get(storeId).getItemIdToPrice().remove(itemId);
        }
        else{   //Item is sold by only one store -> update UI
            throw new invalidItemException(String.format(
                    "Can't complete operation because '%s' is sold only by '%s'",
                    itemIdToItem.get(itemId).getName(),
                    storeIdToStore.get(storeId).getName()));
        }
    }

    @Override
    public void addItemToStore(int storeId, int itemId, int itemNewPrice){
        storeIdToStore.get(storeId).getItemIdToPrice().put(itemId, itemNewPrice);
        storeIdToStore.get(storeId).getItemIdToNumberOfSales().put(itemId, 0.0);
    }

    @Override
    public void updateItemPriceInStore(int storeId, int itemId, int itemNewPrice){
        storeIdToStore.get(storeId).getItemIdToPrice().replace(itemId, itemNewPrice);
    }

    @Override
    public String getItemNameById(int itemId){
        return itemIdToItem.get(itemId).getName();
    }

    @Override
    public void executeStaticOrder(CartDTO cart, Date date, int storeId) {
        //Add order to Super Duper Market System:
        OrderStatic orderStatic = new OrderStatic(date, storeId,
                storeIdToStore.get(storeId).getName(),
                cart.getItems().size(), cart.getTotalItemsPrice(),
                cart.getDeliveryPrice(), cart.getTotalOrderPrice(), true);
        orderIdToOrder.put(OrderStatic.getId(), orderStatic);

        //Update store's orders list & total delivery income:
        Store store = storeIdToStore.get(storeId);
        store.getOrders().add(OrderStatic.getId());
        store.setTotalDeliveryIncome(store.getTotalDeliveryIncome() + orderStatic.getDeliveryPrice());

        //Update store's sales counter:
        cart.getItems()
                .forEach((item)->store.getItemIdToNumberOfSales()
                        .replace(item.getId(),
                                store.getItemIdToNumberOfSales().get(item.getId()) + item.getNumOfSales()));
    }

    @Override
    public void executeDynamicOrder(CartDTO cart, Date date, Point customerLocation) {
        //Get all id's of all stores participating in order:
        Set<Integer> storesIdSet = cart.getItems().stream()
                .map(ItemExtendedDTO::getStoreId)
                .collect(Collectors.toSet());
        //Add dynamic order to Super Duper Market:
        Map<Integer, OrderStatic> orderStaticMap = new HashMap<>();
        storesIdSet.forEach(storeId-> {
            Store store = storeIdToStore.get(storeId);
            double deliveryPrice = store.calculateDeliveryPrice(customerLocation);
            double itemsPrice = calculateItemsPriceFromStoreInCart(cart, storeId);

            orderStaticMap.put(storeId, new OrderStatic(date,
                    storeId,
                    store.getName(),
                    calculateNumOfItemsFromStoreInCart(cart, storeId),
                    itemsPrice,
                    deliveryPrice,
                    deliveryPrice + itemsPrice,
                    false));
        });

        OrderDynamic orderDynamic = new OrderDynamic(date,
                -1,
                "Super Duper Market",
                cart.getItems().size(),
                cart.getTotalItemsPrice(),
                cart.getDeliveryPrice(),
                cart.getTotalOrderPrice(),
                new ArrayList<>(orderStaticMap.values()),
                true);
        orderIdToOrder.put(OrderStatic.getId(), orderDynamic);

        //Update store's orders list & total delivery income:
        storesIdSet.forEach(storeId->{
            Store store = storeIdToStore.get(storeId);
            store.getOrders().add(OrderStatic.getId());
            store.setTotalDeliveryIncome(
                    store.getTotalDeliveryIncome() + orderStaticMap.get(storeId).getDeliveryPrice());
        });

        //Update store's sales counter:
        cart.getItems().forEach(item -> {
            Store store = storeIdToStore.get(item.getStoreId());

            store.getItemIdToNumberOfSales().replace(item.getId(),
                    store.getItemIdToNumberOfSales().get(item.getId()) + item.getNumOfSales());
        });
    }

    private double calculateItemsPriceFromStoreInCart(CartDTO cart, int storeId){
        return cart.getItems().stream()
                .filter(item->item.getStoreId().equals(storeId))
                .mapToDouble(ItemExtendedDTO::getPriceSum)
                .sum();
    }

    private int calculateNumOfItemsFromStoreInCart(CartDTO cart, int storeId){
        return (int) cart.getItems().stream()
                .filter(item->item.getStoreId().equals(storeId))
                .count();
    }
}
