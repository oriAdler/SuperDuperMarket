package components.map;

import DTO.CustomerDTO;
import DTO.StoreDTO;
import engine.Engine;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MapView extends AnchorPane{

    final private double width;
    final private double height;
    private Group group;

    public MapView(double width, double height) {
        this.width = width;
        this.height = height;
        group = new Group();
    }

    public void fillMapView(Engine engine, AnchorPane anchorPane){
        for(CustomerDTO customer : engine.getAllCustomersList()){
            CustomerButton customerButton = new CustomerButton(customer.getId(),
                    customer.getName(), customer.getNumberOfOrders());
            group.getChildren().add(customerButton);
            anchorPane.getChildren().add(customerButton);
            AnchorPane.setLeftAnchor(customerButton, (customer.getXLocation()/width) * anchorPane.getWidth());
            AnchorPane.setTopAnchor(customerButton, (customer.getYLocation()/height) * anchorPane.getHeight());
        }

        for(StoreDTO store : engine.getAllStoreList()){
            StoreButton storeButton = new StoreButton(store.getId(),
                    store.getName(), store.getPPK(), store.getOrders().size());
            anchorPane.getChildren().add(storeButton);
            AnchorPane.setLeftAnchor(storeButton, (store.getxLocation()/width) * anchorPane.getWidth());
            AnchorPane.setTopAnchor(storeButton, (store.getyLocation()/height) * anchorPane.getHeight());
        }
    }
}

//
//import components.map.MapLogic;
//import engine.Engine;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.event.EventHandler;
//import javafx.scene.Node;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.Tooltip;
//import javafx.scene.effect.DropShadow;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//import javafx.scene.transform.Affine;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MapView extends AnchorPane {
//    double width;
//    double height;
//    private Canvas canvas;
//
//    private Label labelLocation;
//    private SimpleStringProperty labelLocationProperty;
//
//    Engine engine;
//
//    private Affine affine;
//
//    private MapLogic mapLogic;
//
//    public MapView() {
//        this.width = 600;
//        this.height = 600;
//        this.canvas = new Canvas(width, height);
//
//
//        this.affine = new Affine();
//
//        labelLocationProperty = new SimpleStringProperty();
//        labelLocation = new Label();
//        labelLocation.textProperty().bind(labelLocationProperty);
//
//        this.getChildren().addAll(this.canvas, labelLocation);
//    }
//
//    public void setEngine(Engine engine) {
//        this.engine = engine;
//    }
//
//    public void setMapLogic(MapLogic mapLogic) {
//        this.mapLogic = mapLogic;
//        this.affine.appendScale(width / (double)mapLogic.getWidth(), height / (double)mapLogic.getHeight());
//    }
//
//    public void draw(){
//        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
//        graphicsContext.setTransform(affine);
//
//        graphicsContext.setFill(Color.LIGHTBLUE);
//        graphicsContext.fillRect(0,0,600,600);
//
//        Map<String, Rectangle> tooltips = new HashMap<>();
//
//        // Fill the map with stores
//        graphicsContext.setFill(Color.BLUE);
//        for (int x = 0; x < mapLogic.getWidth() - 1; x++) {
//            for (int y = 0; y < mapLogic.getHeight() - 1; y++) {
//                if(mapLogic.isStore(x,y)){
//                    graphicsContext.drawImage(new Image("/image/store.png"),x,y,1,1);
//
////                    Button button = new Button();
////                    button.setPrefWidth(1.0);
////                    button.setPrefHeight(1.0);
////                    button.setScaleX(width / (double)mapLogic.getWidth());
////                    button.setScaleY(height / (double)mapLogic.getHeight());
////                    button.setTooltip(new Tooltip(mapLogic.getStore(x,y).getName()));
//                    Rectangle rectangle = new Rectangle(x,y,1,1);
//                    rectangle.setScaleX(width / (double)mapLogic.getWidth());
//                    rectangle.setScaleY(height / (double)mapLogic.getHeight());
//                    rectangle.setFill(Color.BURLYWOOD);
//
////                    Tooltip tooltip = new Tooltip();
////                    Tooltip.install(rectangle, tooltip);
////                    tooltip.setText(mapLogic.getStore(x,y).getName());
//
//                    tooltips.put(mapLogic.getStore(x,y).getName(), rectangle);
//                }
//            }
//        }
//
//        // Fill the map with customers
//        graphicsContext.setFill(Color.RED);
//        for (int x = 0; x < mapLogic.getWidth() - 1; x++) {
//            for (int y = 0; y < mapLogic.getHeight() - 1; y++) {
//                if(mapLogic.isCustomer(x,y)){
//                Image image = new Image("/image/customer.png");
//                graphicsContext.drawImage(image,x,y,1,1);
//                }
//            }
//        }
//
//        // Draw grid lines
//        graphicsContext.setStroke(Color.GRAY);
//        graphicsContext.setLineWidth(0.05);
//        // Draw vertical lines
//        for (int x = 0; x <= mapLogic.getWidth(); x++) {
//            graphicsContext.strokeLine(x, 0, x, mapLogic.getHeight());
//        }
//
//        // Draw horizontal lines
//        for (int y = 0; y <= mapLogic.getHeight(); y++) {
//            graphicsContext.strokeLine(0, y, mapLogic.getWidth(), y);
//        }
//
//        tooltips.forEach((color, bounds) -> {
//            graphicsContext.setFill(Color.PAPAYAWHIP);
//            graphicsContext.fillRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
//        });
//        setToolTips(canvas, tooltips);
//    }
//
//    private void setToolTips(Node node, Map<String, Rectangle> tooltips) {
//        Tooltip tooltip = new Tooltip();
//        Tooltip.install(node, tooltip);
//        node.setOnMouseMoved(e -> {
//            tooltips.forEach((detail, bounds) -> {
//                if (bounds.contains(e.getX(), e.getY())) {
//                    tooltip.setText(detail);
//                }
//            });
//        });
//        node.setOnMouseExited(e -> {
//            tooltip.hide();
//        });
//    }
//}
