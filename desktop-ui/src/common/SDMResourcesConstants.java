package common;

import java.net.URL;

public class SDMResourcesConstants {
    private static final String BASE_PACKAGE = "/components";

    public static final URL CUSTOMERS_TABLE_VIEW = SDMResourcesConstants.class.getResource(BASE_PACKAGE + "/customer/customers.fxml");
    public static final URL ORDERS_TABLE_VIEW = SDMResourcesConstants.class.getResource(BASE_PACKAGE + "/order/orders.fxml");
    public static final URL ITEMS_TABLE_VIEW = SDMResourcesConstants.class.getResource(BASE_PACKAGE + "/item/items.fxml");
    public static final URL STORES_BORDER_PANE = SDMResourcesConstants.class.getResource(BASE_PACKAGE + "/store/stores.fxml");
}
