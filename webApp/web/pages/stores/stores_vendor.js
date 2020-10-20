const refreshRate = 5000; //milli seconds
const GET_REGION_NAME_URL = buildUrlWithContextPath("getRegion");
const ITEMS_TABLE_URL = buildUrlWithContextPath("itemsTable");
const STORES_LIST_URL = buildUrlWithContextPath("storesList")
const FEEDBACKS_LIST_URL = buildUrlWithContextPath("feedbacksList");
const ADD_STORE_URL = buildUrlWithContextPath("addStore");

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
            '<td>' + '<span>&#8362</span>' + price + '</td>' +
            '<td>' + item.numOfSales + '</td>' +
            '</tr>').appendTo(itemTable);
    })
}

function refreshStoresList(stores){
    let storesList = $('#storesList');

    storesList.empty();

    $.each(stores || [], function(index, store){
        let container = $('<div></div>').addClass("w3-row w3-white w3-margin-bottom").appendTo(storesList);

        let third = $('<div></div>').addClass("w3-third w3-container").appendTo(container);
        let itemsIncome = Number.parseFloat(store.totalItemsIncome).toFixed(2);
        let deliveryIncome = Number.parseFloat(store.totalDeliveryIncome).toFixed(2);

        let storeData = $('<div>' +
            '<h2>' + store.name + '</h2>' +
            '<h4>' + 'Serial No. - ' + store.id + '</h4>' +
            '<h4>' + 'Owner - ' + store.ownerName + '</h4>' +
            '<h4>' + 'Location - [' + store.xLocation + ',' + store.yLocation + ']' + '</h4>' +
            '<h4>' + 'Orders No. - ' + store.numOfOrders + '</h4>' +
            '<h4>' + 'Items income - ' + '<span>&#8362</span>' + itemsIncome + '</h4>' +
            '<h4>' + 'PPK - ' +  '<span>&#8362</span>' + store.PPK + '</h4>' +
            '<h4>' + 'Delivery Income - ' + '<span>&#8362</span>' + deliveryIncome + '</h4>' +
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
                '<td>' + '<span>&#8362</span>' + item.price + '</td>' +
                '<td>' + item.numOfSales + '</td>' +
                '</tr>').appendTo(itemsTable);
        })

        //orders history
        // "orderDTOList":[
        // {"id":1, "localDate":{"year":2020,"month":10,"day":28}, "customerName":"tomer", "location":{"x":1,"y":1},
        // "numOfStores":1, "numOfItems":3, "itemsPrice":25.0, "deliveryPrice":108.16653826391968, "totalPrice":133.16653826391968, "itemsList":[
        // {"priceSum":20.0,"storeName":"super baba","storeId":1,"onDiscount":false,"id":1,"name":"Ketshop","category":"Quantity","numOfSellers":0,"price":"20.00","numOfSales":1.0,"isSoldByStore":true},
        let ordersContainer = $('<div></div>').addClass("w3-row w3-white w3-margin-bottom w3-container w3-responsive").appendTo(container);
        $('<h3>Orders History</h3>').appendTo(ordersContainer);

        let ordersTable = $('<table></table>').addClass("w3-striped w3-border w3-table-all w3-large w3-hoverable").appendTo(ordersContainer);
        let orderHeaders = $('<tr>' +
            '<th>Serial No.</th>' +
            '<th>Date</th>' +
            '<th>Customer Name</th>' +
            '<th>Customer Location</th>' +
            '<th>Items No.</th>' +
            '<th>Items Price</th>' +
            '<th>Delivery Price</th>' +
            '</tr>').appendTo(ordersTable);

        if(store.orderDTOList.length === 0){  //table is empty
            $('<tr>' +
                '<td>' + 'No content in table' + '</td>' +
                '<td>' + ' ' + '</td>' +
                '<td>' + ' ' + '</td>' +
                '<td>' + ' ' + '</td>' +
                '<td>' + ' ' + '</td>' +
                '<td>' + ' ' + '</td>' +
                '<td>' + ' ' + '</td>' +
                '</tr>').appendTo(ordersTable);
        }

        //rebuild orders table:
        $.each(store.orderDTOList || [], function(index,order) {
            let itemsPrice = Number.parseFloat(order.itemsPrice).toFixed(2);
            let deliveryPrice = Number.parseFloat(order.deliveryPrice).toFixed(2);

            let orderRow = $('<tr>' +
                '<td>' + order.id + '</td>' +
                '<td>' + order.localDate.day + '-' + order.localDate.month + '-' + order.localDate.year + '</td>' +
                '<td>' + order.customerName + '</td>' +
                '<td>' + '[' + order.location.x + ',' + order.location.y + ']' + '</td>' +
                '<td>' + order.numOfItems + '</td>' +
                '<td>' + '<span>&#8362</span>' + itemsPrice + '</td>' +
                '<td>' + '<span>&#8362</span>' + deliveryPrice + '</td>' +
                '</tr>').appendTo(ordersTable);

            //order's items table:
            let itemsTable = $('<table></table>').addClass("w3-striped w3-border w3-table-all w3-small w3-light-blue").appendTo(orderRow);
            $('<h4>Items</h4>').appendTo(itemsTable);
            $('<tr>' +
                '<th>Serial number</th>' +
                '<th>Name</th>' +
                '<th>Category</th>' +
                '<th>Amount</th>' +
                '<th>Price</th>' +
                '<th>Total Price</th>' +
                '<th>On Discount</th>' +
                '</tr>').appendTo(itemsTable);

            $.each(order.itemsList || [], function (index, item) {
                let itemPrice = Number.parseFloat(item.price).toFixed(2);
                let priceSum = Number.parseFloat(item.priceSum).toFixed(2);

                $('<tr>' +
                    '<td>' + item.id + '</td>' +
                    '<td>' + item.name + '</td>' +
                    '<td>' + item.category + '</td>' +
                    '<td>' + item.numOfSales + '</td>' +
                    '<td>' + '<span>&#8362</span>' + itemPrice + '</td>' +
                    '<td>' + '<span>&#8362</span>' + priceSum + '</td>' +
                    '<td>' + item.onDiscount + '</td>' +
                    '</tr>').appendTo(itemsTable);
            })

            itemsTable.hide();

            orderRow.click(function (){
                itemsTable.toggle();
            })
        })
    })
}

