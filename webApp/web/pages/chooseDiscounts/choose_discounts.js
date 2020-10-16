
const FIRST_DISCOUNT_LIST_URL = buildUrlWithContextPath("discountsList");
const REFRESH_DISCOUNTS_URL = buildUrlWithContextPath("refreshDiscount");



function ajaxDiscountList(){
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
}

// offer constructor
function Offer(itemId, itemName, amount, price, storeId){
    this.itemId = itemId;
    this.itemName = itemName;
    this.amount = amount;
    this.price = price;
    this.storeId = storeId;
}

function ifYouBuy(itemId, amount){
    this.itemId = itemId;
    this.amount = amount;
}

//[{"name":"YallA BaLaGaN","itemId":1,"itemName":"Toilet Paper","amount":1.0,
// "operator":"ONE-OF",
// "offerList":
// [{"itemId":1,"itemName":"Toilet Paper","amount":1.0,"price":0.0,"storeId":1},
// {"itemId":7,"itemName":"Eggs","amount":2.0,"price":20.0,"storeId":1}]}]
function refreshDiscountsList(discounts){
    let discountsList = $("#discountsList");
    discountsList.empty();

    //offerArray are user's choices to be sent to the server
    let offersArray = [];
    let offersIndex = 0;
    let ifYouBuyArray = [];
    let ifYouBuyIndex = 0;

    $.each(discounts || [], function (index, discount){
        //create discount listItem
        let listItem = $('<li class="w3-bar-item w3-left-align"></li>').appendTo(discountsList);
        let form = $('<form class="w3-container" method="GET">').appendTo(listItem);
        let div = $('<div class="w3-section">').appendTo(form);
        $('<h2>' + discount.name + '</h2>').appendTo(div).addClass("discountName");

        ifYouBuyArray.push(new ifYouBuy(discount.itemId, discount.amount));
        form.attr('ifYouBuy', ifYouBuyIndex);
        ifYouBuyIndex++;

        let discountType;
        if(discount.operator==="ONE-OF"){
            discountType = $('<h4>' + 'CHOOSE ONE-OF' + '</h4>');
        }
        else{
            discountType = $('<h4>' + 'ALL-OR-NOTHING' + '</h4>');
        }
        div.append(discountType);

        //create offerList:
        $.each(discount.offerList || [], function(index, offer){
            offersArray.push(new Offer(offer.itemId, offer.itemName, offer.amount, offer.price, offer.storeId));

            let offerString = " " + offer.amount + " " + offer.itemName + " for " + offer.price + '&#8362';
            let offerParagraph = $('<p></p>')
                .append('<input class="w3-radio" type="radio" name="offer">')
                .append('<label>' + offerString + '</label>');

            offerParagraph.find("input").attr('value', offersIndex);
            offersIndex++;

            div.append(offerParagraph);
        });

        //set radio buttons according to discount's type
        if(discount.operator==="ONE-OF"){
            div.find("input").first().attr('checked', true);
        }
        else{
            div.find("input").attr('disabled', true);
        }

        let button = $('<button style="width:250px" type="submit">' + 'AddDiscount' + '</button>')
            .addClass("w3-button w3-block w3-green w3-section w3-padding");
        div.append(button);

        //add a submit function to current form:
        form.submit(function chooseDiscount(){
            let itemToRemove = ifYouBuyArray[form.attr('ifYouBuy')];

            //update my discounts:
            let nameOfDiscount = form.find(".discountName").text();
            $("#myDiscountsInnerDiv").append('<p>' + nameOfDiscount  + '</p>');

            //find chosen offers:
            let chosenOffers = [];

            //(int itemId, double amount, double price, int storeId)
            form.find("input").each(function(index){
                let offer = $(this);
                if(offer.attr("disabled")==="disabled"){    //All-OR-NOTHING discount
                    chosenOffers.push(offersArray[offer.attr("value")]);
                }
                else if(offer.is(':checked')){  //ONE-OF discount
                    chosenOffers.push(offersArray[offer.attr("value")]);
                }
            });

            console.log(chosenOffers);

            //refresh discounts list:
            $.ajax({
                url: REFRESH_DISCOUNTS_URL,
                // type: 'POST',
                data: { offersArray : JSON.stringify(chosenOffers), itemToRemove : JSON.stringify(itemToRemove)},
                dataType: 'json',
                success: function (data){
                    console.log(data);
                    ajaxDiscountList();
                },
                error: function (errorObject) {
                    alert("error");
                    console.log(errorObject.responseText);
                }
            });

            return false;
        });
    });
}

$(ajaxDiscountList());