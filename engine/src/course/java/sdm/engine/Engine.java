package course.java.sdm.engine;

public interface Engine {
    void loadDataFromFile(String pathName);
    String showStores();
    String showAllItems();
    void makeOrder();
    String showOrdersHistory();
}
