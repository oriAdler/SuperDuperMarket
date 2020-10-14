
const FIRST_DISCOUNT_LIST_URL = buildUrlWithContextPath("discountsList");
const REFRESH_DISCOUNTS_LIST_URL = buildUrlWithContextPath("refreshDiscountList");

// offer constructor
function Offer(id, name, quantity, offerPrice, idStore){
    this.id = id;
    this.name = name;
    this.quantity = quantity;
    this.offerPrice = offerPrice;
    this.idStore = idStore;
}

//[{"name":"YallA BaLaGaN","itemId":1,"itemName":"Toilet Paper","amount":1.0,
// "operator":"ONE-OF",
// "offerList":
// [{"itemId":1,"itemName":"Toilet Paper","amount":1.0,"price":0.0,"storeId":1},
// {"itemId":7,"itemName":"Eggs","amount":2.0,"price":20.0,"storeId":1}]}]
function refreshDiscountsList(discounts){
    let discountsList = $("#discountsList");
    discountsList.empty();

    let offersArray = [];
    let arrayIndex = 0;

    $.each(discounts || [], function (index, discount){
        let listItem = $('<li class="w3-bar-item w3-left-align"></li>')
            .appendTo(discountsList);

        let form = $('<form class="w3-container" method="GET">')
            .appendTo(listItem);
        form.attr('action', 'refreshDiscountList');

        let div = $('<div class="w3-section">')
            .appendTo(form);

        $('<h2>' + discount.name + '</h2>')
            .appendTo(div)
            .addClass("discountName");

        let discountType;
        if(discount.operator==="ONE-OF"){
            discountType = $('<h4>' + 'CHOOSE ONE-OF' + '</h4>');
        }
        else{
            discountType = $('<h4>' + 'ALL-OR-NOTHING' + '</h4>');
        }
        div.append(discountType);

        $.each(discount.offerList || [], function(index, offer){
            offersArray.push(new Offer(offer.itemId, offer.itemName, offer.amount, offer.price, offer.storeId));
            let offerString = " " + offer.amount + " " + offer.itemName + " for " + offer.price + '&#8362';

            let offerParagraph = $('<p></p>')
                .append('<input class="w3-radio" type="radio" name="offer">')
                .append('<label>' + offerString + '</label>');

            offerParagraph.find("input").attr('value', arrayIndex);
            arrayIndex++;

            div.append(offerParagraph);
        })

        if(discount.operator==="ONE-OF"){
            div.find("input").first().attr('checked', true);
        }
        else{
            div.find("input").attr('disabled', true);
        }

        let button = $('<button style="width:250px" type="submit">' + 'AddDiscount' + '</button>')
            .addClass("w3-button w3-block w3-green w3-section w3-padding");
        div.append(button);
    });

    $("form").submit(function(){
        //update my discounts:
        let nameOfDiscount = $(this).find("discountName").text();
        $("#myDiscountsInnerDiv").append('<p>' + nameOfDiscount + '</p>');

        //find chosen offers:
        let chosenOffers = [];

        //(int itemId, double amount, double price, int storeId)
        $(this).find("input").each(function(index){
            let offer = $(this);
            if(offer.hasAttribute('disabled')){
                chosenOffers.push(offersArray[offer.getAttribute('value')]);
            }
            else if(offer.getAttribute('checked')===true){
                chosenOffers.push(offersArray[offer.getAttribute('value')]);
            }
        });

        console.log(chosenOffers);

        //refresh discounts list:
        $.ajax({
            method: 'POST',
            data: $(this).serialize(),
            url: REFRESH_DISCOUNTS_LIST_URL,
            timeout: 2000,
            error: function (errorObject) {
                console.log(errorObject.responseText);
            },
            success: function (discounts){
                refreshDiscountsList(discounts);
            }
        });

        return false;
    });
}

$(function ajaxDiscountList(){
    $.ajax({
        url: FIRST_DISCOUNT_LIST_URL,
        error(errorObject){
            console.log(errorObject.responseText);
        },
        success: function (discounts){
            refreshDiscountsList(discounts);
            console.log(discounts);
        }
    })
})