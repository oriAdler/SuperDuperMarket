const refreshRate = 2000; //milli seconds
const USER_LIST_URL = buildUrlWithContextPath("usersList");
const REGION_TABLE_URL = buildUrlWithContextPath("regionList");
const UPLOAD_FILE_URL = buildUrlWithContextPath("upload");
const USER_INFO_URL = buildUrlWithContextPath("userInfo");
const USER_TRANSACTIONS_URL = buildUrlWithContextPath("userTransactions");
const ADD_MONEY_URL = buildUrlWithContextPath("addMoney");
const SET_REGION_NAME_URL = buildUrlWithContextPath("setRegion");

// Global Variables:
var userType;

$(function getUserTypeAndAdjustPage(){
    $.ajax({
        url: USER_INFO_URL,
        success: function(user){
            console.log("user[name: " + user.name + ", type: " + user.type+"]");
            userType = user.type;

            // Adjust page according to user type:
            if(userType=="Customer"){
                $("#uploadFileButton").hide();
            }
            else{
                $("#transferMoneyButton").hide();
            }
        }
    })
});

function setRegionNameOnSession(name){
    $.ajax({
        url:SET_REGION_NAME_URL,
        data: "regionName=" + name,
        dataType: 'json',
        error: function (){
            console.log("SET_REGION_NAME returned error")
        },
        success: function (){
            console.log("SET_REGION_NAME returned success")
        }
    })
}

function refreshTransactionTable(transactions){
    let accountTable = $("#accountTable");

    accountTable.empty();

    $('<tr>' +
        '<th>Type</th>' +
        '<th>Date</th>' +
        '<th>Amount</th>' +
        '<th>Balance before</th>' +
        '<th>Balance after</th>' +
        '</tr>').appendTo(accountTable);

    $.each(transactions || [], function (index, transaction){
        let row = $('<tr>' +
                '<td>' + transaction.type + '</td>' +
                '<td>' + transaction.date + '</td>' +
                '<td>' + transaction.amount + '&#8362' + '</td>' +
                '<td>' + transaction.balanceBefore + '&#8362' + '</td>' +
                '<td>' + transaction.balanceAfter + '&#8362' + '</td>' +
                '</tr>').appendTo(accountTable);
        if(transaction.type==="Add"){
            row.addClass("w3-text-green");
        }
    });
}

function ajaxTransactionsTable(){
    $.ajax({
        url: USER_TRANSACTIONS_URL,
        success: function (transactions){
            refreshTransactionTable(transactions);
        }
    })
}

//users = a list of UserDTO objects:
//{id: 1,
// name: "ori",
// type: "Vendor"}
function refreshUsersList(users) {
    //clear all current users
    $("#usersList").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, user) {
        console.log("Adding user #" + index + ": " + user);
        //append it to the #userslist (div with id=usersList) element
        //if-else terms differentiate user's image
        if(index % 3 === 0){
            $('<li class="w3-bar">' +
                '<img src=../../common/images/avatar1.png alt="" class="w3-bar-item w3-circle" style="width:85px">' +
                '<div class="w3-bar-item">' +
                user.name + '<br>' +
                user.type +
                '</div>' +
                '</li>').appendTo($("#usersList"));
        }
        else if(index % 3 === 1){
            $('<li class="w3-bar">' +
                '<img src=../../common/images/avatar2.png alt="" class="w3-bar-item w3-circle" style="width:85px">' +
                '<div class="w3-bar-item">' +
                user.name + '<br>' +
                user.type +
                '</div>' +
                '</li>').appendTo($("#usersList"));
        }
        else{
            $('<li class="w3-bar">' +
                '<img src=../../common/images/avatar3.png alt="" class="w3-bar-item w3-circle" style="width:85px">' +
                '<div class="w3-bar-item">' +
                user.name + '<br>' +
                user.type +
                '</div>' +
                '</li>').appendTo($("#usersList"));
        }
    });
}

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

