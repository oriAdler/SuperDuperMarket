const refreshRate = 2000; //milli seconds
const GET_REGION_NAME_URL = buildUrlWithContextPath("getRegion");
const ITEMS_TABLE_URL = buildUrlWithContextPath("itemsTable");
const STORES_LIST_URL = buildUrlWithContextPath("storesList");
const MAKE_ORDER_URL = buildUrlWithContextPath("makeOrder");
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
        '<th>Sold No.</th>' +
        '</tr>').appendTo(itemTable);

    //rebuild the items table:
    $.each(items || [], function(index,item){
        $('<tr>' +
            '<td>' + item.id + '</td>' +
            '<td>' + item.name + '</td>' +
            '<td>' + item.category + '</td>' +
            '<td>' + item.numOfSellers + '</td>' +
            '<td>' + item.price + '&#8362' +'</td>' +
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
        let storeData = $('<div>' +
            '<h2>' + store.name + '</h2>' +
            '<h4>' + 'Serial No. - ' + store.id + '</h4>' +
            '<h4>' + 'Owner - ' + store.ownerName + '</h4>' +
            '<h4>' + 'Location - [' + store.xLocation + ',' + store.yLocation + ']' + '</h4>' +
            '<h4>' + 'Orders No. - ' + store.numOfOrders + '</h4>' +
            '<h4>' + 'Items income - ' + store.totalItemsIncome + '</h4>' +
            '<h4>' + 'PPK - ' +  store.PPK + '</h4>' +
            '<h4>' + 'Delivery Income - ' + store.totalDeliveryIncome + '</h4>' +
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
            '<th>Sells No.</th>' +
            '</tr>').appendTo(itemsTable);
        //ItemDTO
        // id: 1
        // name: "Toilet Paper"
        // category: "Quantity"
        // price: "10.00"
        // numOfSellers: 0
        $.each(store.items || [], function (index, item){
            $('<tr>' +
                '<td>' + item.id + '</td>' +
                '<td>' + item.name + '</td>' +
                '<td>' + item.category + '</td>' +
                '<td>' + item.price + '&#8362' + '</td>' +
                '<td>' + item.numOfSellers + '</td>' +
                '</tr>').appendTo(itemsTable);
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

$(function(){

    ajaxItemsTable();
    setInterval(ajaxItemsTable, refreshRate);

    ajaxStoresList();
    setInterval(ajaxStoresList, refreshRate);
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