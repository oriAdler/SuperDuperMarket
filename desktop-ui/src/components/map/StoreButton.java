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
        isDetailShown = false;

        this.storeId = storeId;
        this.name = name;
        this.PPK = PPK;
        this.numberOfOrders = numberOfOrders;

        super.setOnAction(event -> {
            if(!isDetailShown){
                isDetailShown = true;
                super.setText(String.format("Id: %d\nName: %s\nPPK: %f\nOrders: %d",
                        storeId, name, PPK, numberOfOrders));
            }
            else{
                isDetailShown = false;
                super.setText("");
            }
        });
    }


}
