package sdm.message;

public class Messenger {

    public static final String WELCOME_MESSAGE =
                    "******************************\n" +
                    "******************************\n" +
                    "**                          **\n" +
                    "**        WELCOME TO        **\n" +
                    "**                          **\n" +
                    "**    SUPER DUPER MARKET    **\n" +
                    "**                          **\n" +
                    "******************************\n" +
                    "******************************\n";

    public static final String LINE_SEPARATOR_NEW_LINE = "----------------------------------------\n";
    public static final String LINE_SEPARATOR = "----------------------------------------";
    public static final String SUPER_DUPER_MARKET = "----Super Duper Market----";

    public static final String ENTER_FULL_FILE_PATH = "Please enter the file's full path (including file name)" +"\n"
            + "(e.g: C:\\test files\\ex1-small.xml)";
    public static String ENTER_FILE_PATH = "Please enter the file's full path (excluding file name)";
    public static final String EXIT = "Thank you for buying Super Duper Market!";
    public static final String UNKNOWN_ERROR = "Unknown error has occurred";
    public static final String FILE_LOADED_SUCCESSFULLY = "File was loaded successfully";
    public static final String VALID_FILE_NOT_LOADED =
            "Can't complete operation: Please load a valid file before attempting to ";

    public static final String CHOOSE_STORE = "Please enter a number to choose a store from the list above\n" +
            "[Id]";
    public static final String CHOOSE_DATE = "Please enter order's desired date\n" +
            "[dd/mm-hh:mm] (Day/Month-Hour:Minutes)";
    public static final String CHOOSE_LOCATION = "Please enter your location (two numbers)\n"+
            "each number between [1,50] (e.g: 1 5)";
    public static final String CONTINUE_ORDER = "Please chose one of the options:\n" +
            "* Enter item's id to add item to cart [id]\n" +
            "* Enter 'q'/'Q' to proceed to checkout ['q'/'Q']";
    public static final String ITEM_NOT_EXIST_SYSTEM = "Invalid input: item is not exist in the system";
    public static final String ITEM_NOT_SOLD_BY_STORE ="Invalid input: item is not sold by this store";
    public static final String ITEM_SOLD_BY_STORE ="Invalid input: item is already sold by this store";

    public static final String ENTER_FULL_PATH = "Please enter the desired full path to save file (excluding file name)";
    public static final String CANT_CONVERT_STRING_TO_PATH =
            "Invalid input: The path string cannot be converted to Path";
    public static final String PATH_NOT_EXIST = "Invalid input: path doesn't exist";
    public static final String FILE_NOT_EXIST = "Invalid input: file doesn't exist";
    public static final String ENTER_FILE_NAME = "Please enter the file's names with suffix '.dat'" + "\n"
            + "(e.g: orders.dat)";
    public static final String SUFFIX_DAT = ".dat";
    public static final String INVALID_FILE_NAME = "Invalid input: invalid file name";

    public static final String[] MAIN_MENU ={
            "Load file",
            "Display stores",
            "Display all items",
            "Make order",
            "Show orders history",
            "Update store's Prices/Items",
            "Save orders to file",
            "Load orders from file",
            "Exit"
    };

    public static final String[] UPDATE_STORE_MENU ={
            "Remove item from store",
            "Add item to store",
            "Update item price in store"
    };

    public static final String[] COMPLETE_ORDER_MENU ={
            "Approve order",
            "Cancel order"
    };

    public static final String[] CHOOSE_ORDER_METHOD_MENU={
            "Static order: make order from a specific store",
            "Dynamic order: let 'Super Duper Market' find the cheapest cart for you!"
    };

    public static String[] getChooseOrderMethodMenu() {
        return CHOOSE_ORDER_METHOD_MENU;
    }

    public static String[] getMainMenu() {
        return MAIN_MENU;
    }

    public static String[] getCompleteOrderMenu() {
        return COMPLETE_ORDER_MENU;
    }

    public static String[] getUpdateStoreMenu() {
        return UPDATE_STORE_MENU;
    }

    public static String generateMenu(String[] strings){
        StringBuilder menu = new StringBuilder();
        menu.append("Please choose an option:\n");
        for(int i = 0; i < strings.length; i++){
            menu.append(i+1).append(") ").append(strings[i]).append("\n");
        }
        return menu.toString();
    }
}
