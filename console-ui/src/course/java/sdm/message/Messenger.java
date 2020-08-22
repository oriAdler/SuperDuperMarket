package course.java.sdm.message;

public class Messenger {

    static public final String WELCOME_MESSAGE =
                    "******************************\n" +
                    "******************************\n" +
                    "**                          **\n" +
                    "**        WELCOME TO        **\n" +
                    "**                          **\n" +
                    "**    SUPER DUPER MARKET    **\n" +
                    "**                          **\n" +
                    "******************************\n" +
                    "******************************\n";

    static public final String LINE_SEPARATOR_NEW_LINE = "----------------------------------------\n";
    static public final String LINE_SEPARATOR = "----------------------------------------";
    static public final String SUPER_DUPER_MARKET = "----Super Duper Market----";

    static public final String ENTER_FILE_PATH = "Please enter the file's full path";
    static public final String EXIT = "Thank you for buying Super Duper Market!";
    static public final String UNKNOWN_ERROR = "Unknown error has occurred";
    static public final String FILE_LOADED_SUCCESSFULLY = "File was loaded successfully";
    static public final String VALID_FILE_NOT_LOADED =
            "Can't complete operation: Please load a valid file before attempting to ";

    static public final String CHOOSE_STORE = "Please choose a store from the list above\n" +
            "[Id]";
    static public final String CHOOSE_DATE = "Please enter order's desired date\n" +
            "[dd/mm-hh:mm] (Day/Month-Hour:Minutes)";
    static public final String CHOOSE_LOCATION = "Please enter your location (two numbers)\n"+
            "each number between [1,50] (e.g: 1 5)";
    static public final String CONTINUE_ORDER = "Please chose one of the options:\n" +
            "* Enter item's id to add item to cart [id]\n" +
            "* Enter 'q'/'Q' to proceed to checkout ['q'/'Q']";

    static public final String[] MAIN_MENU ={
            "Load file",
            "Display stores",
            "Display all items",
            "Make order",
            "Show orders history",
            "Exit"
    };

    static public final String[] ORDER_MENU ={
            "Approve order",
            "Cancel order"
    };

    public static String[] getMainMenu() {
        return MAIN_MENU;
    }

    public static String[] getOrderMenu() {
        return ORDER_MENU;
    }

    //question: how to print a menu?
    static public String generateMenu(String[] strings){
        StringBuilder menu = new StringBuilder();
        menu.append("Please choose an option:\n");
        for(int i = 0; i < strings.length; i++){
            menu.append(i+1).append(") ").append(strings[i]).append("\n");
        }
        return menu.toString();
    }
//    static public final String LOAD_FILE = "Load file";
//    static public final String SHOW_STORES = "Show stores";
//    static public final String SHOW_ALL_ITEMS = "Show all items";
//    static public final String MAKE_ORDER = "Make order";
//    static public final String SHOW_ORDERS_HISTORY = "Show orders history";
//    static public final String Exit = "Exit";
}
