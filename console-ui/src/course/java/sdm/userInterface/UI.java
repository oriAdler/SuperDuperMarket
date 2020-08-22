package course.java.sdm.userInterface;

import course.java.sdm.DTO.ItemDTO;
import course.java.sdm.DTO.CartDTO;
import course.java.sdm.DTO.OrderDTO;
import course.java.sdm.DTO.StoreDTO;
import course.java.sdm.engine.Engine;
import course.java.sdm.engine.EngineImpl;
import course.java.sdm.exception.invalidCustomerLocationException;
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
    //note: instead of passing engine all the time make it a member of class.
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

    static private void showOrdersHistory(Engine engine){
        if(!engine.isValidFileLoaded()){
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

    static private void makeOrder(Engine engine){
        if(!engine.isValidFileLoaded()){
            System.out.println(Messenger.VALID_FILE_NOT_LOADED + "make an order");
        }
        else{
            displayStoresMinimalData(engine.getAllStoreList());
            int storeId = chooseStore(engine);

            System.out.println(Messenger.CHOOSE_DATE);
            Date orderDate = Input.getDate();   //note: get date is buggy

            Point customerLocation = getUserLocation(engine);

            //note: show store name, maybe should get back DTO map
            System.out.println("---" + engine.getStoreNameById(storeId) + "---");
            displayAllItemInStore(engine, storeId);
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);

            Map<Integer, Double> itemsMap = chooseItems(engine, storeId);
            if(itemsMap.isEmpty()){
                System.out.println("No items were chosen");
            }
            else{
                CartDTO cart = engine.summarizeStaticOrder(itemsMap, storeId, customerLocation);
                displayOrderSummary(cart);
                System.out.println(Messenger.generateMenu(Messenger.getOrderMenu()));
                Integer Choice = Input.getOnePositiveIntegerInRange(MIN_CHOICE, Messenger.getOrderMenu().length,
                        String.format("Invalid input: number must be between %d-%d",
                                MIN_CHOICE, Messenger.getOrderMenu().length));
                if(Choice.equals(1)){
                    engine.executeStaticOrder(cart, orderDate, storeId);
                    System.out.println("Thank you for buying " + engine.getStoreNameById(storeId) + "!");
                }
                else{
                    System.out.println("You chose to renounce to order.. hope to see you again soon");
                }
            }
        }
        System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
    }

    static private void displayOrderSummary(CartDTO cart){
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

    static private Map<Integer, Double> chooseItems(Engine engine, int storeId){
        Scanner scanner = new Scanner(System.in);
        Map<Integer, Double> itemsMap = new HashMap<>();

        do{
            System.out.println(Messenger.CONTINUE_ORDER);
            String userInputString = scanner.nextLine().trim();
            //Check if user chose to finish choosing by entering "q"/"Q"
            if(userInputString.equals("q") || userInputString.equals("Q")){
                return itemsMap;
            }
            else{   //User continues to chose items by ID
                if(Input.isPositiveInteger(userInputString)){
                    int itemId = Integer.parseInt(userInputString);

                    if(!engine.isItemExistInSystem(itemId)){
                        System.out.println("Invalid input: item is not exist in the system");
                    }
                    else if(!engine.isItemSoldByStore(storeId, itemId)) {
                            System.out.println("Invalid input: item is not sold by this store");
                    }
                    else{   //User entered a valid item ID, continue to chose amount
                        PurchaseCategory category = engine.getItemPurchaseCategory(itemId);
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
                }
            }
            System.out.println(Messenger.LINE_SEPARATOR_NEW_LINE);
        } while(true);  //A return statement is inside the do-while loop
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
                        + "Price: " + (engine.isItemSoldByStore(storeId, itemDTO.getId()) ?
                        engine.getItemPriceInStore(storeId, itemDTO.getId()) :
                        "Not sold by this store") + "}")
                .forEach(System.out::println);
    }

    static private Point getUserLocation(Engine engine){
        System.out.println(Messenger.CHOOSE_LOCATION);
        boolean isValidLocation;
        Point customerLocation;

        do{
            int[] inputArray = Input.getTwoIntegers();
            customerLocation = new Point(inputArray[0], inputArray[1]);
            try{
                engine.checkCustomerLocationIsValid(customerLocation);
                isValidLocation = true;
            }
            catch (invalidCustomerLocationException exception){
                System.out.println(exception.getMessage());
                isValidLocation = false;
            }
        } while(!isValidLocation);

        return customerLocation;
    }

    //note: consider a generic function for choosing a store/item
    static private int chooseStore(Engine engine){
        System.out.println(Messenger.CHOOSE_STORE);
        boolean isValidChoice;
        int userChoice = 0;

        do{
            userChoice = Input.getOnePositiveInteger();
            if(!engine.storeExistById(userChoice)){
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

    static private void displayAllItems(Engine engine){
        if(!engine.isValidFileLoaded()){
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

    static private void displayStores(Engine engine){
        if(!engine.isValidFileLoaded()){
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
}
