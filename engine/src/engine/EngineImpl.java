package engine;

import DTO.RegionDTO;
import engine.accounts.Account;
import jaxb.schema.generated.*;
import sdm.SuperDuperMarket;
import sdm.SuperDuperMarketImpl;
import sdm.discount.Discount;
import sdm.discount.Offer;
import sdm.item.Item;
import sdm.store.Store;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Adding and retrieving regions is synchronized and in that manner - these actions are thread safe
 */
public class EngineImpl implements Engine{

    static public final int MIN_RANGE = 1;
    static public final int MAX_RANGE = 50;
    private final Map<String, SuperDuperMarket> regionNameToSDM;
    private boolean validFileLoaded;

    public EngineImpl() {
        this.validFileLoaded = false;
        regionNameToSDM = new HashMap<>();
    }

    @Override
    public void loadDataFromFile(InputStream fileInputStream, String ownerName) {
        SuperDuperMarketDescriptor SDMDescriptor = XmlFileHandler.generateJaxbClasses(fileInputStream);
        XmlFileHandler.checkValidXmlFile(SDMDescriptor, regionNameToSDM);

        SuperDuperMarketImpl superDuperMarket = SDMDescriptorToSDMImpl(SDMDescriptor, ownerName);

        synchronized (this){
            regionNameToSDM.put(superDuperMarket.getRegionName(), superDuperMarket);
        }

        validFileLoaded = true;
    }

    @Override
    public synchronized List<RegionDTO> getAllRegionList() {
        List<RegionDTO> regionDTOList = new ArrayList<>();

        for(SuperDuperMarket SDM : regionNameToSDM.values()){
            regionDTOList.add(SDM.superDuperMarketToRegionDTO());
        }

        return regionDTOList;
    }

    @Override
    public synchronized SuperDuperMarket getRegionSDM(String regionName) {
        return regionNameToSDM.getOrDefault(regionName, null);
    }

    @Override
    public synchronized boolean isRegionNameExist(String regionName) {
        return regionNameToSDM.containsKey(regionName);
    }

    @Override
    public boolean validFileIsNotLoaded() {
        return !validFileLoaded;
    }

    private SuperDuperMarketImpl SDMDescriptorToSDMImpl(SuperDuperMarketDescriptor superDuperMarketDescriptor, String ownerName)
    {
        // Convert items list:
        Map<Integer, Item> itemIdToItem = new HashMap<>();
        List<SDMItem> SDMItemsList = superDuperMarketDescriptor.getSDMItems().getSDMItem();
        for(SDMItem item : SDMItemsList){
            itemIdToItem.put(item.getId(), new Item(item.getId(), item.getName().trim(), item.getPurchaseCategory().trim(), 0));
        }
        // Convert stores list:
        Map<Integer, Store> storeIdToStore = new HashMap<>();
        List<SDMStore> SDMStoresList = superDuperMarketDescriptor.getSDMStores().getSDMStore();
        for(SDMStore store : SDMStoresList){
            // Convert store's items:
            Map<Integer, Integer> storeItems = new HashMap<>();
            List<SDMSell> SDMPrices = store.getSDMPrices().getSDMSell();
            for (SDMSell sell : SDMPrices) {
                storeItems.put(sell.getItemId(), sell.getPrice());
            }
            // Convert store's discounts:
            List<Discount> discountList = convertStoreDiscounts(store.getSDMDiscounts());
            // Generate a new store in the system:
            storeIdToStore.put(store.getId(), new Store(store.getId(), store.getName().trim(), storeItems,
                    new Point(store.getLocation().getX(), store.getLocation().getY()), store.getDeliveryPpk(), discountList, ownerName));
        }

        return new SuperDuperMarketImpl(storeIdToStore, itemIdToItem, superDuperMarketDescriptor.getSDMZone().getName(), ownerName);
    }

    // 'sdmDiscounts' can be null if store didn't define any discounts,
    // in that case return an empty discount list in order to enable adding discounts in future.
    private List<Discount> convertStoreDiscounts(SDMDiscounts sdmDiscounts){
        List<Discount> discountList = new ArrayList<>();
        // If store has discounts:
        if(sdmDiscounts != null){
            List<SDMDiscount> sdmDiscountList = sdmDiscounts.getSDMDiscount();
            for(SDMDiscount discount : sdmDiscountList){
                discountList.add(new Discount(discount.getName().trim(),
                        discount.getIfYouBuy().getItemId(),
                        discount.getIfYouBuy().getQuantity(),
                        discount.getThenYouGet().getOperator().trim(),
                        convertDiscountOffers(discount.getThenYouGet().getSDMOffer())));
            }
        }
        return discountList;
    }

    // If this function was called, it is schema guaranteed that 'offerSDMList isn't null.
    private List<Offer> convertDiscountOffers(List<SDMOffer> offerSDMList){
        List<Offer> offerList = new ArrayList<>();
        for(SDMOffer offer : offerSDMList){
            offerList.add(new Offer(offer.getItemId(),
                    offer.getQuantity(),
                    offer.getForAdditional()));
        }
        return offerList;
    }
}
