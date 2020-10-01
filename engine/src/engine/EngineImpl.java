package engine;

import DTO.CustomerDTO;
import DTO.ItemDTO;
import DTO.OrderDTO;
import DTO.StoreDTO;
import jaxb.schema.generated.*;
import sdm.SuperDuperMarket;
import sdm.SuperDuperMarketImpl;
import sdm.user.User;
import sdm.discount.Discount;
import sdm.discount.Offer;
import sdm.item.Item;
import sdm.order.OrderDynamic;
import sdm.order.OrderStatic;
import sdm.store.Store;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngineImpl implements Engine{

    static public final int MIN_RANGE = 1;
    static public final int MAX_RANGE = 50;
    private SuperDuperMarketImpl superDuperMarketImpl;
    private boolean validFileLoaded;

    public EngineImpl() {
        this.validFileLoaded = false;
    }

    @Override
    public void loadDataFromFile(String pathName) {
        SuperDuperMarketDescriptor SDMDescriptor = XmlFileHandler.generateJaxbClasses(pathName);
        XmlFileHandler.checkValidXmlFile(SDMDescriptor);
        convertJaxbGeneratedClassesToProjectClasses(SDMDescriptor);
        validFileLoaded = true;
    }

    @Override
    public boolean validFileIsNotLoaded() {
        return !validFileLoaded;
    }

    @Override
    public SuperDuperMarket getSDM() {
        return superDuperMarketImpl;
    }

    @Override
    public List<StoreDTO> getAllStoreList() {
        List<StoreDTO> storesDTOList = new ArrayList<>();
        superDuperMarketImpl.getStoreIdToStore()
                .forEach((id, store) -> storesDTOList.add(new StoreDTO(id,
                        store.getName(),
                        getItemsSoldByStore(store),
                        getStoreOrdersList(id),
                        store.getLocation().x,
                        store.getLocation().y,
                        store.getPPK(),
                        store.getTotalDeliveryIncome()
                        )));
        return storesDTOList;
    }

    @Override
    public List<ItemDTO> getAllItemList() {
        List<ItemDTO> itemsDTOList = new ArrayList<>();
        superDuperMarketImpl.getItemIdToItem()
                .forEach((id, item) -> itemsDTOList.add(new ItemDTO(id,
                        item.getName(),
                        item.getPurchaseCategory().toString(),
                        superDuperMarketImpl.getNumOfSellersById(id),
                        superDuperMarketImpl.getAveragePriceById(id),
                        superDuperMarketImpl.getNumOfSalesById(id))));
        return itemsDTOList;
    }

    @Override
    public List<OrderDTO> getOrdersHistory() {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        superDuperMarketImpl.getOrderIdToOrder()
                .forEach((key, value) -> orderDTOList.add(value.convertOrderToOrderDTO(key)));
        return orderDTOList;
    }

    @Override
    public List<CustomerDTO> getAllCustomersList() {
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        superDuperMarketImpl.getCustomerIdToCustomer()
                .forEach((id, user) -> customerDTOList.add(new CustomerDTO(id,
                        user.getName(),
                        user.getLocation().x,
                        user.getLocation().y,
                        user.getOrdersIdList().size(),
                        user.getAverageItemsPrice(),
                        user.getAverageDeliveryPrice())));
        return customerDTOList;
    }

    //Gets a store's id and returns a list of orders from this store.
    private List<OrderDTO> getStoreOrdersList(int storeId){
        List<OrderDTO> orderDTOList = new ArrayList<>();

        superDuperMarketImpl.getStoreIdToStore()
                .get(storeId)
                .getOrders()
                .forEach((orderId)-> {
                    OrderStatic currentOrder = superDuperMarketImpl.getOrderIdToOrder().get(orderId);
                    if(currentOrder instanceof OrderDynamic){
                        orderDTOList.add(
                                ((OrderDynamic)currentOrder)    //Casting to Order Dynamic
                                        .getStoreIdToOrder().get(storeId)   //Get store's part in order
                                        .convertOrderToOrderDTO(orderId));     //Convert to DTO object
                    }
                    else{
                        orderDTOList.add(currentOrder.convertOrderToOrderDTO(orderId)); //NOTE: exception has occurred
                    }
                });
        return orderDTOList;
    }

    private List<ItemDTO> getItemsSoldByStore(Store store){
        List<ItemDTO> itemsDTOList = new ArrayList<>();
        store.getItemIdToPrice()
                .forEach((id, price) -> itemsDTOList.add(new ItemDTO(id,
                superDuperMarketImpl.getItemIdToItem().get(id).getName(),
                superDuperMarketImpl.getItemIdToItem().get(id).getPurchaseCategory().toString(),
                0, // Inside store number of sellers is irrelevant
                price,
                store.getItemIdToNumberOfSales().get(id))));
        return itemsDTOList;
    }

    private void convertJaxbGeneratedClassesToProjectClasses(SuperDuperMarketDescriptor superDuperMarketDescriptor)
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
                    new Point(store.getLocation().getX(), store.getLocation().getY()), store.getDeliveryPpk(), discountList));
        }
        // Convert customers list:
        Map<Integer, User> customerIdToCustomer = new HashMap<>();
        List<SDMCustomer> SDMCustomerList = superDuperMarketDescriptor.getSDMCustomers().getSDMCustomer();
        for(SDMCustomer customer : SDMCustomerList){
            customerIdToCustomer.put(customer.getId(), new User(customer.getId(), customer.getName().trim(),
                    new Point(customer.getLocation().getX(), customer.getLocation().getY())));
        }

        this.superDuperMarketImpl = new SuperDuperMarketImpl(storeIdToStore, itemIdToItem, customerIdToCustomer);
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
