
const ORDER_ITEMS_TABLE = buildUrlWithContextPath("orderItemsList");

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
    $('<tr>' +
        '<th>Serial number</th>' +
        '<th>Name</th>' +
        '<th>Category</th>' +
        '<th>Price</th>' +
        '<th>Cart</th>' +
        '</tr>').appendTo(itemTable);

    //rebuild the items table:
    $.each(items || [], function(index,item){
        let itemPrice = item.isSoldByStore ? item.price + "&#8362" : "N/A";
        let tableRow = $('<tr>' +
            '<td>' + item.id + '</td>' +
            '<td>' + item.name + '</td>' +
            '<td>' + item.category + '</td>' +
            '<td>' + itemPrice +'</td>' +
            // '<td>' + '<button>Add to Cart</button>' + '</td>' +
            '</tr>').appendTo(itemTable);

        if(item.isSoldByStore) {
            tableRow.append('<td>' + '<button>Add to Cart</button>' + '</td>')
                .find("button")
                .addClass("w3-button w3-block w3-green")
                .click(function (){
                    console.log()
                        let buttonCell = tableRow.find("button").parent();
                        buttonCell.empty();
                        buttonCell.append('<input type="number" id="quantity" name="quantity" min="1" step="1" value="1">');
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
})

