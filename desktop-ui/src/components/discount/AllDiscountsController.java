package components.discount;

import DTO.DiscountDTO;
import DTO.OfferDTO;
import common.SDMResourcesConstants;
import components.item.ItemsController;
import engine.Engine;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import sdm.discount.Offer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AllDiscountsController implements Initializable {

    @FXML private AnchorPane anchorPane;
    @FXML private BorderPane borderPane;
    @FXML private ScrollPane scrollPane;
    @FXML private Label discountLabel;

    private AnchorPane discountAnchorPane;

    private SimpleStringProperty discountLabelProperty;

    private Engine engine;
    private Map<Integer, Double> itemsIdToAmount;
    private List<OfferDTO> offerList;
    private Integer discountCounter;

    public AllDiscountsController(){
        offerList = new ArrayList<>();
        discountLabelProperty = new SimpleStringProperty();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        discountLabel.textProperty().bind(discountLabelProperty);
        discountCounter = 0;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setItemsIdToAmount(Map<Integer, Double> itemsIdToAmount) {
        this.itemsIdToAmount = itemsIdToAmount;
    }

    public List<OfferDTO> getOfferList() {
        return offerList;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void fillAllDiscountData(Integer storeId){
        FlowPane flowPane = new FlowPane();
        List<DiscountDTO> discountDTOList = engine.getSDM().getStoreDiscounts(storeId);

        for(DiscountDTO discountDTO : discountDTOList){
            DiscountController discountController = createDiscountController();
            discountController.fillDiscountTileData(discountDTO);
            discountController.getOfferComboBox().setVisible(false);
            discountController.getAddButton().setVisible(false);

            flowPane.getChildren().add(discountController.getAnchorPane());
        }

        scrollPane.setContent(flowPane);
    }

    public void fillAllDiscountData(){
        FlowPane flowPane = new FlowPane();
        List<DiscountDTO> discountDTOList = engine.getSDM().getDiscounts(itemsIdToAmount);

        for(DiscountDTO discountDTO : discountDTOList){
            DiscountController discountController = createDiscountController();
            discountController.setAllDiscountsController(this);
            discountController.fillDiscountTileData(discountDTO);

            flowPane.getChildren().add(discountController.getAnchorPane());
        }

        scrollPane.setContent(flowPane);
    }

    public void updateUserChoice(List<OfferDTO> offerListToAdd, Integer itemId, Double itemAmount, DiscountDTO discount){
        discountCounter++;
        discountLabelProperty.set(String.format("%d. Discount '%s' was added to cart", discountCounter, discount.getName()));
        offerList.addAll(offerListToAdd);
        itemsIdToAmount.put(itemId, itemsIdToAmount.get(itemId) - itemAmount);
        fillAllDiscountData();
    }

    public DiscountController createDiscountController()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.DISCOUNT_ANCHOR_PANE);
            discountAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
