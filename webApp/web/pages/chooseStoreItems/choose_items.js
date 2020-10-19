
const ORDER_ITEMS_TABLE = buildUrlWithContextPath("orderItemsList");
const APPROVE_ADD_STORE_URL = buildUrlWithContextPath("approveAddStore");

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
        '<th>Category</th>' +
        '<th>New Price</th>' +
        '</tr>');
    tableHeader.appendTo(itemTable);

    //rebuild the items table:
    $.each(items || [], function(index,item){
        let itemPrice = item.isSoldByStore ? item.price + "&#8362" : "N/A";
        let tableRow = $('<tr>' +
            '<td class="id">' + item.id + '</td>' +
            '<td>' + item.name + '</td>' +
            '<td>' + item.category + '</td>' +
        '</tr>').appendTo(itemTable);;

        tableRow.append('<td>' + '<button>Add to Store</button>' + '</td>')
            .find("button")
            .addClass("w3-button w3-block w3-green")
            .click(function (){
                console.log()
                let buttonCell = tableRow.find("button").parent();
                buttonCell.empty();
                buttonCell.append('<input type="number" id="amount" name="amount" min="1" step="1" value="1">');
                tableRow.find("input").attr('name', item.id);

                tableRow.addClass("inCart");    //in order to extract from table and send to server.
            });
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
                url: APPROVE_ADD_STORE_URL,
                timeout: 2000,
                error: function (errorObject) {
                    console.log(errorObject.responseText);
                },
                success: function () {
                    window.location.assign("../stores/stores_vendor.html");
                }
            });

        }
        else{   //item wasn't chosen
            $("#error-placeholder").text("Store must sell at least one item, please chose an item to add to store");
        }

        return false;
    });
});


