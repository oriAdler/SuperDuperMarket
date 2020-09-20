package components.discount;

import DTO.DiscountDTO;
import DTO.OfferDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import sdm.discount.Offer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DiscountController implements Initializable {

    @FXML private AnchorPane anchorPane;

    @FXML private Label discountNameLabel;
    @FXML private Label discountTermLabel;
    @FXML private ListView<OfferDTO> offersListView;
    @FXML private ComboBox<OfferDTO> offerComboBox;
    @FXML private Button addButton;

    // Secondary Controller:
    private AllDiscountsController allDiscountsController;
    DiscountDTO discount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public ComboBox<OfferDTO> getOfferComboBox() {
        return offerComboBox;
    }

    public Button getAddButton() {
        return addButton;
    }

    public void setAllDiscountsController(AllDiscountsController allDiscountsController) {
        this.allDiscountsController = allDiscountsController;
    }

    public void fillDiscountTileData(DiscountDTO discountDTO){
        this.discount = discountDTO;

        discountNameLabel.setText(discountDTO.getName());
        discountTermLabel.setText(discountDTO.toString());

        ObservableList<OfferDTO>  offersOL = FXCollections.observableArrayList();
        offersOL.addAll(discountDTO.getOfferList());
        offersListView.setItems(offersOL);
        offerComboBox.setItems(offersOL);

        if(discountDTO.getOperator().equals("IRRELEVANT") || discountDTO.getOperator().equals("ALL-OR-NOTHING")){
            offerComboBox.setVisible(false);
            offerComboBox.setDisable(true);
        }
        else{   // Operator = "ONE-OF"
            addButton.setDisable(true);
        }

        offerComboBox.setOnAction(e -> addButton.setDisable(false));
    }

    @FXML
    void addButtonAction(ActionEvent event) {
        List<OfferDTO> offerList = new ArrayList<>();
        Integer itemId = discount.getItemId();
        Double itemAmount = discount.getAmount();

        if(discount.getOperator().equals("IRRELEVANT") || discount.getOperator().equals("ALL-OR-NOTHING")){
            offerList.addAll(discount.getOfferList());
        }
        else{   //discount.getOperator().equals("ONE-OF")
            offerList.add(offerComboBox.getValue());
        }

        allDiscountsController.updateUserChoice(offerList, itemId, itemAmount, discount);
    }
}
