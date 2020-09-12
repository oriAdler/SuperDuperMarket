package engine;

import exception.*;
import jaxb.schema.generated.*;

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

public class XmlFileHandler {
    private final static String JAXB_XML_SDM_PACKAGE_NAME = "jaxb.schema.generated";
    private final static String XML_FILE_SUFFIX = ".xml";
    
    public static SuperDuperMarketDescriptor generateJaxbClasses(String pathName){
        if(!pathName.toUpperCase().endsWith(XML_FILE_SUFFIX.toUpperCase())){
            throw new XmlFileException("File not loaded successfully: file is not a valid xml file");
        }
        else{
            try {
                InputStream inputStream = new FileInputStream(new File(pathName));
                return deserializeFrom(inputStream);
            } catch (FileNotFoundException e) {
                throw new XmlFileException("File not loaded successfully: file was not found");
            } catch (JAXBException e){
                throw new XmlFileException("File not loaded successfully: A JAXB error has occurred");
            }
        }
    }

    public static void checkValidXmlFile(SuperDuperMarketDescriptor SDMDescriptor){
        checkTwoItemsWithIdenticalId(SDMDescriptor.getSDMItems().getSDMItem());
        checkTwoStoresWithIdenticalId((SDMDescriptor.getSDMStores().getSDMStore()));
        checkAllReferencedItemsExist(SDMDescriptor.getSDMItems().getSDMItem(), SDMDescriptor.getSDMStores().getSDMStore());
        checkEveryItemIsReferencedByStore(SDMDescriptor.getSDMItems().getSDMItem(), SDMDescriptor.getSDMStores().getSDMStore());
        checkItemIsDefinedTwiceInStore(SDMDescriptor.getSDMStores().getSDMStore());
        checkAllStoresInRange(SDMDescriptor.getSDMStores().getSDMStore());

        checkTwoCustomersWithIdenticalId(SDMDescriptor.getSDMCustomers().getSDMCustomer());
        checkAllCustomersInRange(SDMDescriptor.getSDMCustomers().getSDMCustomer());

        checkTwoObjectsWithIdenticalLocation(SDMDescriptor.getSDMStores().getSDMStore(), SDMDescriptor.getSDMCustomers().getSDMCustomer());
        checkAllDiscountsAreValid(SDMDescriptor.getSDMStores().getSDMStore(), SDMDescriptor.getSDMItems().getSDMItem());
    }

    //Checks that a given coordinates (x,y) are between (MIN,MAX) range.
    public static Boolean inRange(Point location){
        return location.x >= EngineImpl.MIN_RANGE && location.x <= EngineImpl.MAX_RANGE
                && location.y>= EngineImpl.MIN_RANGE && location.y <= EngineImpl.MAX_RANGE;
    }

