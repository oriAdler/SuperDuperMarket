package components.order;

import DTO.CartDTO;
import DTO.CustomerDTO;
import DTO.ItemDTO;
import DTO.StoreDTO;
import common.Input;
import common.SDMResourcesConstants;
import components.cart.CartController;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MakeOrderController implements Initializable {

    // View:
    @FXML ScrollPane scrollPaneCenter;
    @FXML GridPane gridPaneTop;
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
    @FXML private Button nextToSummaryButton;
    @FXML private Button approveButton;
    @FXML private Button cancelButton;
    @FXML private Label totalPriceLabel;
    @FXML private Label totalPriceValueLabel;

    // Secondary Controllers:
    private MainController mainController;

    private ItemsController itemsController;
    private AnchorPane itemsAnchorPane;

    private CartController cartController;
    private GridPane cartGridPane;

    private DynamicOrderController dynamicOrderController;
    private AnchorPane dynamicOrderAnchorPane;

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

    private SimpleDoubleProperty totalPrice;

    // Order details:
    LocalDate date;
    CustomerDTO customer;
    StoreDTO store;
    Map<Integer, Double> itemIdToAmount;
    CartDTO cart;
    List<CartDTO> cartList;

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

        totalPrice = new SimpleDoubleProperty();

        // Load items & orders FXML files:
        itemsController = createItemsController();
        cartController = createCartController();
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
        nextToSummaryButton.setVisible(false);
        nextToSummaryButton.setDisable(true);

        staticOrderRadioButton.disableProperty().bind(customerComboBoxClicked.not().or(okButtonIsClicked));
        dynamicOrderRadioButton.disableProperty().bind(customerComboBoxClicked.not().or(okButtonIsClicked));
        chooseStoreComboBox.disableProperty().bind(staticOrderRadioButtonClicked.not().or(okButtonIsClicked));
        chooseCustomerComboBox.disableProperty().bind(okButtonIsClicked);
        datePicker.disableProperty().bind(okButtonIsClicked);

        okButton.disableProperty().bind(datePickerClicked.not().or(storeComboBoxClicked.not().and(dynamicOrderRadioButtonClicked.not())));
        checkoutButton.disableProperty().bind(itemsController.proceedToCheckoutProperty().not());

        totalPriceValueLabel.textProperty().bind(Bindings.concat(Bindings.format("%.2f", totalPrice)));
        totalPriceLabel.setVisible(false);
        totalPriceValueLabel.setVisible(false);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void fillMakeOrderData(Engine engine){
        // View:
        scrollPaneCenter.setContent(gridPaneTop);
        okButton.setVisible(true);
        checkoutButton.setVisible(false);
        approveButton.setVisible(false);
        itemsController.setProceedToCheckout(false);
        approveButton.setDisable(true);

        // Choose customer:
        ObservableList<CustomerDTO> customerOL = FXCollections.observableArrayList();
        customerOL.addAll(engine.getAllCustomersList());
        chooseCustomerComboBox.setItems(customerOL);
        chooseCustomerComboBox.setOnAction(event -> {
            customerComboBoxClicked.set(true);
            customerId.set(chooseCustomerComboBox.getValue().getId());
        });

        // Choose date:
        datePicker.setOnAction(e -> datePickerClicked.set(true));

        // Choose order type:
        staticOrderRadioButton.setOnAction(e-> {
            staticOrderRadioButtonClicked.set(true);
            dynamicOrderRadioButtonClicked.set(false);
        });
        dynamicOrderRadioButton.setOnAction(e->{
            dynamicOrderRadioButtonClicked.set(true);
            staticOrderRadioButtonClicked.set(false);
            storeId.set(0);
            xLocation.set(0);
            yLocation.set(0);
            deliveryPrice.set(0);
        });

        // Choose store:
        ObservableList<StoreDTO> storeOL = FXCollections.observableArrayList();
        storeOL.addAll(engine.getAllStoreList());
        chooseStoreComboBox.setItems(storeOL);
        chooseStoreComboBox.setOnAction(e ->{
            storeComboBoxClicked.set(true);
            if(chooseStoreComboBox.getValue() != null){
                storeId.set(chooseStoreComboBox.getValue().getId());
                xLocation.set(chooseStoreComboBox.getValue().getxLocation());
                yLocation.set(chooseStoreComboBox.getValue().getyLocation());
                engine.getSDM().calculateDeliveryPrice(storeId.getValue(), customerId.getValue(), deliveryPrice::set);
            }
        });
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        mainController.setInOrderProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();
    }

    @FXML
    void okButtonAction(ActionEvent event) {
        okButtonIsClicked.set(true);
        okButton.setVisible(false);
        checkoutButton.setVisible(true);

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

    @FXML
    void checkoutButtonAction(ActionEvent event){
        checkoutButton.setVisible(false);
        nextToSummaryButton.setVisible(true);
        nextToSummaryButton.setDisable(false);

        // Collect data from controllers:
        date = datePicker.getValue();
        customer = chooseCustomerComboBox.getValue();
        if(staticOrderRadioButtonClicked.getValue()){
            store = chooseStoreComboBox.getValue();
        }
        else{
            store = null;
        }

        // Collect items from table.
        itemIdToAmount = new HashMap<>();
        itemsController.getTableView().getItems().forEach(item -> {
            if(Input.isPositiveDouble(item.getAmount())){
                itemIdToAmount.put(item.getId(), Double.parseDouble(item.getAmount()));
            }
        });

        // Execute order:
        if(staticOrderRadioButtonClicked.getValue()){
            nextToSummaryButton.fire();
        }
        else{   // dynamicOrderRadioButtonClicked.getValue() == true
            cartList = engine.getSDM().summarizeDynamicOrder(itemIdToAmount, customer.getId());

            // Load dynamic order FXML file:
            dynamicOrderController = createDynamicOrderController();
            dynamicOrderController.fillDynamicOrderTableView(cartList);

            scrollPaneCenter.setContent(dynamicOrderAnchorPane);
            AnchorPane.setTopAnchor(dynamicOrderAnchorPane, 0.0);
            AnchorPane.setBottomAnchor(dynamicOrderAnchorPane, 0.0);
            AnchorPane.setRightAnchor(dynamicOrderAnchorPane, 0.0);
            AnchorPane.setLeftAnchor(dynamicOrderAnchorPane, 0.0);
        }
    }

    //TODO: hakol beshekel shows distance 0.0
    @FXML
    void nextToSummaryButtonAction(ActionEvent event){
        approveButton.setVisible(true);
        approveButton.setDisable(false);
        nextToSummaryButton.setVisible(false);
        nextToSummaryButton.setDisable(true);
        totalPriceLabel.setVisible(true);
        totalPriceValueLabel.setVisible(true);

        FlowPane flowPane = new FlowPane();
        scrollPaneCenter.setContent(flowPane);

        if(staticOrderRadioButtonClicked.getValue()){
            // Generate customer's cart:
            cart = engine.getSDM().summarizeStaticOrder(itemIdToAmount, store.getId(),
                    new Point(customer.getXLocation(), customer.getYLocation()));

            // Fill cart controller data:
            cartController.fillGridPaneData(cart, store.getName(), store.getId(), store.getPPK(),
                    engine.getSDM().calculateDistanceStoreToCustomer(store.getId(), customer.getId()),
                    engine.getSDM().calculateDeliveryPrice(store.getId(), customer.getId()));
            totalPrice.set(cart.getTotalOrderPrice());

            flowPane.getChildren().add(cartGridPane);
        }
        else{
            cartList = engine.getSDM().summarizeDynamicOrder(itemIdToAmount, customer.getId());
            for(CartDTO cart : cartList){
                CartController currentCartController = createCartController();
                currentCartController.fillGridPaneData(cart, cart.getStoreName(), cart.getStoreId(),
                        cart.getPPK(), cart.getDistanceFromStoreToCustomer(), cart.getDeliveryPrice());
                totalPrice.set(totalPrice.get() + cart.getTotalOrderPrice());

                flowPane.getChildren().add(currentCartController.getGridPane());
            }
        }
    }

    @FXML
    void approveButtonAction(ActionEvent event) {
        //NOTE: change date in engine to local date?
        if(staticOrderRadioButtonClicked.get()){
            engine.getSDM().executeStaticOrder(cart, new Date(), customer.getId());

        }
        else{   // staticOrderRadioButtonClicked.getValue() == true
            engine.getSDM().executeDynamicOrder(cartList, new Date(), customer.getId());
        }

        mainController.setInOrderProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();
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

    public CartController createCartController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.CART_GRID_PANE);
            cartGridPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DynamicOrderController createDynamicOrderController(){
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.DYNAMIC_ORDER_ANCHOR_PANE);
            dynamicOrderAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
