package course.java.sdm.engine;

import course.java.sdm.SuperDuperMarket;
import course.java.sdm.jaxb.schema.generated.SDMItem;
import course.java.sdm.jaxb.schema.generated.SDMSell;
import course.java.sdm.jaxb.schema.generated.SDMStore;
import course.java.sdm.jaxb.schema.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class EngineImpl implements Engine{
    SuperDuperMarket superDuperMarket;

    @Override
    public void loadDataFromFile(String pathName) {
        SuperDuperMarketDescriptor SDMDescriptor = XmlFileHandler.generateJaxbClasses(pathName);
        XmlFileHandler.checkValidXmlFile(SDMDescriptor);
    }

    @Override
    public String showStores() {
        return null;
    }

    @Override
    public String showAllItems() {
        return null;
    }

    @Override
    public void makeOrder() {

    }

    @Override
    public String showOrdersHistory() {
        return null;
    }



//    private void convertJaxbGeneratedClassesToProjectClasses(SuperDuperMarketDescriptor superDuperMarketDescriptor)
//    {
//        //Convert items list.
//        Map<Integer, Item> itemsMap = new HashMap<>();
//        List<SDMItem> SDMItemsList = superDuperMarketDescriptor.getSDMItems().getSDMItem();
//        for(SDMItem item : SDMItemsList){
//            if(itemsMap.containsKey(item.getId())) {
//                //Throw exception? two items with identical id
//            }
//            itemsMap.put(item.getId(), new Item(item.getId(), item.getName(), item.getPurchaseCategory()));
//        }
//
//        //Convert stores list
//        Map<Integer, Store> storesMap = new HashMap<>();
//        List<SDMStore> SDMStoresList = superDuperMarketDescriptor.getSDMStores().getSDMStore();
//        for(SDMStore store : SDMStoresList){
//            if(storesMap.containsKey(store.getId())){
//                //Throw exception? two stores with identical id
//            }
//            Map<Integer, Item> storeItems = new HashMap<>();
//            List<SDMSell> SDMPrices = store.getSDMPrices().getSDMSell();
//            for (SDMSell sell : SDMPrices) {
//                if(!itemsMap.containsKey(sell.getItemId())){
//                    //Throw exception, item doesn't exist
//                }
//                if(storeItems.containsKey(sell.getItemId())){
//                    //Throw exception, two items with identical id
//                }
//                Item newItem = itemsMap.get(sell.getItemId());
//                newItem.setPrice(sell.getPrice());
//                storeItems.put(sell.getItemId(), new Item(newItem.));
//            }
//            //check location
//            storesMap.put(store.getId(), new Store(store.getId(), store.getName(), storeItems,
//                    new Point(store.getLocation().getX(), store.getLocation().getY()), store.getDeliveryPpk()));
//        }
//
//        this.superDuperMarket = new SuperDuperMarket(storesMap, itemsMap);
//    }
}
