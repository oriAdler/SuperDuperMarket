
const ORDER_SUMMARY_URL = buildUrlWithContextPath("getOrderSummary");
const APPROVE_ORDER_URL = buildUrlWithContextPath("approveOrder");
const ADD_FEEDBACK_URL = buildUrlWithContextPath("addFeedback");

// function Feedback(id, rating, text){
//     this.id = id;
//     this.rating = rating;
//     this.text = text;
// }

function giveFeedbacks(){
    $("#ApproveButton").hide();
    $("#CancelButton").text("FINISH").removeClass("w3-gray").addClass("w3-green");

    $("#divHeader").text("Give Feedbacks");
    $(".storeDiv").each(function (){
        let feedbackDiv = $(this);
        let storeName = feedbackDiv.attr("storeName");
        let storeId = feedbackDiv.attr("storeId");

        feedbackDiv.empty();
        feedbackDiv.append('<h2>' + storeName + '<h2>');

        //build feedback form:
        let form = $('<form class="w3-container w3-left-align" method="GET">').appendTo(feedbackDiv);
        let innerDiv = $('<div class="w3-section">').appendTo(form);

        $('<input type="hidden" name="id">').attr("value", storeId).appendTo(innerDiv);

        $('<label>' + '<b>' + 'Rating' + '</b>' + '</label>').appendTo(innerDiv);
        let radioButtonTable = $('<table style="max-width:200px"></table>').addClass("w3-striped w3-border w3-table-all").appendTo(innerDiv);
        $('<tr>' +
            '<th>1</th>' +
            '<th>2</th>' +
            '<th>3</th>' +
            '<th>4</th>' +
            '<th>5</th>' +
            '</tr>').appendTo(radioButtonTable);
        $('<tr>' +
            '<td>' + '<input type="radio" name="rate" value="1" required>' + '</td>' +
            '<td>' + '<input type="radio" name="rate" value="2" required>' + '</td>' +
            '<td>' + '<input type="radio" name="rate" value="3" required>' + '</td>' +
            '<td>' + '<input type="radio" name="rate" value="4" required>' + '</td>' +
            '<td>' + '<input type="radio" name="rate" value="5" required>' + '</td>' +
            '</tr>').appendTo(radioButtonTable);

        $('<label>' + '<b>' + 'Feedback' + '</b>' + '</label>').appendTo(innerDiv);
        let textArea = $('<textarea id="feedback" class="w3-input w3-border w3-margin-bottom" name="feedback" placeholder="Write your feedback here" style="height:150px">')
            .appendTo(innerDiv);

        $('<button class="w3-button w3-block w3-green w3-section w3-padding" type="submit">POST</button>')
            .appendTo(innerDiv);
        form.submit(function (){
                feedbackDiv.empty();
                feedbackDiv.append('<h4>' + 'Thank you for your feedback!' + '<h4>');
            $.ajax({
                type: 'POST',
                data: $(this).serialize(),
                url: ADD_FEEDBACK_URL,
                error: function(errorObject) {
                    alert("Failed to submit feedback !");
                },
                success: function(nextPageUrl) {
                    //window.location.assign(nextPageUrl);
                }
            });

            return false;
        });
    })
}

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
        container.addClass("storeDiv");
        container.attr("storeName", cart.storeName);
        container.attr("storeId", cart.storeId);

        let third = $('<div></div>').addClass("w3-third w3-container").appendTo(container);

        //fix doubles to two decimal digits:
        let distance = Number.parseFloat(cart.distanceFromStoreToCustomer).toFixed(2);
        let deliveryPrice = Number.parseFloat(cart.deliveryPrice).toFixed(2);

        let cartData = $('<div>' +
            '<h2>' + cart.storeName + '</h2>' +
            '<h4>' + 'Serial No. - ' + cart.storeId + '</h4>' +
            '<h4>' + '<span>&#8362</span>' + 'PPK - ' + cart.PPK + '</h4>' +
            '<h4>' + 'Distance to customer - ' + distance + '&#13214' + '</h4>' +
            '<h4>' + 'Delivery Price - ' + '<span>&#8362</span>' + deliveryPrice + '</h4>' +
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
                '<td>' + '<span>&#8362</span>' + item.price + '</td>' +
                '<td>' + '<span>&#8362</span>' + item.priceSum + '</td>' +
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
            $.ajax({
                url: APPROVE_ORDER_URL,
                error: function (errorObject){
                    console.log(errorObject.responseText);
                    alert(errorObject.responseText);
                },
                success: function (nextUrlPage){
                    giveFeedbacks();
                    //console.log(nextUrlPage);
                    //window.location.assign(nextUrlPage);
                }
            })
    });

    let buttonCancel = '<button id="CancelButton">Cancel</button>';
    cartsList.append(buttonCancel);
    cartsList.find("#CancelButton")
        .addClass("w3-button w3-block w3-gray w3-section w3-padding")
        .click(function (){
            document.getElementById('approvalModal').style.display='block';
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

//close the upload file modal and clean message
$(function closeApprovalModal(){
    $("#closeApprovalModal").click(function(){
        window.location.href = "../stores/stores_customer.html";
    });
});

// onload - capture the submit event on the form.
$(function submitApprovalModal() { // onload...do
    $("#approvalModal").submit(function() {
        window.location.href = "../stores/stores_customer.html";
        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })
})