package components.map;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StoreButton extends Button {
    final private int storeId;
    final private String name;
    final private double PPK;
    final private int numberOfOrders;
    private boolean isDetailShown;

    public StoreButton(int storeId, String name, double PPK, int numberOfOrders) {
        Image image = new Image("/image/store.png");
        super.setGraphic(new ImageView(image));
        super.getStyleClass().setAll("LocationButton");
        isDetailShown = false;

        this.storeId = storeId;
        this.name = name;
        this.PPK = PPK;
        this.numberOfOrders = numberOfOrders;

        super.setOnAction(event -> {
            if(!isDetailShown){
                isDetailShown = true;
                super.setText(String.format("%s\nId: %d\n" + String.format("PPK: %.2f\n", PPK) + "Orders: %d",
                        name, storeId, numberOfOrders));
            }
            else{
                isDetailShown = false;
                super.setText("");
            }
        });
    }


}
