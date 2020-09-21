package components.map;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomerButton extends Button {
    final private int customerId;
    final private String customerName;
    final private int numberOfOrders;
    private boolean isDetailShown;

    public CustomerButton(int customerId, String customerName, int numberOfOrders) {
        ImageView image = new ImageView("/image/customer.png");
        super.setGraphic(image);
        super.getStyleClass().setAll("LocationButton");
        isDetailShown = false;

        this.customerId = customerId;
        this.customerName = customerName;
        this.numberOfOrders = numberOfOrders;

        super.setOnAction(event -> {
            if(!isDetailShown){
                isDetailShown = true;
                super.setText(String.format("%s\nId: %d\nOrders: %d",
                        customerName, customerId, numberOfOrders));
            }
            else{
                isDetailShown = false;
                super.setText("");
            }
        });
    }
}
