package components.map;

import DTO.CustomerDTO;
import DTO.StoreDTO;
import engine.Engine;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MapGenerator {

    public static ScrollPane draw(Engine engine){
        ScrollPane scrollPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        double width = engine.getSDM().findMaxXCoordinate() + 1;
        double height = engine.getSDM().findMaxYCoordinate() + 1;

        // Draw grid pane:
        for (int x = 0; x < width; x++) {
            gridPane.addColumn(x, createDummyPane());
        }
        for (int y = 0; y < height; y++) {
            gridPane.addRow(y, createDummyPane());
        }

        // Add stores & customers to grid pane:
        for(CustomerDTO customer : engine.getAllCustomersList()){
            CustomerButton customerButton = new CustomerButton(customer.getId(),
                    customer.getName(), customer.getNumberOfOrders());
            gridPane.add(customerButton,customer.getXLocation()-1, customer.getYLocation()-1);
        }
        for(StoreDTO store : engine.getAllStoreList()){
            StoreButton storeButton = new StoreButton(store.getId(),
                    store.getName(), store.getPPK(), store.getOrders().size());
            gridPane.add(storeButton, store.getxLocation()-1, store.getyLocation()-1);
        }

        scrollPane.setContent(gridPane);

        return scrollPane;
    }

    private static Pane createDummyPane(){
        Pane pane = new Pane();
        pane.setMinHeight(20);
        pane.setMinWidth(20);

        return pane;
    }
}