//regions = a list of regionDTO objects:
// [{"ownerName":"ori",
// "regionName":"Hasharon",
// "numOfItemsType":10,
// "numOfStores":4,
// "numOfOrders":0,
// "ordersAveragePrice":-1.0}]
function refreshRegionTable(regions){
    let regionTable = $("#regionTable");

    //clear all current regions
    regionTable.empty();
    $('<tr>' +
        '<th>Owner name</th>' +
        '<th>Region name</th>' +
        '<th>Items No.</th>' +
        '<th>Stores No.</th>' +
        '<th>Orders No.</th>' +
        '<th>Orders AVG price</th>' +
        '<th>More Info</th>' +
    '</tr>').appendTo(regionTable);

    //rebuild the region's table:
    $.each(regions || [], function (index, region){
        console.log("Adding region #" + index + ": " + region);
        $('<tr>' +
            '<td>' + region.ownerName + '</td>' +
            '<td>' + region.regionName + '</td>' +
            '<td>' + region.numOfItemsType + '</td>' +
            '<td>' + region.numOfStores + '</td>' +
            '<td>' + region.numOfOrders + '</td>' +
            '<td>' + region.ordersAveragePrice + '&#8362' + '</td>' +
            '<td>' + '<button>Go to region >></button>' + '</td>' +
            '</tr>').appendTo(regionTable)
            .find("button")
            .addClass("w3-button w3-block w3-green")
            .click(function (){
                setRegionNameOnSession(region.regionName);
                if(userType=="Customer"){
                    window.location.assign("../stores/stores_customer.html");
                }
                else{
                    window.location.assign("../stores/stores_vendor.html");
                }
            });
    });
}

function ajaxRegionTable(){
    $.ajax({
        url: REGION_TABLE_URL,
        success: function(regions){
            refreshRegionTable(regions);
        }
    })
}

// step 1: onload - capture the submit event on the form.
$(function AddMoney() {
    $("#transferMoney").submit(function() {

        let parameters = $(this).serialize();

        $.ajax({
            method:'POST',
            data: parameters,
            url: ADD_MONEY_URL,
            timeout: 4000,
            error: function(error) {
                console.error("Failed to submit");
                $("#resultTransferMoney").text("Failed to get result from server " + error)
                    .addClass("w3-text-red")
                    .removeClass("w3-text-green");
            },
            success: function(response) {
                $("#resultTransferMoney").text(response)
                    .addClass("w3-text-green")
                    .removeClass("w3-text-red");
            }
        });

        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })
})

// onload - capture the submit event on the form.
$(function uploadFile() { // onload...do
    $("#uploadForm").submit(function() {

        let file = this[0].files[0];

        let formData = new FormData();
        formData.append("file-key", file);

        $.ajax({
            method:'POST',
            data: formData,
            url: UPLOAD_FILE_URL,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            error: function(errorObject) {
                console.error("Failed to submit");
                $("#resultUploadFile").text(errorObject.responseText)
                    .addClass("w3-text-red")
                    .removeClass("w3-text-green");
            },
            success: function(response) {
                $("#resultUploadFile").text(response)
                    .addClass("w3-text-green")
                    .removeClass("w3-text-red");
            }
        });

        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })
})

//close the upload file modal and clean message
$(function closeModalFile(){
    $("#closeModalFile").click(function(){
        $("#resultUploadFile").text("");
        $("#uploadFileModal").fadeOut(500);;
    });
});

//close the transfer money modal and clean message
$(function closeModalTransfer(){
    $("#closeModalTransfer").click(function(){
        $("#resultTransferMoney").text("");
        $("#transferMoneyModal").fadeOut(500);
    });
});

//activate the timer calls after the page is loaded
$(function() {
    //The users list is refreshed automatically
    setInterval(ajaxUsersList, refreshRate);

    //The region table is refreshed automatically
    setInterval(ajaxRegionTable, refreshRate);

    setInterval(ajaxTransactionsTable, refreshRate);
});