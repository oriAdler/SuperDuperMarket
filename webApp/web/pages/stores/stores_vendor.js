const refreshRate = 2000; //milli seconds
const GET_REGION_NAME = buildUrlWithContextPath("getRegion");
const ITEMS_TABLE = buildUrlWithContextPath("itemsTable");
const STORES_LIST = buildUrlWithContextPath("storesList")
//Global Variables:
var regionName;

$(function getRegionNameAndAdjustPage(){
    $.ajax({
        url: GET_REGION_NAME,
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
        let container = $('<div></div>').addClass("w3-row w3-white w3-margin-bottom").appendTo(storesList);

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

        let twoThird = $('<div></div>').addClass("w3-twothird w3-container").appendTo(container);
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
        url: ITEMS_TABLE,
        success: function (items){
            refreshItemsList(items);
        }
    })
}

function ajaxStoresList(){
    $.ajax({
        url: STORES_LIST,
        success: function (stores){
            refreshStoresList(stores);
        }
    })
}

$(function(){
    setInterval(ajaxItemsTable, refreshRate);

    setInterval(ajaxStoresList, refreshRate);
})