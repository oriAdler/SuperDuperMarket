
const ORDER_ITEMS_TABLE = buildUrlWithContextPath("orderItemsList");
const GET_REGION_NAME_URL = buildUrlWithContextPath("getRegion");
const GET_DYNAMIC_ORDER_SUMMARY = buildUrlWithContextPath("getDynamicOrderSummary");

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
    if(localStorage.getItem("orderType")==="static"){
        tableHeader.append('<th>Price</th>');
    }
    tableHeader.append(
        '<th>Cart</th>' +
        '</tr>');
    tableHeader.appendTo(itemTable);

    //rebuild the items table:
    $.each(items || [], function(index,item){
        let itemPrice = item.isSoldByStore ? item.price + "&#8362" : "N/A";
        let tableRow = $('<tr>' +
            '<td class="id">' + item.id + '</td>' +
            '<td>' + item.name + '</td>' +
            '<td>' + item.category + '</td>');
        if(localStorage.getItem("orderType")==="static"){
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
                        }
                        else{   //item.category==="Quantity"
                            buttonCell.append('<input type="number" id="amount" name="amount" min="0.5" step="0.1" value="0.5">');
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

$(function getRegionNameAndAdjustPage(){
    // Check browser support for web storage API
    if (typeof(Storage) !== "undefined") {
        // retrieve orderType & store name:
        let orderType = localStorage.getItem("orderType");
        if(orderType==="static"){
            let storeName = localStorage.getItem("store");
            $("#logo").text(storeName);
        }
        else{   //orderType==="dynamic"
            // get region name via ajax request
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
        }
    } else {
        alert("Sorry, your browser does not support Web Storage...");
    }
});

// item constructor
function Item(id, amount){
    this.id = id;
    this.amount = amount;
}

function ajaxDynamicOrderSummary(itemsArray){
    $.ajax({
        url: GET_DYNAMIC_ORDER_SUMMARY,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(itemsArray),
        error: function (error){

        },
        success: function (result){

        }
    })
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
            let itemsArray = [];

            // fill items array:
            tableRows.each(function(){
                let tableRow = $(this);
                if(tableRow.hasClass("inCart")){
                    itemsArray.push(new Item(tableRow.children(".id").html(), tableRow.find("input").val()));
                }
            })

            console.log(itemsArray);
            //TODO: change local storage to servlet who tells what is the order type.
            if(localStorage.getItem("orderType")==="dynamic"){
                ajaxDynamicOrderSummary(itemsArray);
            }
            else{

            }
        }
        else{   //item wasn't chosen
            $("#error-placeholder").text("Please chose at least one item before Checkout");
        }

        return false;
    });
});