    private static SuperDuperMarketDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_SDM_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (SuperDuperMarketDescriptor) u.unmarshal(in);
    }

    private static void checkAllStoresInRange(List<SDMStore> storesList){
        for(SDMStore store : storesList){
            if(!inRange(new Point(store.getLocation().getX(), store.getLocation().getY()))){
                throw new ObjectNotInRangeException(store.getName(),
                        new Point(store.getLocation().getX(), store.getLocation().getY()));
            }
        }
    }

    private static void checkTwoItemsWithIdenticalId(List<SDMItem> itemsList){
        Set<Integer> dummySet  = new HashSet<>();
        Set<SDMItem> duplicateSet = itemsList
                .stream()
                .filter(i -> !dummySet.add(i.getId()))
                .collect(Collectors.toSet());
        if(!duplicateSet.isEmpty()){
            int duplicatedId = duplicateSet.iterator().next().getId();
            List<SDMItem> duplicatedList = itemsList
                    .stream()
                    .filter(i->i.getId()==duplicatedId)
                    .collect(Collectors.toList());
            throw new twoObjectsWithSameIdException(
                    "items", duplicatedList.get(0).getName(), duplicatedList.get(1).getName(), duplicatedId);
        }
    }

    private static void checkTwoStoresWithIdenticalId(List<SDMStore> storesList){
        Set<Integer> dummySet  = new HashSet<>();
        Set<SDMStore> duplicateSet = storesList
                .stream()
                .filter(i -> !dummySet.add(i.getId()))
                .collect(Collectors.toSet());
        if(!duplicateSet.isEmpty()){
            int duplicatedId = duplicateSet.iterator().next().getId();
            List<SDMStore> duplicatedList = storesList
                    .stream()
                    .filter(i->i.getId()==duplicatedId)
                    .collect(Collectors.toList());
            throw new twoObjectsWithSameIdException(
                    "stores", duplicatedList.get(0).getName(), duplicatedList.get(1).getName(), duplicatedId);
        }
    }

    private static void checkAllReferencedItemsExist(List<SDMItem> itemsList, List<SDMStore> storesList){
        Set<Integer> itemsIdSet = itemsList
                .stream()
                .map(SDMItem::getId)
                .collect(Collectors.toSet());
        for(SDMStore store : storesList) {
            List<SDMSell> sellList = store.getSDMPrices().getSDMSell();
            for(SDMSell sell : sellList){
                if(!itemsIdSet.contains(sell.getItemId())){
                            throw new invalidItemException(String.format(
                                    "File not loaded successfully: " +
                                            "item with id '%d' referenced in store '%s' does not exist",
                                    sell.getItemId(), store.getName()));
                    }
                }
            }
    }

    private static void checkEveryItemIsReferencedByStore(List<SDMItem> itemsList, List<SDMStore> storesList){
        Set<Integer> storesItemsIdsSet = new HashSet<>();
        for(SDMStore store : storesList) {
            storesItemsIdsSet.addAll(store.getSDMPrices().getSDMSell()
                    .stream()
                    .map(SDMSell::getItemId)
                    .collect(Collectors.toSet()));
        }
        for(SDMItem item : itemsList){
            if(!storesItemsIdsSet.contains(item.getId())){
                throw new invalidItemException(
                        String.format(
                                "File not loaded successfully: item '%s' with id '%d' was not referenced in any store",
                                item.getName(), item.getId()));
            }
        }
    }

    private static void checkItemIsDefinedTwiceInStore(List<SDMStore> storesList){
        for(SDMStore store : storesList){
            Set<Integer> dummySet  = new HashSet<>();
            Set<SDMSell> duplicateSet = store.getSDMPrices().getSDMSell()
                    .stream()
                    .filter(s -> !dummySet.add(s.getItemId()))
                    .collect(Collectors.toSet());
            if(!duplicateSet.isEmpty()){
                int duplicatedId = duplicateSet.iterator().next().getItemId();
                throw new invalidItemException(String.format(
                        "File not loaded successfully: " +
                                "item with id '%d' is defined twice in store %s",
                        duplicatedId, store.getName()));
            }
        }
    }

    private static void checkTwoCustomersWithIdenticalId(List <SDMCustomer> customersList){
        // Find duplicates via a dummy set:
        Set<Integer> dummySet  = new HashSet<>();
        Set<SDMCustomer> duplicateSet = customersList
                .stream()
                .filter(customer -> !dummySet.add(customer.getId()))
                .collect(Collectors.toSet());
        // If duplicates were found, generate an adequate exception:
        if(!duplicateSet.isEmpty()){
            // Get some duplicates id:
            int duplicatedId = duplicateSet.iterator().next().getId();
            List<SDMCustomer> duplicatedList = customersList
                    .stream()
                    .filter(customer->customer.getId()==duplicatedId)
                    .collect(Collectors.toList());
            throw new twoObjectsWithSameIdException(
                    "customers", duplicatedList.get(0).getName(), duplicatedList.get(1).getName(), duplicatedId);
        }
    }

    private static void checkTwoObjectsWithIdenticalLocation(List<SDMStore> storesList, List<SDMCustomer> customersList) {
        // Find duplicates via dummy set:
        Set<Point> dummySet = new HashSet<>();
        // Find duplicates among stores:
        Set<Point> duplicateSet = storesList
                .stream()
                .filter(store->!dummySet.add(
                        new Point(store.getLocation().getX(), store.getLocation().getY())))
                .map(store->
                        new Point(store.getLocation().getX(), store.getLocation().getY()))
                .collect(Collectors.toSet());
        // Find duplicates among customers:
        duplicateSet.addAll(customersList.
                stream().filter(customer->!dummySet.add(
                        new Point(customer.getLocation().getX(), customer.getLocation().getY())))
                .map(customer->
                        new Point(customer.getLocation().getX(), customer.getLocation().getY()))
                .collect(Collectors.toSet()));
        if(!duplicateSet.isEmpty()){
            Point duplicatedLocation = duplicateSet.iterator().next();
            throw new invalidLocationException(
                    String.format("Invalid File: Location [%.0f,%.0f] is inhibited by more than one customer/store",
                            duplicatedLocation.getX(), duplicatedLocation.getY()));
        }
    }

    private static void checkAllCustomersInRange(List<SDMCustomer> customerList){
        for(SDMCustomer customer : customerList){
            if(!inRange(new Point(customer.getLocation().getX(), customer.getLocation().getY()))){
                throw new ObjectNotInRangeException(customer.getName(),
                        new Point(customer.getLocation().getX(), customer.getLocation().getY()));
            }
        }
    }

    private static void checkAllDiscountsAreValid(List<SDMStore> storeList, List<SDMItem> itemsList){
        List<Integer> discountItemsIdList = new ArrayList<>();
        // Collect SDM item's id list:
        List<Integer> SDMItemsIdList = itemsList
                .stream()
                .map(SDMItem::getId)
                .collect(Collectors.toList());

        // Check all stores discounts:
        storeList.forEach(store->{
            // If store has discounts:
            if(store.getSDMDiscounts() != null){
                // Collect store's items id:
                List<Integer> storeItemsIdList = store.getSDMPrices().getSDMSell()
                        .stream()
                        .map(SDMSell::getItemId)
                        .collect(Collectors.toList());

                List<SDMDiscount> sdmDiscounts = store.getSDMDiscounts().getSDMDiscount();
                // Collect item's id from discount
                sdmDiscounts.forEach(discount -> {
                    discountItemsIdList.add(discount.getIfYouBuy().getItemId());
                    discountItemsIdList.addAll(discount.getThenYouGet().getSDMOffer()
                            .stream()
                            .map(SDMOffer::getItemId)
                            .collect(Collectors.toList()));
                });
                // Check if all items in discount sold by store and exist in system:
                if(!SDMItemsIdList.containsAll(discountItemsIdList) || !storeItemsIdList.containsAll(discountItemsIdList)){
                    generateInvalidItemInDiscountException(SDMItemsIdList, storeItemsIdList, discountItemsIdList, store.getName());
                }
                // Clear item's id list before inquiring next store:
                discountItemsIdList.clear();
            }
        });
    }

    private static void generateInvalidItemInDiscountException(List<Integer> SDMItemsIdList,
                                                               List<Integer> storeItemsIdList,
                                                               List<Integer> discountItemsIdList, String storeName){
        // Make a copy of the list for the second check:
        List<Integer> dummyDiscountItemsIdList = new ArrayList<>(discountItemsIdList);
        int itemId;

        // Check item exist in the system:
        discountItemsIdList.removeAll(SDMItemsIdList);
        if(!discountItemsIdList.isEmpty()){
            itemId = discountItemsIdList.iterator().next();
            throw new invalidLocationException(String.format(
                    "Item with id '%d' defined in discount in store '%s' doesn't exist in System", itemId, storeName));
        }
        else{   // Check item sold by the store:
            dummyDiscountItemsIdList.removeAll(storeItemsIdList);
            if(!dummyDiscountItemsIdList.isEmpty()){
                itemId = dummyDiscountItemsIdList.iterator().next();
                throw new invalidLocationException(String.format(
                        "Item with id '%d' defined in discount in store '%s' doesn't sold by the store", itemId, storeName));
            }
        }
    }
}
