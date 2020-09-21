package sdm;

import DTO.*;
import engine.XmlFileHandler;
import exception.invalidLocationException;
import exception.invalidItemException;
import sdm.customer.Customer;
import sdm.discount.Discount;
import sdm.discount.Offer;
import sdm.item.PurchaseCategory;
import sdm.order.OrderDynamic;
import sdm.order.OrderStatic;
import sdm.item.Item;
import sdm.store.Store;

import java.awt.*;
import java.time.LocalDate;
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

            List<Discount> discountListToDelete = getDiscountListIncludingRemovedItem(
                    storeIdToStore.get(storeId).getDiscounts(), itemId);

            // Remove discounts including the item to be removed and generate an adequate message
            if(!discountListToDelete.isEmpty()){
                storeIdToStore.get(storeId).getDiscounts().removeAll(discountListToDelete);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.format("Item '%s' was removed from store '%s' successfully.\n",
                        itemIdToItem.get(itemId).getName(), storeIdToStore.get(storeId).getName()));
                stringBuilder.append(String.format("The following discounts including item '%s' were removed as well:\n",
                        itemIdToItem.get(itemId).getName()));
                discountListToDelete.forEach(discount -> stringBuilder.append(String.format("%s\n", discount.getName())));
                throw new invalidLocationException(stringBuilder.toString());
            }
        }
        else{   //Item is sold by only one store -> update UI
            throw new invalidItemException(String.format(
                    "Can't complete operation because '%s' is sold only by '%s'",
                    itemIdToItem.get(itemId).getName(),
                    storeIdToStore.get(storeId).getName()));
        }
    }

    // Gets a discount list and an item id, returns a list of discounts including the item.
    private List<Discount> getDiscountListIncludingRemovedItem(List<Discount> storeDiscounts, int removedItemId){
        List<Discount> discountListToDelete = new ArrayList<>();

        for(Discount discount : storeDiscounts){
            if(discount.getItemId() == removedItemId){
                discountListToDelete.add(discount);
            }
            else{
                for(Offer offer : discount.getOfferList()){
                    if(offer.getItemId() == removedItemId){
                        discountListToDelete.add(discount);
                        break;
                    }
                }
            }
        }

        return discountListToDelete;
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
    public void executeStaticOrder(CartDTO cart, LocalDate date, int customerId) {
        Integer storeId = cart.getStoreId();

        //Add order to Super Duper Market System:
        OrderStatic orderStatic = new OrderStatic(date, storeId,
                storeIdToStore.get(storeId).getName(),
                cart.getItems().size(), cart.getTotalItemsPrice(),
                cart.getDeliveryPrice(), cart.getTotalOrderPrice(),
                true,
                cart.getItems().stream()    //Generate items list:
                        .map(this::itemDTOToItem)
                        .collect(Collectors.toList()),
                customerId);
        orderStatic.setCart(cart);  // Save cart for showing details later
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
    public void executeDynamicOrder(List<CartDTO> cartList, LocalDate date, Integer customerId) {
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

            OrderStatic orderStatic = new OrderStatic(date, storeId,
                    store.getName(),
                    calculateNumOfItemsFromStoreInCart(cart, storeId),
                    itemsPrice,
                    deliveryPrice,
                    deliveryPrice + itemsPrice,
                    false,
                    cart.getItems().stream()    //Generate items list of current store:
                            .filter(item -> item.getStoreId().equals(storeId))
                            .map(this::itemDTOToItem)
                            .collect(Collectors.toList()),
                    customerId);
            orderStatic.setCart(cart);  // Save cart for showing details later
            orderIdToOrderStatic.put(storeId, orderStatic);
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
                        .collect(Collectors.toList()),
                customerId);
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
    public CartDTO summarizeStaticOrder(Map<Integer, Double> itemsToAddToCart, List<OfferDTO> offersToAddToCart,
                                        int storeId, Integer customerId) {
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
                storeId,
                false)));

        if(offersToAddToCart!=null && !offersToAddToCart.isEmpty()) {
            offersToAddToCart.forEach(offerDTO -> itemExtendedDTOList.add(new ItemExtendedDTO(
                    offerDTO.getItemId(),
                    itemIdToItem.get(offerDTO.getItemId()).getName(),
                    itemIdToItem.get(offerDTO.getItemId()).getPurchaseCategory().toString(),
                    0,
                    offerDTO.getPrice(),
                    offerDTO.getAmount(),
                    store.getName(),
                    storeId,
                    true)));
        }

        return new CartDTO(itemExtendedDTOList,
                store.calculateDistanceFromStoreToCustomer(customerIdToCustomer.get(customerId).getLocation()),
                store.getPPK(),
                store.calculateDeliveryPrice(customerIdToCustomer.get(customerId).getLocation()), storeId, store.getName(),
                store.getLocation().x, store.getLocation().y);
    }

    @Override
    public List<CartDTO> summarizeDynamicOrder(Map<Integer, Double> itemsToAddToCart, List<OfferDTO> offersToAddToCart,
                                               Integer customerId) {
        List<CartDTO> cartDTOList = new ArrayList<>();
        List<ItemExtendedDTO> itemExtendedDTOList = new ArrayList<>();

        // Find the items with best prices:
        itemsToAddToCart.forEach((itemId, amount)->{
            int storeId = findStoreIdWithCheapestPriceForItem(itemId);

            itemExtendedDTOList.add(new ItemExtendedDTO(itemId,
                    itemIdToItem.get(itemId).getName(),
                    itemIdToItem.get(itemId).getPurchaseCategory().toString(),
                    -1,
                    storeIdToStore.get(storeId).getItemIdToPrice().get(itemId),
                    amount,
                    storeIdToStore.get(storeId).getName(),
                    storeId,
                    false));
        });

        if(offersToAddToCart!=null && !offersToAddToCart.isEmpty()){
            offersToAddToCart.forEach(offer -> itemExtendedDTOList.add(new ItemExtendedDTO(
                    offer.getItemId(),
                    itemIdToItem.get(offer.getItemId()).getName(),
                    itemIdToItem.get(offer.getItemId()).getPurchaseCategory().toString(),
                    0,
                    offer.getPrice(),
                    offer.getAmount(),
                    storeIdToStore.get(offer.getStoreId()).getName(),
                    offer.getStoreId(),
                    true)));
        }

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
        return storeIdToStore.get(storeId)
                .calculateDeliveryPrice(customerIdToCustomer.get(customerId).getLocation());
    }

    public Double calculateDistanceStoreToCustomer(Integer storeId, Integer customerId){
        return this.storeIdToStore.get(storeId)
                .calculateDistanceFromStoreToCustomer(customerIdToCustomer.get(customerId).getLocation());
    }

    // Gets an order number and returns a list of 'CartDTO' objects representing the order.
    public List<CartDTO> getDetailedOrder(Integer orderId){
        List<CartDTO> cartDTOList = new ArrayList<>();

        if(orderIdToOrder.get(orderId) instanceof OrderDynamic){
            OrderDynamic orderDynamic = (OrderDynamic)orderIdToOrder.get(orderId);

            for(OrderStatic orderStatic : orderDynamic.getStoreIdToOrder().values()){
                cartDTOList.add(orderStatic.getCart());
            }
        }
        else{   // order instance of OrderStatic
            OrderStatic orderStatic = orderIdToOrder.get(orderId);

            cartDTOList.add(orderStatic.getCart());
        }

        return cartDTOList;
    }

    public List<DiscountDTO> getStoreDiscounts(Map<Integer, Double> itemIdToAmount, Integer storeId){
        List<DiscountDTO> discountDTOList = new ArrayList<>();
        List<Discount> discountList = storeIdToStore.get(storeId).getDiscounts();

        // For each item in Map:
        itemIdToAmount.keySet().forEach(itemId->{
            for(Discount discount : discountList){
                if(discount.getItemId() == itemId &&
                        discount.getAmount() <= itemIdToAmount.get(itemId)){
                    discountDTOList.add(discountToDiscountDTO(discount, storeId));
                }
            }
        });

        return discountDTOList;
    }

    // Get discounts for static order:
    private DiscountDTO discountToDiscountDTO(Discount discount, int storeId){
        List<OfferDTO> offerDTOList = new ArrayList<>();
        discount.getOfferList().forEach(offer -> offerDTOList.add(new OfferDTO(offer.getItemId(),
                itemIdToItem.get(offer.getItemId()).getName(),
                offer.getAmount(),
                offer.getPrice(),
                storeId)));

        return new DiscountDTO(discount.getName(),
                discount.getItemId(),
                itemIdToItem.get(discount.getItemId()).getName(),
                discount.getAmount(),
                discount.getOperator(),
                offerDTOList);
    }

    // Get discounts for Dynamic order:
    // As only item's id is provided, this function finds the store
    // with the cheapest price in order to add adequate discounts.
    public List<DiscountDTO> getDiscounts(Map<Integer, Double> itemIdToAmount){
        List<DiscountDTO> discountDTOList = new ArrayList<>();

        // For each item in Map:
        itemIdToAmount.keySet().forEach(itemId->{
            int storeId = findStoreIdWithCheapestPriceForItem(itemId);
            List<Discount> discountList = storeIdToStore.get(storeId).getDiscounts();

            for(Discount discount : discountList){
                if(discount.getItemId() == itemId &&
                        discount.getAmount() <= itemIdToAmount.get(itemId)){
                    discountDTOList.add(discountToDiscountDTO(discount, storeId));
                }
            }
        });

        return discountDTOList;
    }

    private int findStoreIdWithCheapestPriceForItem(int itemId){
        // Find the store with best prices:
            double itemMinPrice = Double.MAX_VALUE;
            int storeId = 0;   // SDM gets an arbitrary store's id

            for(Store store : storeIdToStore.values()){
                if(store.getItemIdToPrice().containsKey(itemId) &&
                        store.getItemIdToPrice().get(itemId) < itemMinPrice){
                    itemMinPrice = store.getItemIdToPrice().get(itemId);
                    storeId = store.getId();
                }
            }

            return storeId;
    }

    public List<ItemDTO> getStoreItems(int storeId) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        Store store = storeIdToStore.get(storeId);

        store.getItemIdToPrice().keySet().forEach(itemId->{
            Item item = itemIdToItem.get(itemId);

            itemDTOList.add(new ItemDTO(itemId,
                    item.getName(),
                    item.getPurchaseCategory().toString(),
                    getNumOfSellersById(itemId),
                    store.getItemIdToPrice().get(itemId),
                    getNumOfSalesById(itemId)));
        });

        return itemDTOList;
    }

    public List<ItemDTO> getItemsNotSoldByStore(int storeId){
        List<ItemDTO> itemDTOList = new ArrayList<>();
        Store store = storeIdToStore.get(storeId);

        itemIdToItem.keySet().forEach(itemId->{
            if(!store.getItemIdToPrice().containsKey(itemId)){
                Item item = itemIdToItem.get(itemId);

                itemDTOList.add(new ItemDTO(itemId,
                        item.getName(),
                        item.getPurchaseCategory().toString(),
                        getNumOfSellersById(itemId),
                        -1, // store doesn't sell this item therefor price isn't relevant
                        getNumOfSalesById(itemId)));
            }
        });

        return itemDTOList;
    }

    public double findMaxXCoordinate(){
        double storeMaxXCoordinate, customerMaxXCoordinate;

        // Find store's max X coordinate:
        OptionalDouble storeMaxXCoordinateOD = storeIdToStore.values()
                .stream()
                .mapToDouble(store->store.getLocation().getX())
                .max();
        storeMaxXCoordinate = storeMaxXCoordinateOD.isPresent() ? storeMaxXCoordinateOD.getAsDouble() : 0;

        // Find customer max X coordinate:
        OptionalDouble customerMaxXCoordinateOD = customerIdToCustomer.values()
                .stream()
                .mapToDouble(customer->customer.getLocation().getX())
                .max();
        customerMaxXCoordinate = customerMaxXCoordinateOD.isPresent() ? customerMaxXCoordinateOD.getAsDouble() : 0;

        return Math.max(storeMaxXCoordinate, customerMaxXCoordinate);
    }

    public double findMaxYCoordinate(){
        double storeMaxYCoordinate, customerMaxYCoordinate;

        // Find store's max X coordinate:
        OptionalDouble storeMaxYCoordinateOD = storeIdToStore.values()
                .stream()
                .mapToDouble(store->store.getLocation().getY())
                .max();
        storeMaxYCoordinate = storeMaxYCoordinateOD.isPresent() ? storeMaxYCoordinateOD.getAsDouble() : 0;

        // Find customer max X coordinate:
        OptionalDouble customerMaxYCoordinateOD = customerIdToCustomer.values()
                .stream()
                .mapToDouble(customer->customer.getLocation().getY())
                .max();
        customerMaxYCoordinate = customerMaxYCoordinateOD.isPresent() ? customerMaxYCoordinateOD.getAsDouble() : 0;

        return Math.max(storeMaxYCoordinate, customerMaxYCoordinate);
    }

    public boolean isLocationOccupied(Point newLocation){
        for(Store store : storeIdToStore.values()){
            if(newLocation.equals(store.getLocation())){
                return true;
            }
        }
        for(Customer customer : customerIdToCustomer.values()){
            if(newLocation.equals(customer.getLocation())){
                return true;
            }
        }

        return false;
    }

}
