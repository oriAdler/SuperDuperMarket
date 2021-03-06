
const ORDER_ITEMS_TABLE = buildUrlWithContextPath("orderItemsList");
const GET_REGION_NAME_URL = buildUrlWithContextPath("getRegion");
const CHOOSE_ITEMS_URL = buildUrlWithContextPath("chooseItems");
const CHOOSE_DISCOUNTS_URL = "../chooseDiscounts/choose_discounts.html";
const GET_ORDER_TYPE_URL = buildUrlWithContextPath("getOrderType");

//Global variables:
var orderType;

//response from server returns with one extra blank line, therefor trim is necessary.
$(function getOrderTypeAndAdjustPage(){
    $.ajax({
        url: GET_ORDER_TYPE_URL,
        success: function(response){
            orderType = response.trim();
        },
        error: function (error){
            console.log(error);
        }
    })
})

// ItemDTO object:
//{"id":1,
// "name":"Toilet Paper",
// "category":"Quantity",
// "price":"10.00",
// "isSoldByStore":true}
function refreshItemsTable(items){
    let itemTable = $('#itemsTable');

    //clear all current items
    itemTable.empty();

    //append headers
    let tableHeader = $('<tr>' +
        '<th>Serial number</th>' +
        '<th>Name</th>' +
        '<th>Category</th>');
    if(orderType=="static"){
        tableHeader.append('<th>Price</th>');
    }
    tableHeader.append(
        '<th>Cart</th>' +
        '</tr>');
    tableHeader.appendTo(itemTable);

    //rebuild the items table:
    $.each(items || [], function(index,item){
        let itemPrice = item.isSoldByStore ? '<span>&#8362</span>' + item.price : "N/A";
        let tableRow = $('<tr>' +
            '<td class="id">' + item.id + '</td>' +
            '<td>' + item.name + '</td>' +
            '<td>' + item.category + '</td>');
        if(orderType=="static"){
            tableRow.append('<th>' + itemPrice + '</th>');
        }
        tableRow.append('</tr>');
        tableRow.appendTo(itemTable);

        if(item.isSoldByStore) {
            tableRow.append('<td>' + '<button>Add to Cart</button>' + '</td>')
                .find("button")
                .addClass("w3-button w3-block w3-green")
                .click(function (){
                    console.log()
                        let buttonCell = tableRow.find("button").parent();
                        buttonCell.empty();
                        if(item.category==="Quantity"){
                            buttonCell.append('<input type="number" id="amount" name="amount" min="1" step="1" value="1">');
                            tableRow.find("input").attr('name', item.id);
                        }
                        else{   //item.category==="Quantity"
                            buttonCell.append('<input type="number" id="amount" name="amount" min="0.5" step="0.1" value="0.5">');
                            tableRow.find("input").attr('name', item.id);
                        }
                        tableRow.addClass("inCart");    //in order to extract from table and send to server.
                });
        }
        else{
            tableRow.append('<td>Not Sold</td>')
        }
    });
}

$(function ajaxOrderItemsList(){
    $.ajax({
        url: ORDER_ITEMS_TABLE,
        error(errorObject){
            console.log("error: " + errorObject.responseText);
            $("#check").text(errorObject.responseText);
        },
        success: function (items){
            refreshItemsTable(items);
        }
    })
});

function showOrderSummary(carts){
    //clear page old details - choose item form
    let items = $("#items");
    items.find("h2").text("ORDER SUMMARY");
    let innerDiv = $("#innerDiv");
    innerDiv.empty();

    //build order html page:
    //build order summary table:
    let orderTable = $('<tr>' +
        '<th>Serial No.</th>' +
        '<th>Name</th>' +
        '<th>Location</th>' +
        '<th>Distance</th>' +
        '<th>PPK</th>' +
        '<th>Delivery Price</th>' +
        '<th>Products No.</th>' +
        '<th>Items Price</th>' +
        '</tr>');
    orderTable.addClass("w3-striped w3-border w3-table-all w3-large")

    //CartDTO object:
    // PPK: 0
    // deliveryPrice: 0
    // distanceFromStoreToCustomer: 4
    // items: [{…}]
    // itemsNumber: 1
    // storeId: 2
    // storeName: "Hakol beshekel"
    // storeXLocation: 1
    // storeYLocation: 5
    // totalItemsPrice: 0.5
    // totalOrderPrice: 0.5
    $.each(carts || [], function(index, cart){
        //fix doubles to two decimal digits:
        let distance = Number.parseFloat(cart.distanceFromStoreToCustomer).toFixed(2);
        let deliveryPrice = Number.parseFloat(cart.deliveryPrice).toFixed(2);
        let itemsPrice = Number.parseFloat(cart.totalItemsPrice).toFixed(2);

        $('<tr>' +
            '<td>' + cart.storeId + '</td>' +
            '<td>' + cart.storeName + '</td>' +
            '<td>' + '[' + cart.storeXLocation + ',' + cart.storeYLocation + ']' + '</td>' +
            '<td>' + distance + '&#13214' + '</td>' +
            '<td>' + '<span>&#8362</span>' + cart.PPK + '</td>' +
            '<td>' + '<span>&#8362</span>' + deliveryPrice + '</td>' +
            '<td>' + cart.itemsNumber + '</td>' +
            '<td>' + '<span>&#8362</span>' + itemsPrice + '</td>' +
            '</tr>').appendTo(orderTable)
    });

    //build order summary button:
    let button = '<button>Checkout</button>';

    innerDiv.append(orderTable);
    innerDiv.append(button);

    innerDiv.find("button")
        .addClass("w3-button w3-block w3-green w3-section w3-padding")
        .click(function(){
            window.location.assign(CHOOSE_DISCOUNTS_URL);
        });
}

$(function chooseItemsFrom(){
    $("#chooseItemForm").submit(function (){
        let itemWasChosen = false;  //let assume user didn't choose items
        let tableRows = $("tr");    //get all table rows

        //check if user chose item:
        tableRows.each(function (){
            let tableRow = $(this);
            if(tableRow.hasClass("inCart")){
                itemWasChosen = true;
            }
        })
        if(itemWasChosen){
            let parameters = $(this).serialize();

            $.ajax({
                method: 'POST',
                data: parameters,
                url: CHOOSE_ITEMS_URL,
                timeout: 2000,
                error: function (errorObject) {
                    console.log(errorObject.responseText);
                },
                success: function (carts) {
                    if(orderType=="dynamic"){
                        showOrderSummary(carts);
                    }
                    else{   //"orderType" =="static"
                        window.location.assign(CHOOSE_DISCOUNTS_URL);
                    }
                }
            });

        }
        else{   //item wasn't chosen
            $("#error-placeholder").text("Please chose at least one item before Checkout");
        }

        return false;
    });
});


