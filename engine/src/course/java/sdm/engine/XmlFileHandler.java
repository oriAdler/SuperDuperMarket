package course.java.sdm.engine;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class XmlFileHandler {
    private final static String JAXB_XML_SDM_PACKAGE_NAME = "course.java.sdm.jaxb.schema.generated";

    static public SuperDuperMarketDescriptor generateJaxbClasses(String pathName){
        try {
            InputStream inputStream = new FileInputStream(new File(pathName));
            return deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;    //note: exception revise
    }

    static public void checkValidXmlFile(SuperDuperMarketDescriptor SDMDescriptor){
        if(twoItemsWithIdenticalId(SDMDescriptor.getSDMItems().getSDMItem())){
            System.out.println("Two items with identical ids");
        }
        if(twoStoresWithIdenticalId((SDMDescriptor.getSDMStores().getSDMStore()))){
            System.out.println("Two items with stores ids");
        }
        if(!allReferencedItemsExist(SDMDescriptor.getSDMItems().getSDMItem(),
                SDMDescriptor.getSDMStores().getSDMStore())){
            System.out.println("A reference to an item that doesn't exist");
        }
        if(!everyItemIsReferencedByStore(SDMDescriptor.getSDMItems().getSDMItem(),
                SDMDescriptor.getSDMStores().getSDMStore())){
            System.out.println("There is an item which not sold by any store");
        }
        if(itemIsDefinedTwiceInStore(SDMDescriptor.getSDMStores().getSDMStore())){
            System.out.println("There is a store with an item defined twice");
        }
        if(!allStoresInRange(SDMDescriptor.getSDMStores().getSDMStore())){
            System.out.println("Not all stores are in range [1,50]");
        }
    }

    private static SuperDuperMarketDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_SDM_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (SuperDuperMarketDescriptor) u.unmarshal(in);
    }

    static private Boolean allStoresInRange(List<SDMStore> storesList){
        for(SDMStore store : storesList){
            if(!inRange(new Point(store.getLocation().getX(), store.getLocation().getY()))){
                return false;
            }
        }
        return true;
    }

    static private Boolean inRange(Point location){
        return location.x >= 1 && location.y <= 50;
    }

    static private Boolean twoItemsWithIdenticalId(List<SDMItem> itemsList){
        Set<Integer> itemsIdSet = itemsList
                .stream()
                .map(SDMItem::getId)
                .collect(Collectors.toSet());
        return itemsIdSet.size() < itemsList.size();
    }

    static private Boolean twoStoresWithIdenticalId(List<SDMStore> storesList){
        Set<Integer> storesIdSet = storesList
                .stream()
                .map(SDMStore::getId)
                .collect(Collectors.toSet());
        return storesIdSet.size() < storesList.size();
    }

    static private Boolean allReferencedItemsExist(List<SDMItem> itemsList, List<SDMStore> storesList){
        List<Integer> itemsIdList = itemsList
                .stream()
                .map(SDMItem::getId)
                .collect(Collectors.toList());
        for(SDMStore store : storesList){
            List<Integer> storeItemsIdList = store.getSDMPrices().getSDMSell()
                    .stream()
                    .map(SDMSell::getItemId)
                    .collect(Collectors.toList());
            if(!itemsIdList.containsAll(storeItemsIdList)){
                return false;
            }
        }
        return true;
    }

    static private Boolean everyItemIsReferencedByStore(List<SDMItem> itemsList, List<SDMStore> storesList){
        Set<Integer> storesItemsIdsSet = new HashSet<>();
        for(SDMStore store : storesList) {
            storesItemsIdsSet.addAll(store.getSDMPrices().getSDMSell()
                    .stream()
                    .map(SDMSell::getItemId)
                    .collect(Collectors.toSet()));
        }
        Set<Integer> itemsIdSet = itemsList
                .stream()
                .map(SDMItem::getId)
                .collect(Collectors.toSet());
        return storesItemsIdsSet.containsAll(itemsIdSet);
    }

    static private Boolean itemIsDefinedTwiceInStore(List<SDMStore> storesList){
        for(SDMStore store : storesList){
            Set<Integer> storeItemsIdsSet = store.getSDMPrices().getSDMSell()
                    .stream()
                    .map(SDMSell::getItemId)
                    .collect(Collectors.toSet());
            if(storeItemsIdsSet.size()<store.getSDMPrices().getSDMSell().size()){
                return true;
            }
        }
        return false;
    }
}
