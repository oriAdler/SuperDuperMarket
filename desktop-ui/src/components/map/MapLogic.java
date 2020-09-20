package components.map;

import DTO.CustomerDTO;
import DTO.LocationDTO;
import DTO.StoreDTO;
import engine.Engine;

// X coordinate -> width
// Y coordinate -> height
public class MapLogic {
    private int width;
    private int height;
    LocationDTO[][] matrix;

    public MapLogic(int width, int height) {
        this.width = width + 1;
        this.height = height + 1;
        matrix = new LocationDTO[width][height];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void fillMapLogic(Engine engine){
        for(StoreDTO store : engine.getAllStoreList()){
            matrix[store.getxLocation()-1][store.getyLocation()-1] = store;
        }
        for(CustomerDTO customer : engine.getAllCustomersList()){
            matrix[customer.getXLocation()-1][customer.getYLocation()-1] = customer;
        }
    }

    public boolean isStore(int x, int y){
        return matrix[x][y] instanceof StoreDTO;
    }

    public StoreDTO getStore(int x, int y){
        return (StoreDTO)matrix[x][y];
    }

    public boolean isCustomer(int x, int y){
        return matrix[x][y] instanceof CustomerDTO;
    }
}