//[{"customerName":"tomer",
// "localDate":{"year":2020,"month":10,"day":7},
// "rating":4,
// "feedback":"Good store!"}]

function refreshFeedbacksList(feedbacks){
    let feedbacksList = $('#feedbacksList');

    feedbacksList.empty();

    if(feedbacks.length === 0){
        $('<p>' + '<i>' + 'No feedbacks have been received yet' + '</i>' + '</p>')
            .addClass("w3-row w3-white w3-margin-bottom w3-card-2 w3-padding-large")
            .appendTo(feedbacksList);
    }

    $.each(feedbacks || [], function(index, feedback) {
        let container = $('<div></div>').addClass("w3-row w3-white w3-margin-bottom w3-card-4 w3-padding-large")
            .appendTo(feedbacksList);

        let feedbackData = $('<div>' +
            '<h4>' + 'Date - ' + feedback.localDate.day + '-' + feedback.localDate.month + '-' + feedback.localDate.year + '</h4>' +
            '<h4>' + 'Rating - ' + feedback.rating + '/5' + '</h4>').addClass("w3-left-align").appendTo(container);
        if(feedback.feedback !== ""){
            $('<div>' + '<h4>' + 'Feedback' + '</h4>' +
                '<p>' + '"' + feedback.feedback + '"' + '</p>' + '</div>')
                .addClass("w3-left-align w3-light-gray w3-padding-large")
                .appendTo(feedbackData);
        }
        $('<h4 class="w3-right-align">' + feedback.customerName + '</h4>' +
        '</div>')
        .addClass("w3-left-align")
        .appendTo(feedbackData);
    });
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

function ajaxFeedbacksList(){
    $.ajax({
        url: FEEDBACKS_LIST_URL,
        success: function (feedbacks){
            refreshFeedbacksList(feedbacks);
        }
    })
}

$(function(){
    ajaxItemsTable();
    // setInterval(ajaxItemsTable, refreshRate);

    //TODO: consider another method as items in order's history is pop-up window
    ajaxStoresList();
    setInterval(ajaxStoresList, refreshRate);

    ajaxFeedbacksList();
    setInterval(ajaxFeedbacksList, refreshRate);
})

$(function() {
    //add a function to the submit event
    $("#addStoreForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: ADD_STORE_URL,
            timeout: 2000,
            error: function(errorObject) {
                console.error("Failed to add store !");
                $("#error-placeholder").text(errorObject.responseText)
            },
            success: function(nextPageUrl) {
                window.location.assign(nextPageUrl);
            }
        });

        return false;
    });
});