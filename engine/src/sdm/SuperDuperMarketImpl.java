package sdm;

import DTO.*;
import engine.XmlFileHandler;
import exception.invalidLocationException;
import exception.invalidItemException;
import sdm.customer.Customer;
import sdm.item.PurchaseCategory;
import sdm.order.OrderDynamic;
import sdm.order.OrderStatic;
import sdm.item.Item;
import sdm.store.Store;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SuperDuperMarketImpl implements SuperDuperMarket {
    static public final int ONE_ITEM = 1;

    final private Map<Integer, Store> storeIdToStore;
    final private Map<Integer, Item> itemIdToItem;
    final private Map<Integer, OrderStatic> orderIdToOrder;
    final private Map<Integer, Customer> customerIdToCustomer;

    public SuperDuperMarketImpl(Map<Integer, Store> storeIdToStore, Map<Integer, Item> itemIdToItem, Map<Integer, Customer> customerIdToCustomer) {
        this.storeIdToStore = storeIdToStore;
        this.itemIdToItem = itemIdToItem;
        this.customerIdToCustomer = customerIdToCustomer;
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

    public Map<Integer, Customer> getCustomerIdToCustomer() {
        return customerIdToCustomer;
    }

    public int getNumOfSellersById(int id){
        return (int) storeIdToStore
                .values()
                .stream()
                .filter(store->store.getItemIdToPrice().containsKey(id))
                .count(); //NOTE: how to avoid explicit casting?
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

    private double calculateDeliveryPriceDynamicOrder(List<ItemExtendedDTO> itemsList, Point customerLocation){
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
        // Xml file handler contains inRange function
        if(!XmlFileHandler.inRange(location)){
            throw new invalidLocationException(
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
                throw new invalidLocationException(String.format(
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
    public void executeStaticOrder(CartDTO cart, Date date, int customerId) {
        Integer storeId = cart.getStoreId();

        //Add order to Super Duper Market System:
        OrderStatic orderStatic = new OrderStatic(date, storeId,
                storeIdToStore.get(storeId).getName(),
                cart.getItems().size(), cart.getTotalItemsPrice(),
                cart.getDeliveryPrice(), cart.getTotalOrderPrice(),
                true,
                cart.getItems().stream()    //Generate items list:
                        .map(this::itemDTOToItem)
                        .collect(Collectors.toList()));
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

        // Update customer data:
        customerIdToCustomer.get(customerId).addNewOrder(orderStatic, OrderStatic.getId());
    }

    @Override
    public void executeDynamicOrder(List<CartDTO> cartList, Date date, Integer customerId) {
        //Get all id's of all stores participating in order:
        Set<Integer> storesIdSet = cartList
                .stream()
                .map(CartDTO::getStoreId)
                .collect(Collectors.toSet());

        //Add dynamic order to Super Duper Market:
        Map<Integer, OrderStatic> orderIdToOrderStatic = new HashMap<>();
        cartList.forEach(cart-> {
            Integer storeId = cart.getStoreId();
            Store store = storeIdToStore.get(storeId);
            double deliveryPrice = store.calculateDeliveryPrice(customerIdToCustomer.get(customerId).getLocation());
            double itemsPrice = calculateItemsPriceFromStoreInCart(cart, storeId);

            orderIdToOrderStatic.put(storeId, new OrderStatic(date,
                    storeId,
                    store.getName(),
                    calculateNumOfItemsFromStoreInCart(cart, storeId),
                    itemsPrice,
                    deliveryPrice,
                    deliveryPrice + itemsPrice,
                    false,
                    cart.getItems().stream()    //Generate items list of current store:
                            .filter(item -> item.getStoreId().equals(storeId))
                            .map(this::itemDTOToItem)
                            .collect(Collectors.toList())));
        });

        // Add dynamic order to system:
        CartDTO cart = cartDTOListToCartDTO(cartList);
        OrderDynamic orderDynamic = new OrderDynamic(date,
                0,  // SDM get an arbitrary number as store id
                "Super Duper Market",
                cart.getItems().size(),
                cart.getTotalItemsPrice(),
                cart.getDeliveryPrice(),
                cart.getTotalOrderPrice(),
                new HashMap<>(orderIdToOrderStatic),
                true,
                cart.getItems().stream()    //Generate items list:
                        .map(this::itemDTOToItem)
                        .collect(Collectors.toList()));
        orderIdToOrder.put(OrderStatic.getId(), orderDynamic);

        //Update store's orders list & total delivery income:
        storesIdSet.forEach(storeId->{
            Store store = storeIdToStore.get(storeId);
            store.getOrders().add(OrderStatic.getId());
            store.setTotalDeliveryIncome(
                    store.getTotalDeliveryIncome() + orderIdToOrderStatic.get(storeId).getDeliveryPrice());
        });

        //Update store's sales counter:
        cart.getItems().forEach(item -> {
            Store store = storeIdToStore.get(item.getStoreId());

            store.getItemIdToNumberOfSales().replace(item.getId(),
                    store.getItemIdToNumberOfSales().get(item.getId()) + item.getNumOfSales());
        });

        // Update customer data:
        customerIdToCustomer.get(customerId).addNewOrder(orderDynamic, OrderStatic.getId());
    }

    // TODO: Check after making orders that numbers make sense.
    private CartDTO cartDTOListToCartDTO(List<CartDTO> cartDTOList){
        // Generate items list:
        List<ItemExtendedDTO> itemList = new ArrayList<>();
        cartDTOList.forEach(cart -> {
            itemList.addAll(new ArrayList<>(cart.getItems()));
        });

        double totalItemsPrice = 0.0;
        double deliveryPrice = 0.0;
        double totalOrderPrice = 0.0;
        for(CartDTO cart : cartDTOList){
            totalItemsPrice += cart.getTotalItemsPrice();
            deliveryPrice += cart.getDeliveryPrice();
            totalOrderPrice += cart.getTotalOrderPrice();
        }

        return new CartDTO(itemList, 0.0, 0.0, deliveryPrice,
                0, "Super Duper Market", 0, 0);
    }

    @Override
    public CartDTO summarizeStaticOrder(Map<Integer, Double> itemsToAddToCart, int storeId, Point customerLocation) {
        List<ItemExtendedDTO> itemExtendedDTOList = new ArrayList<>();
        Store store = storeIdToStore.get(storeId);

        itemsToAddToCart.forEach((itemId, amount)->itemExtendedDTOList.add(new ItemExtendedDTO(
                itemId,
                itemIdToItem.get(itemId).getName(),
                itemIdToItem.get(itemId).getPurchaseCategory().toString(),
                0, // Number of sellers has no meaning and therefor get an arbitrary number.
                store.getItemIdToPrice().get(itemId),
                amount,
                store.getName(),
                storeId)));

        return new CartDTO(itemExtendedDTOList,
                store.calculateDistanceFromStoreToCustomer(customerLocation),
                store.getPPK(),
                store.calculateDeliveryPrice(customerLocation), storeId, store.getName(),
                store.getLocation().x, store.getLocation().y);
    }

    @Override
    public List<CartDTO> summarizeDynamicOrder(Map<Integer, Double> itemsToAddToCart, Integer customerId) {
        List<CartDTO> cartDTOList = new ArrayList<>();
        List<ItemExtendedDTO> itemExtendedDTOList = new ArrayList<>();

        // Find the items with best prices:
        itemsToAddToCart.forEach((itemId, amount)->{
            double itemMinPrice = Double.MAX_VALUE;
            Integer storeId = 0;   // SDM gets an arbitrary store's id

            for(Map.Entry<Integer,Store> entry : storeIdToStore.entrySet()){
                Store currentStore = entry.getValue();
                if(currentStore.getItemIdToPrice().containsKey(itemId) &&
                        currentStore.getItemIdToPrice().get(itemId)<itemMinPrice){
                    itemMinPrice = currentStore.getItemIdToPrice().get(itemId);
                    storeId = entry.getKey();
                }
            }

            itemExtendedDTOList.add(new ItemExtendedDTO(itemId,
                    itemIdToItem.get(itemId).getName(),
                    itemIdToItem.get(itemId).getPurchaseCategory().toString(),
                    -1,
                    storeIdToStore.get(storeId).getItemIdToPrice().get(itemId),
                    amount,
                    storeIdToStore.get(storeId).getName(),
                    storeId));
        });

        // Generate a separate cart for each store:
        storeIdToStore.keySet().forEach(storeId->{
            List<ItemExtendedDTO> currentStoreItems = itemExtendedDTOList
                    .stream()
                    .filter(itemExtendedDTO -> itemExtendedDTO.getStoreId().equals(storeId))
                    .collect(Collectors.toList());
            if(!currentStoreItems.isEmpty()){
                Store currentStore = storeIdToStore.get(storeId);

                cartDTOList.add(new CartDTO(currentStoreItems,
                        calculateDistanceStoreToCustomer(storeId, customerId),
                        currentStore.getPPK(),
                        calculateDeliveryPrice(storeId, customerId), storeId,
                        currentStore.getName(),
                        currentStore.getLocation().x,
                        currentStore.getLocation().y));
            }
        });

        return cartDTOList;
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

    @Override
    public void addOrdersFromFileToSDM(Map<Integer, OrderStatic> orderIdToOrder) {
        orderIdToOrder.forEach((orderId, order) -> {
            this.orderIdToOrder.put(orderId, order);    //Add order to orders history

            if (order instanceof OrderDynamic) {    //Order is dynamic order
                Map<Integer, OrderStatic> storeIdToOrderStatic = ((OrderDynamic) order).getStoreIdToOrder();
                storeIdToOrderStatic.forEach((storeId, orderStatic) ->
                                updateStoreRevenue(storeIdToStore.get(orderStatic.getStoreId()), orderId, orderStatic));
            }
            else {   //Order is a static order
                updateStoreRevenue(storeIdToStore.get(order.getStoreId()), orderId, order);
            }
        });

        //Update order's running id number
        OrderStatic.setId(orderIdToOrder.size());
    }

    private void updateStoreRevenue(Store store, int orderId, OrderStatic orderStatic) {
        //Update store's orders list & total delivery income:
        store.getOrders().add(orderId);
        store.setTotalDeliveryIncome(store.getTotalDeliveryIncome() +
                orderStatic.getDeliveryPrice());

        //Update store's sales counter:
        orderStatic.getItemList().forEach(item -> {
            store.getItemIdToNumberOfSales().replace(item.getId(),
                    store.getItemIdToNumberOfSales().get(item.getId()) + item.getNumberOfSales());
        });
    }

    private Item itemDTOToItem(ItemDTO itemDTO){
        return new Item(itemDTO.getId(),
                itemDTO.getName(),
                itemDTO.getPurchaseCategory(),
                itemDTO.getNumOfSales());
    }

    @Override
    public void calculateDeliveryPrice(Integer storeId, Integer customerId, Consumer<Double> deliveryPrice) {
        deliveryPrice.accept(storeIdToStore.get(storeId).calculateDeliveryPrice(customerIdToCustomer.get(customerId).getLocation()));
    }

    public Double calculateDeliveryPrice(Integer storeId, Integer customerId){
        return storeIdToStore.get(storeId).calculateDeliveryPrice(customerIdToCustomer.get(customerId).getLocation());
    }

    public Double calculateDistanceStoreToCustomer(Integer storeId, Integer customerId){
        return this.storeIdToStore.get(storeId)
                .calculateDeliveryPrice(customerIdToCustomer.get(customerId).getLocation());
    }
}
