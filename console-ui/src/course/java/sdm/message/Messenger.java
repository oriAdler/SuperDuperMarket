package course.java.sdm.message;

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

    public static final String ENTER_FILE_PATH = "Please enter the file's full path";
    public static final String EXIT = "Thank you for buying Super Duper Market!";
    public static final String UNKNOWN_ERROR = "Unknown error has occurred";
    public static final String FILE_LOADED_SUCCESSFULLY = "File was loaded successfully";
    public static final String VALID_FILE_NOT_LOADED =
            "Can't complete operation: Please load a valid file before attempting to ";

    public static final String CHOOSE_STORE = "Please choose a store from the list above\n" +
            "[Id]";
    public static final String CHOOSE_DATE = "Please enter order's desired date\n" +
            "[dd/mm-hh:mm] (Day/Month-Hour:Minutes)";
    public static final String CHOOSE_LOCATION = "Please enter your location (two numbers)\n"+
            "each number between [1,50] (e.g: 1 5)";
    public static final String CONTINUE_ORDER = "Please chose one of the options:\n" +
            "* Enter item's id to add item to cart [id]\n" +
            "* Enter 'q'/'Q' to proceed to checkout ['q'/'Q']";

    public static final String[] MAIN_MENU ={
            "Load file",
            "Display stores",
            "Display all items",
            "Make order",
            "Show orders history",
            "Exit"
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

    public static String generateMenu(String[] strings){
        StringBuilder menu = new StringBuilder();
        menu.append("Please choose an option:\n");
        for(int i = 0; i < strings.length; i++){
            menu.append(i+1).append(") ").append(strings[i]).append("\n");
        }
        return menu.toString();
    }
}
