package common;

import java.net.URL;

public class SDMResourcesConstants {
    private static final String BASE_PACKAGE = "/components";

    public static final URL MAKE_ORDER_BORDER_PANE = SDMResourcesConstants.class.getResource((BASE_PACKAGE + "/order/makeOrder.fxml"));
    public static final URL CUSTOMERS_ANCHOR_PANE = SDMResourcesConstants.class.getResource(BASE_PACKAGE + "/customer/customers.fxml");
    public static final URL ORDERS_ANCHOR_PANE = SDMResourcesConstants.class.getResource(BASE_PACKAGE + "/order/orders.fxml");
    public static final URL ITEMS_ANCHOR_PANE = SDMResourcesConstants.class.getResource(BASE_PACKAGE + "/item/items.fxml");
    public static final URL STORES_VBOX = SDMResourcesConstants.class.getResource(BASE_PACKAGE + "/store/stores.fxml");
}