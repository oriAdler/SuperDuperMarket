const refreshRate = 2000; //milli seconds
const GET_REGION_NAME_URL = buildUrlWithContextPath("getRegion");
const ITEMS_TABLE_URL = buildUrlWithContextPath("itemsTable");
const STORES_LIST_URL = buildUrlWithContextPath("storesList");
const MAKE_ORDER_URL = buildUrlWithContextPath("makeOrder");
const ORDER_LIST_URL = buildUrlWithContextPath("ordersList");

//Global Variables:
var regionName;

$(function getRegionNameAndAdjustPage(){
    $.ajax({
        url: GET_REGION_NAME_URL,
        success: function(response){
            regionName = response;
            $("#logo").text(regionName);
        },
        error: function (error){
            console.log(error);
        }
    })
})

// items = a list of itemDTO objects:
//{id: 1,
// name: "Toilet Paper",
// category: "Quantity",
// numOfSellers: 2,
// price: "30.50",
// numOfSales: 0,
// }
function refreshItemsList(items){
    let itemTable = $('#itemsTable');

    //clear all current items
    itemTable.empty();

    //append headers
    $('<tr>' +
        '<th>Serial number</th>' +
        '<th>Name</th>' +
        '<th>Category</th>' +
        '<th>Sellers No.</th>' +
        '<th>Average price</th>' +
        '<th>Sales No.</th>' +
        '</tr>').appendTo(itemTable);

    //rebuild the items table:
    $.each(items || [], function(index,item){
        let price = Number.parseFloat(item.price).toFixed(2);

        $('<tr>' +
            '<td>' + item.id + '</td>' +
            '<td>' + item.name + '</td>' +
            '<td>' + item.category + '</td>' +
            '<td>' + item.numOfSellers + '</td>' +
            '<td>' + price + '&#8362' +'</td>' +
            '<td>' + item.numOfSales + '</td>' +
            '</tr>').appendTo(itemTable);
    })
}

function refreshStoresList(stores){
    let storesList = $('#storesList');

    storesList.empty();

    $.each(stores || [], function(index, store){
        let container = $('<div></div>').addClass("w3-row w3-white w3-margin-bottom w3-responsive").appendTo(storesList);

        let third = $('<div></div>').addClass("w3-third w3-container").appendTo(container);
        let itemsIncome = Number.parseFloat(store.totalItemsIncome).toFixed(2);
        let deliveryIncome = Number.parseFloat(store.totalDeliveryIncome).toFixed(2);

        let storeData = $('<div>' +
            '<h2>' + store.name + '</h2>' +
            '<h4>' + 'Serial No. - ' + store.id + '</h4>' +
            '<h4>' + 'Owner - ' + store.ownerName + '</h4>' +
            '<h4>' + 'Location - [' + store.xLocation + ',' + store.yLocation + ']' + '</h4>' +
            '<h4>' + 'Orders No. - ' + store.numOfOrders + '</h4>' +
            '<h4>' + 'Items income - ' + itemsIncome + '&#8362' + '</h4>' +
            '<h4>' + 'PPK - ' +  store.PPK + '&#8362' + '</h4>' +
            '<h4>' + 'Delivery Income - ' + deliveryIncome + '&#8362' + '</h4>' +
            '</div>')
            .addClass("w3-left-align")
            .appendTo(third);

        let twoThird = $('<div></div>').addClass("w3-twothird w3-container w3-responsive").appendTo(container);
        $('<h3>Items</h3>').appendTo(twoThird);
        let itemsTable = $('<table></table>').addClass("w3-striped w3-border w3-table-all w3-large").appendTo(twoThird);
        $('<tr>' +
            '<th>Serial No.</th>' +
            '<th>Name</th>' +
            '<th>Category</th>' +
            '<th>Price</th>' +
            '<th>Sales No.</th>' +
            '</tr>').appendTo(itemsTable);
        //ItemDTO
        // id: 1
        // name: "Toilet Paper"
        // category: "Quantity"
        // price: "10.00"
        // numOfSales: 10.0
        $.each(store.items || [], function (index, item){
            $('<tr>' +
                '<td>' + item.id + '</td>' +
                '<td>' + item.name + '</td>' +
                '<td>' + item.category + '</td>' +
                '<td>' + item.price + '&#8362' + '</td>' +
                '<td>' + item.numOfSales + '</td>' +
                '</tr>').appendTo(itemsTable);
        })
    })
}

