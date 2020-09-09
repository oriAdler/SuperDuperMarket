package components.order;

import DTO.CustomerDTO;
import DTO.ItemDTO;
import DTO.StoreDTO;
import common.SDMResourcesConstants;
import components.item.ItemsController;
import components.main.MainController;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

//TODO: handle exceptions when choosing again this option
public class MakeOrderController implements Initializable {

    // View:
    @FXML private AnchorPane anchorPaneBottom;

    // Buttons:
    @FXML private ComboBox<CustomerDTO> chooseCustomerComboBox;
    @FXML private Label customerIdLabel;

    @FXML private DatePicker datePicker;

    private ToggleGroup orderTypeToggleGroup;
    @FXML private RadioButton staticOrderRadioButton;
    @FXML private RadioButton dynamicOrderRadioButton;

    @FXML private ComboBox<StoreDTO> chooseStoreComboBox;
    @FXML private Label storeIdLabel;
    @FXML private Label locationLabel;
    @FXML private Label deliveryPriceLabel;

    @FXML private Button okButton;
    @FXML private Button checkoutButton;
    @FXML private Button cancelButton;

    // Secondary Controllers:
    private MainController mainController;
    private ItemsController itemsController;
    private AnchorPane itemsAnchorPane;

    private Engine engine;

    // Properties:
    private SimpleIntegerProperty customerId;
    private SimpleIntegerProperty storeId;
    private SimpleIntegerProperty xLocation;
    private SimpleIntegerProperty yLocation;
    private SimpleDoubleProperty deliveryPrice;

    private SimpleBooleanProperty customerComboBoxClicked;
    private SimpleBooleanProperty storeComboBoxClicked;
    private SimpleBooleanProperty staticOrderRadioButtonClicked;
    private SimpleBooleanProperty dynamicOrderRadioButtonClicked;
    private SimpleBooleanProperty datePickerClicked;
    private SimpleBooleanProperty okButtonIsClicked;

    public MakeOrderController(){
        customerId = new SimpleIntegerProperty();
        storeId = new SimpleIntegerProperty();
        xLocation = new SimpleIntegerProperty();
        yLocation = new SimpleIntegerProperty();
        deliveryPrice = new SimpleDoubleProperty();

        customerComboBoxClicked = new SimpleBooleanProperty();
        storeComboBoxClicked = new SimpleBooleanProperty();
        staticOrderRadioButtonClicked = new SimpleBooleanProperty();
        dynamicOrderRadioButtonClicked = new SimpleBooleanProperty();
        datePickerClicked = new SimpleBooleanProperty();
        okButtonIsClicked = new SimpleBooleanProperty();

        itemsController = createItemsController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderTypeToggleGroup = new ToggleGroup();
        staticOrderRadioButton.setToggleGroup(orderTypeToggleGroup);
        dynamicOrderRadioButton.setToggleGroup(orderTypeToggleGroup);

        customerIdLabel.textProperty().bind(Bindings.format("%d", customerId));
        storeIdLabel.textProperty().bind(Bindings.format("%d", storeId));
        locationLabel.textProperty().bind(Bindings.format("[%d,%d]", xLocation, yLocation));
        deliveryPriceLabel.textProperty().bind(Bindings.format("%.2f", deliveryPrice));

        customerComboBoxClicked.set(false);
        datePickerClicked.set(false);
        storeComboBoxClicked.set(false);
        staticOrderRadioButtonClicked.set(false);
        okButtonIsClicked.set(false);

        staticOrderRadioButton.disableProperty().bind(customerComboBoxClicked.not().or(okButtonIsClicked));
        dynamicOrderRadioButton.disableProperty().bind(customerComboBoxClicked.not().or(okButtonIsClicked));
        chooseStoreComboBox.disableProperty().bind(staticOrderRadioButtonClicked.not().or(okButtonIsClicked));
        chooseCustomerComboBox.disableProperty().bind(okButtonIsClicked);
        datePicker.disableProperty().bind(okButtonIsClicked);

        okButton.disableProperty().bind(datePickerClicked.not().or(storeComboBoxClicked.not().and(dynamicOrderRadioButtonClicked.not())));
        checkoutButton.setVisible(false);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void fillMakeOrderData(Engine engine){
        //TODO: this check is necessary?
        if(engine != null){
            ObservableList<CustomerDTO> customerOL = FXCollections.observableArrayList();
            customerOL.addAll(engine.getAllCustomersList());
            chooseCustomerComboBox.setItems(customerOL);
            chooseCustomerComboBox.setOnAction(e -> {
                customerComboBoxClicked.set(true);
                customerId.set(chooseCustomerComboBox.getValue().getId());
            });

            datePicker.setOnAction(e -> datePickerClicked.set(true));

            staticOrderRadioButton.setOnAction(e-> {
                staticOrderRadioButtonClicked.set(true);
                dynamicOrderRadioButtonClicked.set(false);
            });

            dynamicOrderRadioButton.setOnAction(e->{
                dynamicOrderRadioButtonClicked.set(true);
                staticOrderRadioButtonClicked.set(false);
            });

            ObservableList<StoreDTO> storeOL = FXCollections.observableArrayList();
            storeOL.addAll(engine.getAllStoreList());
            chooseStoreComboBox.setItems(storeOL);
            chooseStoreComboBox.setOnAction(e ->{
                storeComboBoxClicked.set(true);
                storeId.set(chooseStoreComboBox.getValue().getId());
                xLocation.set(chooseStoreComboBox.getValue().getxLocation());
                yLocation.set(chooseStoreComboBox.getValue().getyLocation());
                engine.getSDM().calculateDeliveryPrice(storeId.getValue(), customerId.getValue(), deliveryPrice::set);
            });
        }
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {

    }

    @FXML
    void okButtonAction(ActionEvent event) {
        okButtonIsClicked.set(true);
        okButton.setVisible(false);
        checkoutButton.setVisible(true);
        checkoutButton.disableProperty().bind(itemsController.proceedToCheckoutProperty().not());

        List<ItemDTO> itemList, dummyList;
        // Get the items list:
        if(dynamicOrderRadioButtonClicked.getValue()){
            itemsController.getPriceColumn().setVisible(false);
            itemList = engine.getAllItemList();
        }
        else{
            StoreDTO store = chooseStoreComboBox.getValue();
            itemList = store.getItems();
            dummyList = engine.getAllItemList();
            dummyList.forEach(itemDTO -> itemDTO.setPrice("Not Sold"));
            itemList.addAll
                    (dummyList.stream().filter(itemDTO -> !itemList.contains(itemDTO))
                            .collect(Collectors.toList()));
        }

        // Show the table:
        itemsController.fillTableViewData(itemList);
        anchorPaneBottom.getChildren().clear();
        anchorPaneBottom.getChildren().add(itemsController.getTableView());

        itemsController.getAmountColumn().setVisible(true);
        itemsController.getTableView().setEditable(true);
    }

    //TODO: complete order procedure, save itemIdToAmount a member?
    @FXML
    void checkoutButtonAction(ActionEvent event){
        Map<Integer, Double> itemIdToAmount = new HashMap<>();
        // Collect items from table.
        itemsController.getTableView().getItems().forEach(item -> {
            try{
                double amount = Double.parseDouble(item.getAmount());
                if(amount > 0){
                    itemIdToAmount.put(item.getId(), amount);
                }
            }
            catch (Exception ignore){

            }
        });
        anchorPaneBottom.getChildren().clear();
    }

    public ItemsController createItemsController()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ITEMS_ANCHOR_PANE);
            itemsAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
