package course.java.sdm.userInterface;

import course.java.sdm.DTO.ItemDTO;
import course.java.sdm.DTO.CartDTO;
import course.java.sdm.DTO.OrderDTO;
import course.java.sdm.DTO.StoreDTO;
import course.java.sdm.engine.Engine;
import course.java.sdm.engine.EngineImpl;
import course.java.sdm.exception.invalidCustomerLocationException;
import course.java.sdm.exception.invalidItemException;
import course.java.sdm.input.Input;
import course.java.sdm.item.PurchaseCategory;
import course.java.sdm.message.Messenger;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class UI {
    static private final int MIN_CHOICE = 1;

    static public void run(){
        boolean superDuperMarketIsOpen = true;
        Engine engine = new EngineImpl();
        System.out.println(Messenger.WELCOME_MESSAGE);
        while(superDuperMarketIsOpen){
            System.out.println(Messenger.generateMenu(Messenger.getMainMenu()));
            int userChoice = getUserChoiceMainMenu();
            switch(userChoice){
                case 1:
                    loadDataFromFile(engine);
                    break;
                case 2:
                    displayStores(engine);
                    break;
                case 3:
                    displayAllItems(engine);
                    break;
                case 4:
                    makeOrder(engine);
                    break;
                case 5:
                    showOrdersHistory(engine);
                    break;
                case 6:
                    updateStoreItems(engine);
                    break;
                case 7:
                    System.out.println(Messenger.EXIT);
                    System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
                    superDuperMarketIsOpen = false;
                    break;
                default:
                    System.out.println(Messenger.UNKNOWN_ERROR);
                    System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
            }
        }
    }

    static private int getUserChoiceMainMenu(){
        return Input.getOnePositiveIntegerInRange(MIN_CHOICE, Messenger.getMainMenu().length,
                String.format("Invalid input: number must be between %d-%d",
                        MIN_CHOICE, Messenger.getMainMenu().length));
    }

    static private void loadDataFromFile(Engine engine){
        System.out.println(Messenger.ENTER_FILE_PATH);
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        try{
            engine.loadDataFromFile(filePath);
            System.out.println(Messenger.FILE_LOADED_SUCCESSFULLY);
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
        }
    }

    static private void displayStores(Engine engine){
        if(engine.validFileIsNotLoaded()){
            System.out.println(Messenger.VALID_FILE_NOT_LOADED + "display stores");
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
        }
        else{
            List<StoreDTO> storesList = engine.getAllStoreList();
            storesList.stream()
                    .map(UI::storeDTOToString)
                    .forEach(System.out::println);
        }
    }

    static private void displayAllItems(Engine engine){
        if(engine.validFileIsNotLoaded()){
            System.out.println(Messenger.VALID_FILE_NOT_LOADED + "display all items");
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
        }
        else {
            List<ItemDTO> itemsList = engine.getAllItemList();
            System.out.println(Messenger.SUPER_DUPER_MARKET);
            itemsList.stream()
                    .map((ItemDTO item) -> itemDTOToString(item,false))
                    .forEach(System.out::println);
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
        }
    }

    static private void makeOrder(Engine engine){
        if(engine.validFileIsNotLoaded()){
            System.out.println(Messenger.VALID_FILE_NOT_LOADED + "make an order");
        }
        else {
            System.out.println(Messenger.generateMenu(Messenger.getChooseOrderMethodMenu()));
            Integer Choice = Input.getOnePositiveIntegerInRange(MIN_CHOICE, Messenger.getCompleteOrderMenu().length,
                    String.format("Invalid input: number must be between %d-%d",
                            MIN_CHOICE, Messenger.getCompleteOrderMenu().length));
            if(Choice.equals(1)){
                makeStaticOrder(engine);
            }
            else{   //Choice.equals(2)
                makeDynamicOrder(engine);
            }
        }
        System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
    }

    static private void updateStoreItems(Engine engine){
        if(engine.validFileIsNotLoaded()){
            System.out.println(Messenger.VALID_FILE_NOT_LOADED + "update store's products");
        }
        else{
            displayStoresMinimalData(engine.getAllStoreList());
            int storeId = chooseStore(engine);

            System.out.println(Messenger.generateMenu(Messenger.getUpdateStoreMenu()));
            int Choice = Input.getOnePositiveIntegerInRange(MIN_CHOICE, Messenger.getUpdateStoreMenu().length,
                    String.format("Invalid input: number must be between %d-%d",
                            MIN_CHOICE, Messenger.getUpdateStoreMenu().length));
            switch(Choice){
                case 1:
                    removeItemFromStore(engine, storeId);
                    break;
                case 2:
                    addItemToStore(engine, storeId);
                    break;
                case 3:
                    updateItemPrice(engine, storeId);
                    break;
                default:
                    System.out.println(Messenger.UNKNOWN_ERROR);
                    System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
            }
        }
        System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
    }

    static private void makeStaticOrder(Engine engine){
        displayStoresMinimalData(engine.getAllStoreList());
        int storeId = chooseStore(engine);

        System.out.println(Messenger.CHOOSE_DATE);
        Date orderDate = Input.getDate();   //note: get date is buggy

        Point customerLocation = getUserLocation(engine);

        Map<Integer, Double> itemsMap = chooseItems(engine, storeId);

        if(itemsMap.isEmpty()){
            System.out.println("No items were chosen");
        }
        else{
            CartDTO cart = engine.summarizeStaticOrder(itemsMap, storeId, customerLocation);
            displayStaticOrderSummary(cart);
            System.out.println(Messenger.generateMenu(Messenger.getCompleteOrderMenu()));
            Integer Choice = Input.getOnePositiveIntegerInRange(MIN_CHOICE, Messenger.getCompleteOrderMenu().length,
                    String.format("Invalid input: number must be between %d-%d",
                            MIN_CHOICE, Messenger.getCompleteOrderMenu().length));
            if(Choice.equals(1)){
                engine.getSDM().executeStaticOrder(cart, orderDate, storeId);
                System.out.println("Thank you for buying " + engine.getSDM().getStoreNameById(storeId) + "!");
            }
            else{
                System.out.println("You chose to cancel to order.. hope to see you again soon");
            }
        }
    }

    static private void makeDynamicOrder(Engine engine){
        System.out.println(Messenger.CHOOSE_DATE);
        Date orderDate = Input.getDate();

        Point customerLocation = getUserLocation(engine);

        Map<Integer, Double> itemsMap = chooseItems(engine);

        if(itemsMap.isEmpty()){
            System.out.println("No items were chosen");
        }
        else{
            CartDTO cart = engine.summarizeDynamicOrder(itemsMap, customerLocation);
            displayDynamicOrderSummary(cart);
            System.out.println(Messenger.generateMenu(Messenger.getCompleteOrderMenu()));
            Integer Choice = Input.getOnePositiveIntegerInRange(MIN_CHOICE, Messenger.getCompleteOrderMenu().length,
                    String.format("Invalid input: number must be between %d-%d",
                            MIN_CHOICE, Messenger.getCompleteOrderMenu().length));
            if(Choice.equals(1)){
                engine.getSDM().executeDynamicOrder(cart, orderDate, customerLocation);
                System.out.println("Thank you for buying Super Duper Market!");
            }
            else{
                System.out.println("You chose to cancel to order.. hope to see you again soon");
            }
        }
    }

    static private void showOrdersHistory(Engine engine){
        if(engine.validFileIsNotLoaded()){
            System.out.println(Messenger.VALID_FILE_NOT_LOADED + "see orders history");
        }
        else{
            System.out.println(
                    engine.getOrdersHistory().isEmpty() ?
                            "No orders have been placed" : "---Orders---" + "\n" +
                            engine.getOrdersHistory()
                            .stream()
                            .map(order->orderDTOToString(order, false))
                            .collect(Collectors.joining("\n")));
        }
        System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
    }

    static private void displayStaticOrderSummary(CartDTO cart){
        System.out.println("---Order Summary---");
        System.out.println("--Cart--");
        cart.getItems().stream().map((item -> "{Id: " + item.getId() + ", "
                + "Name: " + item.getName() + ", "
                + "Purchase category: " + item.getPurchaseCategory() + ", "
                + String.format("Price: %.2f", item.getPrice()) + ", "
                + (item.getPurchaseCategory().equals(PurchaseCategory.WEIGHT) ?
                String.format("Weight: %.2f", item.getNumOfSales()) :
                String.format("Quantity: %.0f", item.getNumOfSales()))
                + ", "   //note: num of sales not such a good name
                + String.format("Total: %.2f", item.getPriceSum()) + "}"))
                .forEach(System.out::println);
        System.out.println(Messenger.LINE_SEPARATOR);
        System.out.println(String.format("Distance from store: %.2f", cart.getDistanceFromStoreToCustomer()) + "\n"
                + String.format("PPK: %.2f",cart.getPPK())+ "\n"
                + String.format("Delivery price: %.2f", cart.getDeliveryPrice()) + "\n"
                + String.format("Total price: %.2f", cart.getTotalOrderPrice()));
        System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
    }

    static private void displayDynamicOrderSummary(CartDTO cart){
        System.out.println("---Order Summary---");
        System.out.println("--Cart--");
        cart.getItems().stream().map((item -> "{Id: " + item.getId() + ", "
                + "Name: " + item.getName() + ", "
                + "Purchase category: " + item.getPurchaseCategory() + ", "
                + String.format("Price: %.2f", item.getPrice()) + ", "
                + (item.getPurchaseCategory().equals(PurchaseCategory.WEIGHT) ?
                String.format("Weight: %.2f", item.getNumOfSales()) :
                String.format("Quantity: %.0f", item.getNumOfSales()))
                + ", "   //note: num of sales not such a good name
                + String.format("Total: %.2f", item.getPriceSum()) + ", " +
                "(Store: " + item.getStoreName() + ", " + "Store Id: " + item.getStoreId() + ")}"))
                .forEach(System.out::println);
        System.out.println(Messenger.LINE_SEPARATOR);
        System.out.println(String.format("Delivery price: %.2f", cart.getDeliveryPrice()) + "\n"
                + String.format("Total price: %.2f", cart.getTotalOrderPrice()));
        System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
    }

    //Chose items for static order, store Id is needed (function overloading)
    static private Map<Integer, Double> chooseItems(Engine engine, int storeId){
        Scanner scanner = new Scanner(System.in);
        Map<Integer, Double> itemsMap = new HashMap<>();

        do{
            System.out.println("---" + engine.getSDM().getStoreNameById(storeId) + "---");
            displayAllItemInStore(engine, storeId);

            System.out.println(Messenger.CONTINUE_ORDER);
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
            String userInputString = scanner.nextLine().trim();
            //Check if user chose to finish choosing by entering "q"/"Q"
            if(userInputString.equals("q") || userInputString.equals("Q")){
                return itemsMap;
            }
            else{   //User continues to chose items by ID
                if(Input.isPositiveInteger(userInputString)){
                    int itemId = Integer.parseInt(userInputString);

                    if(engine.getSDM().itemNotExistInSystem(itemId)) {
                        System.out.println(Messenger.ITEM_NOT_EXIST_SYSTEM);
                    }
                    else if(!engine.getSDM().isItemSoldByStore(storeId, itemId)) {
                            System.out.println(Messenger.ITEM_NOT_SOLD_BY_STORE);
                    }
                    else{   //User entered a valid item ID, continue to chose amount
                        choseAmountOfItem(engine, itemId, itemsMap);
                    }
                }
            }
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
        } while(true);  //A return statement is inside the do-while loop
    }

    //Chose items for dynamic order (function overloading)
    static private Map<Integer, Double> chooseItems(Engine engine){
        Scanner scanner = new Scanner(System.in);
        Map<Integer, Double> itemsMap = new HashMap<>();

        do{
            displayAllItems(engine);

            System.out.println(Messenger.CONTINUE_ORDER);
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
            String userInputString = scanner.nextLine().trim();
            //Check if user chose to finish choosing by entering "q"/"Q"
            if(userInputString.equals("q") || userInputString.equals("Q")){
                return itemsMap;
            }
            else{   //User continues to chose items by ID
                if(Input.isPositiveInteger(userInputString)){
                    int itemId = Integer.parseInt(userInputString);

                    if(engine.getSDM().itemNotExistInSystem(itemId)) {
                        System.out.println(Messenger.ITEM_NOT_EXIST_SYSTEM);
                    }
                    else{   //User entered a valid item ID, continue to chose amount
                        choseAmountOfItem(engine, itemId, itemsMap);
                    }
                }
            }
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
        } while(true);  //A return statement is inside the do-while loop
    }

    static private void choseAmountOfItem(Engine engine, Integer itemId, Map<Integer, Double> itemsMap){
        PurchaseCategory category = engine.getSDM().getItemPurchaseCategory(itemId);
        System.out.println(String.format("Please enter item's %s\n"
                + (category.equals(PurchaseCategory.QUANTITY) ?
                "[Units]" : "[Kg]"), category.getName()));
        if(category.equals(PurchaseCategory.QUANTITY)) {
            int quantity = Input.getOnePositiveInteger();
            addItemToOrder(itemsMap, itemId, quantity);
        }
        else {  //category.equals(PurchaseCategory.WEIGHT)
            double weight = Input.getOnePositiveDouble();
            addItemToOrder(itemsMap, itemId, weight);
        }
    }

    static private void addItemToOrder(Map<Integer, Double> itemsMap, int itemId, int quantity){
        if(itemsMap.containsKey(itemId)){
            itemsMap.replace(itemId, itemsMap.get(itemId) + quantity);
        }
        else{
            itemsMap.put(itemId, (double) quantity);    // note: maybe another way?
        }
    }

    static private void addItemToOrder(Map<Integer, Double> itemsMap, int itemId, double weight){
        if(itemsMap.containsKey(itemId)){
            itemsMap.replace(itemId, itemsMap.get(itemId) + weight);
        }
        else{
            itemsMap.put(itemId, weight);
        }
    }

    static private void displayAllItemInStore(Engine engine, int storeId){
        engine.getAllItemList().stream()
                .map(itemDTO -> "{Id: " + itemDTO.getId() + ", "
                        + "Name: " + itemDTO.getName() + ", "
                        + "Purchase category: " + itemDTO.getPurchaseCategory() + ", "
                        + "Price: " + (engine.getSDM().isItemSoldByStore(storeId, itemDTO.getId()) ?
                        engine.getSDM().getItemPriceInStore(storeId, itemDTO.getId()) :
                        "Not sold by this store") + "}")
                .forEach(System.out::println);
        System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
    }

    static private Point getUserLocation(Engine engine){
        System.out.println(Messenger.CHOOSE_LOCATION);
        boolean isValidLocation;
        Point customerLocation;

        do{
            int[] inputArray = Input.getTwoIntegers();
            customerLocation = new Point(inputArray[0], inputArray[1]);
            try{
                engine.getSDM().checkCustomerLocationIsValid(customerLocation);
                isValidLocation = true;
            }
            catch (invalidCustomerLocationException exception){
                System.out.println(exception.getMessage());
                isValidLocation = false;
            }
        } while(!isValidLocation);

        return customerLocation;
    }

    static private int chooseStore(Engine engine){
        System.out.println(Messenger.CHOOSE_STORE);
        boolean isValidChoice;
        int userChoice = 0;

        do{
            userChoice = Input.getOnePositiveInteger();
            if(!engine.getSDM().isStoreExistById(userChoice)){
                System.out.println(String.format(
                        "Invalid choice: store with id '%d' does not exist", userChoice));
                isValidChoice = false;
            }
            else{
                isValidChoice = true;
            }
        } while(!isValidChoice);

        return userChoice;
    }

    static private void displayStoresMinimalData(List<StoreDTO> storesList){
        System.out.println(Messenger.SUPER_DUPER_MARKET);
        storesList
                .stream()
                .map(storeDTO -> "{" + "Name: " + storeDTO.getName() + ", " +
                        "Id: " + storeDTO.getId() + ", " +
                        "PPK: " + storeDTO.getPPK() + "}")
                .forEach(System.out::println);
        System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
    }

    static private String itemDTOToString(ItemDTO item, boolean inStore){
        return "{" +
                "Id: " + item.getId() +
                ", Name: '" + item.getName() + '\'' +
                ", Purchase category: " + item.getPurchaseCategory() +
                (inStore ? "" : ", Number of sellers: " + item.getNumOfSellers()) +
                ", Price: " + String.format("%.2f", item.getPrice()) +
                ", Number of sales: " + (item.getPurchaseCategory().equals(PurchaseCategory.QUANTITY) ?
                String.format("%.0f", item.getNumOfSales()) + " Units" : String.format("%.2f", item.getNumOfSales()) + " Kg") +
                '}';
    }

    static private String storeDTOToString(StoreDTO store){
        return "---" + store.getName() + "---" + "\n" +
                "Id: " + store.getId() + "\n" +
                "--Items-- \n" +
                store.getItems()
                .stream()
                .map((ItemDTO item) -> itemDTOToString(item,true))
                .collect(Collectors.joining("\n")) +
                "\n" + Messenger.LINE_SEPARATOR_NEW_LINE +
                "Orders: \n" +
                (store.getOrders().isEmpty() ? "No orders have been placed" :
                store.getOrders()
                        .stream()
                        .map((OrderDTO order)->orderDTOToString(order, true))
                        .collect(Collectors.joining("\n"))) +
                "\n" + Messenger.LINE_SEPARATOR_NEW_LINE +
                "PPK: " + store.getPPK() + "\n" +
                String.format("TotalShipmentsIncome: %.2f", store.getTotalDeliveryIncome()) + "\n" +
                Messenger.LINE_SEPARATOR_NEW_LINE;
    }

    static private String orderDTOToString(OrderDTO order, boolean inStore){
        DateFormat dateFormat = new SimpleDateFormat(Input.DATE_FORMAT);
        return "{" +
                (inStore ? "" : "id: " + order.getId() + ", ") +
                "date: " + dateFormat.format(order.getDate()) +
                (inStore ? "" :", store id: " + order.getStoreId()) +
                (inStore ? "" : ", store name: " + order.getStoreName()) + "\n" +
                "numOfItems: " + order.getNumOfItems() +
                String.format(", itemsPrice: %.2f", order.getItemsPrice()) +
                String.format(", deliveryPrice: %.2f", order.getDeliveryPrice()) +
                String.format(", totalPrice: %.2f", order.getTotalPrice()) +
                '}';
    }

    static private void removeItemFromStore(Engine engine, int storeId){
        System.out.println("---" + engine.getSDM().getStoreNameById(storeId) + "---");
        displayAllItemInStore(engine, storeId);
        System.out.println("chose item's id to remove item from store [id]");

        int itemId = Input.getOnePositiveInteger();
        if(engine.getSDM().itemNotExistInSystem(itemId)){
            System.out.println(Messenger.ITEM_NOT_EXIST_SYSTEM);
        }
        else if(!engine.getSDM().isItemSoldByStore(storeId, itemId)){
            System.out.println(Messenger.ITEM_NOT_SOLD_BY_STORE);
        }
        else{   //User entered a valid item's id
            try{
                engine.getSDM().removeItemFromStore(storeId, itemId);
                System.out.println(String.format(
                        "item '%s' was removed form store '%s' successfully",
                        engine.getSDM().getItemNameById(itemId), engine.getSDM().getStoreNameById(storeId)));
            }
            catch (invalidItemException exception){
                System.out.println(exception.getMessage());
            }
        }
    }

    static private void addItemToStore(Engine engine, int storeId){
        System.out.println("---" + engine.getSDM().getStoreNameById(storeId) + "---");
        displayAllItemInStore(engine, storeId);
        System.out.println("chose item's id to add item to store [id]");

        int itemId = Input.getOnePositiveInteger();
        if(engine.getSDM().itemNotExistInSystem(itemId)){
            System.out.println(Messenger.ITEM_NOT_EXIST_SYSTEM);
        }
        else if(engine.getSDM().isItemSoldByStore(storeId, itemId)){
            System.out.println(Messenger.ITEM_SOLD_BY_STORE);
        }
        else{   //User entered a valid item's id
            System.out.println("Please enter item's price");
            int itemNewPrice = Input.getOnePositiveInteger();
            engine.getSDM().addItemToStore(storeId, itemId, itemNewPrice);
            System.out.println(String.format(
                    "item '%s' with price '%d' was added to '%s' successfully",
                    engine.getSDM().getItemNameById(itemId), itemNewPrice, engine.getSDM().getStoreNameById(storeId)));
        }
    }

    static private void updateItemPrice(Engine engine, int storeId){
        System.out.println("---" + engine.getSDM().getStoreNameById(storeId) + "---");
        displayAllItemInStore(engine, storeId);
        System.out.println("chose item's id to update item's price in the store [id]");

        int itemId = Input.getOnePositiveInteger();
        if(engine.getSDM().itemNotExistInSystem(itemId)){
            System.out.println(Messenger.ITEM_NOT_EXIST_SYSTEM);
        }
        else if(!engine.getSDM().isItemSoldByStore(storeId, itemId)){
            System.out.println(Messenger.ITEM_NOT_SOLD_BY_STORE);
        }
        else{   //User entered a valid item's id
            System.out.println("Please enter item's new price");
            int itemNewPrice = Input.getOnePositiveInteger();
            engine.getSDM().updateItemPriceInStore(storeId, itemId, itemNewPrice);
            System.out.println(String.format(
                    "%s's price in store '%s' was updated to '%d' successfully",
                    engine.getSDM().getItemNameById(itemId), engine.getSDM().getStoreNameById(storeId), itemNewPrice));
        }
    }
}
