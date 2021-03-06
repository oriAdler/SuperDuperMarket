const refreshRate = 2000; //milli seconds
const USER_LIST_URL = buildUrlWithContextPath("usersList");
const REGION_TABLE_URL = buildUrlWithContextPath("regionList");
const UPLOAD_FILE_URL = buildUrlWithContextPath("upload");
const USER_INFO_URL = buildUrlWithContextPath("userInfo");
const USER_TRANSACTIONS_URL = buildUrlWithContextPath("userTransactions");
const ADD_MONEY_URL = buildUrlWithContextPath("addMoney");
const SET_REGION_NAME_URL = buildUrlWithContextPath("setRegion");
const NOTIFICATION_VERSION_URL = buildUrlWithContextPath("notification");
const STORE_NOTIFICATION_URL = buildUrlWithContextPath("storeNotification");
const GET_OLD_NOTIFICATIONS_URL = buildUrlWithContextPath("getOldNotifications");

// Global Variables:
var userType;
var notificationVersionUser = 0;
var firstNotificationUpdate = true;

$(function getUserTypeAndAdjustPage(){
    $.ajax({
        url: USER_INFO_URL,
        success: function(user){
            console.log("user[name: " + user.name + ", type: " + user.type+"]");
            userType = user.type;

            // Adjust page according to user type:
            if(userType=="Customer"){
                $("#uploadFileButton").hide();
                $("#notificationsOuterDiv").hide();
            }
            else{
                $("#transferMoneyButton").hide();
                triggerAjaxNotificationContent();
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

    if(transactions.length === 0){  //table is empty
        $('<tr>' +
            '<td>' + 'No content in table' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '</tr>').appendTo(accountTable);
    }

    $.each(transactions || [], function (index, transaction){
        let amount = Number.parseFloat(transaction.amount).toFixed(2);
        let balanceBefore = Number.parseFloat(transaction.balanceBefore).toFixed(2);
        let balanceAfter = Number.parseFloat(transaction.balanceAfter).toFixed(2);
        let date = transaction.date;

        let row = $('<tr>' +
                '<td>' + transaction.type + '</td>' +
                '<td>' + date.day + '-' + date.month + '-' + date.year + '</td>' +
                '<td>' + '<span>&#8362</span>' + amount + '</td>' +
                '<td>' + '<span>&#8362</span>' + balanceBefore + '</td>' +
                '<td>' + '<span>&#8362</span>' + balanceAfter + '</td>' +
                '</tr>').appendTo(accountTable);
        if(transaction.type==="Charge"){
            row.addClass("w3-text-red");
        }
        else{
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
    let userList = $("#usersList");

    //clear all current users
    userList.empty();

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
                '</li>').appendTo(userList);
        }
        else if(index % 3 === 1){
            $('<li class="w3-bar">' +
                '<img src=../../common/images/avatar2.png alt="" class="w3-bar-item w3-circle" style="width:85px">' +
                '<div class="w3-bar-item">' +
                user.name + '<br>' +
                user.type +
                '</div>' +
                '</li>').appendTo(userList);
        }
        else{
            $('<li class="w3-bar">' +
                '<img src=../../common/images/avatar3.png alt="" class="w3-bar-item w3-circle" style="width:85px">' +
                '<div class="w3-bar-item">' +
                user.name + '<br>' +
                user.type +
                '</div>' +
                '</li>').appendTo(userList);
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
        '<th>Orders average price</th>' +
        '<th>More Info</th>' +
    '</tr>').appendTo(regionTable);

    if(regions.length === 0){  //table is empty
        $('<tr>' +
            '<td>' + 'No content in table' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '</tr>').appendTo(regionTable);
    }

    //rebuild the region's table:
    $.each(regions || [], function (index, region){
        console.log("Adding region #" + index + ": " + region);
        let orderAveragePrice = Number.parseFloat(region.ordersAveragePrice).toFixed(2);
        $('<tr>' +
            '<td>' + region.ownerName + '</td>' +
            '<td>' + region.regionName + '</td>' +
            '<td>' + region.numOfItemsType + '</td>' +
            '<td>' + region.numOfStores + '</td>' +
            '<td>' + region.numOfOrders + '</td>' +
            '<td>' + '<span>&#8362</span>' + orderAveragePrice + '</td>' +
            '<td>' + '<button>Go to region >></button>' + '</td>' +
            '</tr>').appendTo(regionTable)
            .find("button")
            .addClass("w3-button w3-block w3-green")
            .click(function (){
                setRegionNameOnSession(region.regionName);
                if(userType=="Customer"){
                    window.location.assign("../stores/stores_customer.html");
                }
                else{   //userType=="Vendor"
                    //saveUserNotificationVersionOnServer();
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
    ajaxUsersList();
    setInterval(ajaxUsersList, refreshRate);

    //The region table is refreshed automatically
    setInterval(ajaxRegionTable, refreshRate);

    ajaxTransactionsTable();
    setInterval(ajaxTransactionsTable, refreshRate);
});

function appendToNotificationList(notifications){
    let notificationList = $("#notificationList");

    $.each(notifications || [] ,function (index, notification){
        $('<li class="w3-bar">' +
            '<div class="w3-bar-item">' +
            notification +
            '</div>' +
            '</li>').prependTo(notificationList);
    })
}

function triggerAjaxNotificationContent() {
    setTimeout(ajaxNotificationContent, refreshRate);
}

//call the server and get the notification version
//we also send it the current chat version so in case there was a change
//in the notification content, we will get the new string as well
function ajaxNotificationContent() {
    $.ajax({
        url: NOTIFICATION_VERSION_URL,
        data: "notificationVersion=" + notificationVersionUser,
        dataType: 'json',
        success: function(data) {
             //{"notifications":
             //["ori has made an order in your store \"super baba\".\nThe order includes 2 items with total price of 20.00 and delivery price of 108.17",...]
             //,"version":4}
            console.log("Server notification version: " + data.version + ", Current notification version: " + notificationVersionUser);
            if (data.version !== notificationVersionUser) {
                let dropDownButton = $("#dropDownButton");

               if(firstNotificationUpdate===true){  //when routing pages don't show alert bell
                   firstNotificationUpdate = false;
               }
               else if(!dropDownButton.find("span").length){
                    let badge = $('<span>\t&#128276</span>').addClass("w3-badge w3-circle w3-red");
                    dropDownButton.append(badge);
               }

               notificationVersionUser = data.version;
               appendToNotificationList(data.notifications);
            }

            triggerAjaxNotificationContent();
        },
        error: function(error) {
            triggerAjaxNotificationContent();
        }
    });
}

$(function (){
    $("#dropDownButton").click(function(){
        $(this).find("span").remove();
        let dropDown = document.getElementById("notificationDiv");
        if (dropDown.className.indexOf("w3-show") == -1) {
            dropDown.className += " w3-show";
        } else {
            dropDown.className = dropDown.className.replace("w3-show", "");
        }
    })
})


