
const ORDER_SUMMARY_URL = buildUrlWithContextPath("getOrderSummary");

// List<CartDTO>:
// PPK: 10
// deliveryPrice: 36.05551275463989
// distanceFromStoreToCustomer: 3.605551275463989
// items: Array(2)
// 0: {priceSum: 10, storeName: "Rami", storeId: 1, onDiscount: false, id: 1, …}
// 1: {priceSum: 1.5, storeName: "Rami", storeId: 1, onDiscount: false, id: 5, …}
// length: 4
// __proto__: Array(0)
// itemsNumber: 4
// storeId: 1
// storeName: "Rami"
// storeXLocation: 3
// storeYLocation: 4
// totalItemsPrice: 87.5
// totalOrderPrice: 123.55551275463989
// __proto__: Object
function refreshOrdersList(carts){
    let cartsList = $("#cartsList");

    $.each(carts || [], function (index, cart){
        let container = $('<div></div>').addClass("w3-row w3-white w3-margin-bottom").appendTo(cartsList);

        let third = $('<div></div>').addClass("w3-third w3-container").appendTo(container);
        let cartData = $('<div>' +
            '<h2>' + cart.storeName + '</h2>' +
            '<h4>' + 'Serial No. - ' + cart.storeId + '</h4>' +
            '<h4>' + 'PPK - ' + cart.PPK + '&#8362' + '</h4>' +
            '<h4>' + 'Distance to customer - ' + cart.distanceFromStoreToCustomer + '&#13214' + '</h4>' +
            '<h4>' + 'Delivery Price - ' + cart.deliveryPrice + '&#8362' + '</h4>' +
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
            '<th>Amount</th>' +
            '<th>Price</th>' +
            '<th>Total</th>' +
            '<th>On Discount</th>' +
            '</tr>').appendTo(itemsTable);
        // category: "Quantity"
        // id: 1
        // isSoldByStore: false
        // name: "Toilet Paper"
        // numOfSales: 1
        // numOfSellers: -1
        // onDiscount: false
        // price: "10.00"
        // priceSum: 10
        // storeId: 1
        // storeName: "Rami"
        $.each(cart.items || [], function (index, item){
            $('<tr>' +
                '<td>' + item.id + '</td>' +
                '<td>' + item.name + '</td>' +
                '<td>' + item.category + '</td>' +
                '<td>' + item.numOfSales + '</td>' +
                '<td>' + item.price + '&#8362' + '</td>' +
                '<td>' + item.priceSum + '</td>' +
                '<td>' + item.onDiscount + '</td>' +
                '</tr>').appendTo(itemsTable);
        });
    });

    //build approve button:
    let buttonApprove = '<button id="ApproveButton">Approve</button>';
    cartsList.append(buttonApprove);
    cartsList.find("#ApproveButton")
        .addClass("w3-button w3-block w3-green w3-section w3-padding")
        .click(function (){
            alert("approve button works");
    });

    let buttonCancel = '<button id="CancelButton">Cancel</button>';
    cartsList.append(buttonCancel);
    cartsList.find("#CancelButton")
        .addClass("w3-button w3-block w3-gray w3-section w3-padding")
        .click(function (){
            alert("cancel button works");
        })
}

$(function ajaxOrderSummary(){
    $.ajax({
        url: ORDER_SUMMARY_URL,
        success: function(carts){
            refreshOrdersList(carts);
        }
    })
})