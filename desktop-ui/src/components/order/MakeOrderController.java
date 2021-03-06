package components.order;

import DTO.*;
import common.Input;
import common.SDMResourcesConstants;
import components.cart.CartController;
import components.discount.AllDiscountsController;
import components.item.ItemsController;
import components.main.MainController;
import components.message.MessageGeneratorController;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

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
    @FXML private Label chooseItemLabel;
    @FXML private Label chooseItemLabel2;

    @FXML private Button okButton;
    @FXML private Button checkoutButton;
    @FXML private Button nextToDiscountButton;
    @FXML private Button nextToSummaryButton;
    @FXML private Button approveButton;
    @FXML private Button cancelButton;
    @FXML private GridPane gridPaneSummary;
    @FXML private Label itemsPriceValueLabel;
    @FXML private Label deliveryPriceValueLabel;
    @FXML private Label totalPriceValueLabel;

    @FXML private GridPane taskGridPane;
    @FXML private ProgressIndicator taskProgressBar;
    @FXML private Label taskMessageLabel;
    @FXML private Label progressPercentLabel;

    @FXML private ImageView questionImage;
    @FXML private ImageView questionImageOrder;

    // Secondary Controllers:
    private MainController mainController;

    private ItemsController itemsController;
    private AnchorPane itemsAnchorPane;

    private CartController cartController;
    private GridPane cartGridPane;

    private DynamicOrderController dynamicOrderController;
    private AnchorPane dynamicOrderAnchorPane;

    private AllDiscountsController allDiscountsController;
    private AnchorPane  allDiscountAnchorPane;

    private MessageGeneratorController messageGeneratorController;
    private AnchorPane messageAnchorPane;

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

    private SimpleDoubleProperty totalItemsPrice;
    private SimpleDoubleProperty totalDeliveryPrice;
    private SimpleDoubleProperty totalPrice;

    private SimpleStringProperty taskMessageProperty;
    private SimpleBooleanProperty isOrderReadyProperty;

    private SimpleBooleanProperty isAnimationCheckBoxChosen;

    // Order details:
    LocalDate date;
    CustomerDTO customer;
    StoreDTO store;
    Map<Integer, Double> itemIdToAmount;
    CartDTO cart;
    List<CartDTO> cartList;
    List<OfferDTO> offerList;

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
        totalItemsPrice = new SimpleDoubleProperty();
        totalDeliveryPrice = new SimpleDoubleProperty();

        taskMessageProperty = new SimpleStringProperty();
        isOrderReadyProperty = new SimpleBooleanProperty(false);

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
        nextToDiscountButton.setVisible(false);
        nextToDiscountButton.setDisable(true);

        staticOrderRadioButton.disableProperty().bind(customerComboBoxClicked.not().or(okButtonIsClicked));
        dynamicOrderRadioButton.disableProperty().bind(customerComboBoxClicked.not().or(okButtonIsClicked));
        chooseStoreComboBox.disableProperty().bind(staticOrderRadioButtonClicked.not().or(okButtonIsClicked));
        chooseCustomerComboBox.disableProperty().bind(okButtonIsClicked);
        datePicker.disableProperty().bind(okButtonIsClicked);

        chooseItemLabel.setVisible(false);
        chooseItemLabel2.setVisible(false);

        okButton.disableProperty().bind(datePickerClicked.not().or(storeComboBoxClicked.not().and(dynamicOrderRadioButtonClicked.not())));
        checkoutButton.disableProperty().bind(itemsController.proceedToCheckoutProperty().not());

        itemsPriceValueLabel.textProperty().bind(Bindings.format("%.2f", totalItemsPrice));
        deliveryPriceValueLabel.textProperty().bind(Bindings.format("%.2f", totalDeliveryPrice));
        totalPriceValueLabel.textProperty().bind(Bindings.format("%.2f", totalPrice));
        gridPaneSummary.setVisible(false);

        taskGridPane.setVisible(false);
        taskMessageLabel.textProperty().bind(taskMessageProperty);

        Tooltip.install(questionImage, new Tooltip("Chose at least one item to continue"));
        questionImage.setVisible(false);
        Tooltip.install(questionImageOrder, new Tooltip("Static order - buy from one store\n" +
                "Dynamic order - let SDM find the best price for you"));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setIsAnimationCheckBoxChosen(SimpleBooleanProperty isAnimationCheckBoxChosen) {
        this.isAnimationCheckBoxChosen = isAnimationCheckBoxChosen;
    }

    public void fillMakeOrderData(Engine engine){
        // View:
        scrollPaneCenter.setContent(gridPaneTop);
        okButton.setVisible(true);
        checkoutButton.setVisible(false);
        approveButton.setVisible(false);
        itemsController.setProceedToCheckout(false);
        itemsController.setIsAnimationCheckBoxChosen(isAnimationCheckBoxChosen);
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
        mainController.setInDynamicProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();

        messageGeneratorController = createMessageGenerator();
        messageGeneratorController.setMessageLabelText("You decided to cancel your order");

        mainController.getAnchorPaneRight().getChildren().add(messageAnchorPane);
        AnchorPane.setTopAnchor(messageAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(messageAnchorPane, 0.0);
        AnchorPane.setRightAnchor(messageAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(messageAnchorPane, 0.0);
    }

    @FXML
    void okButtonAction(ActionEvent event) {
        okButtonIsClicked.set(true);
        okButton.setVisible(false);
        checkoutButton.setVisible(true);
        chooseItemLabel.setVisible(true);
        chooseItemLabel2.setVisible(true);
        questionImage.setVisible(true);

        List<ItemDTO> itemList, dummyList;
        // Get the items list:
        if(dynamicOrderRadioButtonClicked.getValue()){
            itemsController.getPriceColumn().setVisible(false);
            itemsController.getSalesColumn().setVisible(false);
            itemsController.getSellersColumn().setVisible(false);
            itemList = engine.getAllItemList();
        }
        else{   // Static order button clicked
            itemsController.getSalesColumn().setVisible(false);
            itemsController.getSellersColumn().setVisible(false);
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
        nextToDiscountButton.setVisible(true);
        nextToDiscountButton.setDisable(false);
        questionImage.setVisible(false);

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
            nextToDiscountButton.fire();
        }
        else{   // dynamicOrderRadioButtonClicked.getValue() == true
            showDynamicOrderProcess();

            cartList = engine.getSDM().summarizeDynamicOrder(itemIdToAmount, null, customer.getId());

            // Load dynamic order FXML file:
            dynamicOrderController = createDynamicOrderController();
            dynamicOrderController.fillDynamicOrderTableView(cartList);
            dynamicOrderController.getAnchorPane().visibleProperty().bind(isOrderReadyProperty);

            scrollPaneCenter.setContent(dynamicOrderAnchorPane);
            AnchorPane.setTopAnchor(dynamicOrderAnchorPane, 0.0);
            AnchorPane.setBottomAnchor(dynamicOrderAnchorPane, 0.0);
            AnchorPane.setRightAnchor(dynamicOrderAnchorPane, 0.0);
            AnchorPane.setLeftAnchor(dynamicOrderAnchorPane, 0.0);
        }
    }

    private void showDynamicOrderProcess(){
        taskGridPane.setVisible(true);
        Task<Boolean> currentRunningTask = new DynamicOrderTask(isOrderReadyProperty::set);

        // Bind task to UI components:
        taskMessageLabel.textProperty().bind(currentRunningTask.messageProperty());
        taskProgressBar.progressProperty().bind(currentRunningTask.progressProperty());
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format("%.0f",
                                Bindings.multiply(
                                        currentRunningTask.progressProperty(), 100)), " %"));

        new Thread(currentRunningTask).start();
    }

    @FXML
    void nextToDiscountButtonOnAction(ActionEvent event){
        taskGridPane.setVisible(false);
        nextToSummaryButton.setVisible(true);
        nextToSummaryButton.setDisable(false);
        nextToDiscountButton.setVisible(false);
        nextToDiscountButton.setDisable(true);


        Map<Integer, Double> itemIdToAmountDummy = new HashMap<>(itemIdToAmount);

        allDiscountsController = createAllDiscountsController();
        allDiscountsController.setEngine(engine);
        allDiscountsController.setItemsIdToAmount(itemIdToAmountDummy);

        if(staticOrderRadioButtonClicked.getValue()){
            // No available discounts on this order
            if(engine.getSDM().getStoreDiscounts(itemIdToAmount, store.getId()).isEmpty()){
                nextToSummaryButton.fire();
            }
            else{   // Show customer available discounts
                allDiscountsController.setStaticOrder(true);
                allDiscountsController.setStoreId(store.getId());
                allDiscountsController.fillAllDiscountDataInOrder(store.getId());
                scrollPaneCenter.setContent(allDiscountsController.getBorderPane());
            }
        }
        else{
            if(engine.getSDM().getDiscounts(itemIdToAmount).isEmpty()){
                nextToSummaryButton.fire();
            }
            else{
                allDiscountsController.setStaticOrder(false);
                allDiscountsController.fillAllDiscountDataInOrder();
                scrollPaneCenter.setContent(allDiscountsController.getBorderPane());
            }
        }
    }

    @FXML
    void nextToSummaryButtonAction(ActionEvent event){
        approveButton.setVisible(true);
        approveButton.setDisable(false);
        nextToSummaryButton.setVisible(false);
        nextToSummaryButton.setDisable(true);
        gridPaneSummary.setVisible(true);

        offerList = allDiscountsController.getOfferList();

        FlowPane flowPane = new FlowPane();
        scrollPaneCenter.setContent(flowPane);

        if(staticOrderRadioButtonClicked.getValue()){
            // Generate customer's cart:
            cart = engine.getSDM().summarizeStaticOrder(itemIdToAmount, offerList, store.getId(), customer.getId());

            // Fill cart controller data:
            cartController.fillGridPaneData(cart, store.getName(), store.getId(), store.getPPK(),
                    engine.getSDM().calculateDistanceStoreToCustomer(store.getId(), customer.getId()),
                    engine.getSDM().calculateDeliveryPrice(store.getId(), customer.getId()));

            totalItemsPrice.set(cart.getTotalItemsPrice());
            totalDeliveryPrice.set(cart.getDeliveryPrice());
            totalPrice.set(cart.getTotalOrderPrice());

            flowPane.getChildren().add(cartGridPane);
        }
        else{
            cartList = engine.getSDM().summarizeDynamicOrder(itemIdToAmount, offerList, customer.getId());
            for(CartDTO cart : cartList){
                CartController currentCartController = createCartController();
                currentCartController.fillGridPaneData(cart, cart.getStoreName(), cart.getStoreId(),
                        cart.getPPK(), cart.getDistanceFromStoreToCustomer(), cart.getDeliveryPrice());

                totalItemsPrice.set(totalItemsPrice.get() + cart.getTotalItemsPrice());
                totalDeliveryPrice.set(totalDeliveryPrice.get() + cart.getDeliveryPrice());
                totalPrice.set(totalPrice.get() + cart.getTotalOrderPrice());

                flowPane.getChildren().add(currentCartController.getGridPane());
            }
        }
    }

    @FXML
    void approveButtonAction(ActionEvent event) {
        //NOTE: change date in engine to local date?
        if(staticOrderRadioButtonClicked.get()){
            engine.getSDM().executeStaticOrder(cart, date, customer.getId());

        }
        else{   // staticOrderRadioButtonClicked.getValue() == true
            engine.getSDM().executeDynamicOrder(cartList, date, customer.getId());
        }

        mainController.setInDynamicProcedure(false);
        mainController.getAnchorPaneRight().getChildren().clear();

        messageGeneratorController = createMessageGenerator();
        messageGeneratorController.setMessageLabelText(String.format("Thank you %s for buying Super Duper Market",
                customer.getName()));

        mainController.getAnchorPaneRight().getChildren().add(messageAnchorPane);
        AnchorPane.setTopAnchor(messageAnchorPane, 0.0);
        AnchorPane.setBottomAnchor(messageAnchorPane, 0.0);
        AnchorPane.setRightAnchor(messageAnchorPane, 0.0);
        AnchorPane.setLeftAnchor(messageAnchorPane, 0.0);
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

    public AllDiscountsController createAllDiscountsController()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.ALL_DISCOUNT_ANCHOR_PANE);
            allDiscountAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MessageGeneratorController createMessageGenerator()
    {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(SDMResourcesConstants.MESSAGE_GENERATOR_ANCHOR_PANE);
            messageAnchorPane = loader.load();
            return loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