//[{"id":1,
// "localDate":{"year":2020,"month":10,"day":20},
// "location":{"x":2,"y":2},
// "numOfStores":1,
// "numOfItems":2,
// "itemsPrice":1.0,
// "deliveryPrice":0.0,
// "totalPrice":1.0,
// "itemsList":
// [{"priceSum":0.5,
// "storeName":"Hakol beshekel",
// "storeId":2,
// "onDiscount":false,
// "id":2,
// "name":"Banana",
// "category":"Weight",
// "numOfSellers":0,
// "price":"1.00",
// "numOfSales":0.5,
// "isSoldByStore":true}]}
function refreshOrdersList(orders){
    let ordersTable = $('#ordersTable');

    //clear all current items
    ordersTable.empty();

    //append headers
    $('<tr>' +
        '<th>Serial number</th>' +
        '<th>Date</th>' +
        '<th>Location</th>' +
        '<th>Stores No.</th>' +
        '<th>Items No.</th>' +
        '<th>Items Price</th>' +
        '<th>Delivery Price</th>' +
        '<th>Total Price</th>' +
        '</tr>').appendTo(ordersTable);

    //rebuild the items table:
    $.each(orders || [], function(index,order) {
        let itemsPrice = Number.parseFloat(order.itemsPrice).toFixed(2);
        let deliveryPrice = Number.parseFloat(order.deliveryPrice).toFixed(2);
        let totalPrice = Number.parseFloat(order.totalPrice).toFixed(2);

        let orderRow = $('<tr>' +
            '<td>' + order.id + '</td>' +
            '<td>' + order.localDate.day + '-' + order.localDate.month + '-' + order.localDate.year + '</td>' +
            '<td>' + '[' + order.location.x + ',' + order.location.y + ']' + '</td>' +
            '<td>' + order.numOfStores + '</td>' +
            '<td>' + order.numOfItems + '</td>' +
            '<td>' + itemsPrice + '&#8362' + '</td>' +
            '<td>' + deliveryPrice + '&#8362' + '</td>' +
            '<td>' + totalPrice + '&#8362' + '</td>' +
            '</tr>').appendTo(ordersTable);

        //order's items table:
        let itemsTable = $('<table></table>').addClass("w3-striped w3-border w3-table-all w3-small w3-light-blue").appendTo(orderRow);
        $('<h4>Items</h4>').appendTo(itemsTable);
        $('<tr>' +
            '<th>Serial number</th>' +
            '<th>Name</th>' +
            '<th>Category</th>' +
            '<th>Store Id</th>' +
            '<th>Store Name</th>' +
            '<th>Amount</th>' +
            '<th>Price</th>' +
            '<th>Total Price</th>' +
            '<th>On Discount</th>' +
            '</tr>').appendTo(itemsTable);

        $.each(order.itemsList || [], function (index, item) {
            let itemPrice = Number.parseFloat(item.price).toFixed(2);
            let totalPrice = Number.parseFloat(item.priceSum).toFixed(2);

            $('<tr>' +
                '<td>' + item.id + '</td>' +
                '<td>' + item.name + '</td>' +
                '<td>' + item.category + '</td>' +
                '<td>' + item.storeId + '</td>' +
                '<td>' + item.storeName + '</td>' +
                '<td>' + item.numOfSales + '</td>' +
                '<td>' + itemPrice + '&#8362' + '</td>' +
                '<td>' + itemsPrice + '&#8362' + '</td>' +
                '<td>' + item.onDiscount + '</td>' +
                '</tr>').appendTo(itemsTable);
        })

        itemsTable.hide();

        orderRow.click(function (){
            itemsTable.toggle();
        })
    })
}

function ajaxItemsTable(){
    $.ajax({
        url: ITEMS_TABLE_URL,
        success: function (items){
            refreshItemsList(items);
        }
    })
}

function ajaxStoresList(){
    $.ajax({
        url: STORES_LIST_URL,
        success: function (stores){
            refreshStoresList(stores);
        }
    })
}

function ajaxOrderList(){
    $.ajax({
        url: ORDER_LIST_URL,
        success: function (orders){
            refreshOrdersList(orders);
        }
    })
}

$(function(){

    ajaxItemsTable();
    setInterval(ajaxItemsTable, refreshRate);

    ajaxStoresList();
    setInterval(ajaxStoresList, refreshRate);

    //TODO: interval or not?
    ajaxOrderList();
    //setInterval(ajaxOrderList, refreshRate * 5);
})

$(function setMakeOrderForm(){
    let storeSelect = $("#storeSelect");
    let orderSelect = $("#orderSelect");
    let storeLabel = $("#storeLabel");

    //stores list is hidden before static order was chosen
    storeSelect.hide();
    storeLabel.hide();
    //update current stores list:
    $.ajax({
        url: STORES_LIST_URL,
        success: function (stores){
            $.each(stores || [], function(index, store){
                storeSelect.append(new Option(store.name, store.id));
                // $('<option>' + store.name + '</option>')
                //     .val(store.id)
                //     .appendTo(storeSelect);
            })
        }
    })

    //set select order type:
    orderSelect.change(function (){
        if(orderSelect.val()=="static"){
            storeSelect.show();
            storeSelect.prop('required', true);
            storeLabel.show();
        }
        else{   //orderSelect.val()=="dynamic"
            storeSelect.hide();
            storeSelect.prop('required', false);
            storeLabel.hide();
        }
    })
});

$(function() {
    //add a function to the submit event
    $("#makeOrderForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: MAKE_ORDER_URL,
            timeout: 2000,
            error: function(errorObject) {
                console.error("Failed to login !");
                $("#error-placeholder").text(errorObject.responseText)
            },
            success: function(nextPageUrl) {
                window.location.assign(nextPageUrl);
                // Check browser support for web storage API
                if (typeof(Storage) !== "undefined") {
                    // Store order type & store:
                    localStorage.setItem("orderType", $("#orderSelect").val());
                    localStorage.setItem("store", $("#storeSelect option:selected").html());
                } else {
                    alert("Sorry, your browser does not support Web Storage...");
                }
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});