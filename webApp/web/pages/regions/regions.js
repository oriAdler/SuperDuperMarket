const refreshRate = 2000; //milli seconds
const USER_LIST_URL = buildUrlWithContextPath("usersList");
const REGION_TABLE_URL = buildUrlWithContextPath("regionList");
const UPLOAD_FILE_URL = buildUrlWithContextPath("upload");
const USER_INFO_URL = buildUrlWithContextPath("userInfo");

//users = a map of usernames and user objects:
// {"ori":
    // {"ordersIdList":[],
    // "averageOrdersPrice":0.0,
    // "averageDeliveryPrice":0.0,
    // "averageItemsPrice":0.0,"id":1,
    // "name":"ori",
    // "type":"Customer"}
// }
function refreshUsersList(users) {
    //clear all current users
    $("#usersList").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, user) {
        console.log("Adding user #" + index + ": " + user);
        //create a new <option> tag with a value in it and
        //append it to the #userslist (div with id=userslist) element
        $('<li class="w3-bar">' +
            '<img src="../../common/images/img_avatar2.png" alt="" class="w3-bar-item w3-circle" style="width:85px">' +
            '<div class="w3-bar-item">' +
                user.name + '<br>' +
                user.type +
            '</div>' +
            '</li>').appendTo($("#usersList"));
        // $('<li>' + user.name + " - " + user.type + '</li>').appendTo($("#usersList"));
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
    //clear all current regions
    let regionTable = $("#regionTable");

    regionTable.empty();
    $('<tr>' +
        '<th>Owner name</th>' +
        '<th>Region name</th>' +
        '<th>Items No.</th>' +
        '<th>Stores No.</th>' +
        '<th>Orders No.</th>' +
        '<th>Orders AVG price</th>' +
        '<th>More Info</th>' +
    '</tr>').appendTo(regionTable).addClass("clickableRow");

    //rebuild the region's table:
    $.each(regions || [], function (index, region){
        console.log("Adding region #" + index + ": " + region);
        $('<tr>' +
            '<td>' + region.ownerName + '</td>' +
            '<td>' + region.regionName + '</td>' +
            '<td>' + region.numOfItemsType + '</td>' +
            '<td>' + region.numOfStores + '</td>' +
            '<td>' + region.numOfOrders + '</td>' +
            '<td>' + region.ordersAveragePrice + '</td>' +
            '<td>' + '<button>Go to region >></button>' + '</td>' +
            '</tr>').appendTo(regionTable)
            .find("button")
            .addClass("w3-button w3-block w3-green")
            .click(function (){
                console.log(region.regionName);
                window.location.replace("../stores/stores_customer.html");
            });
    })
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
            error: function(error) {
                console.error("Failed to submit");
                $("#resultUploadFile").text("Failed to get result from server " + error);
            },
            success: function(response) {
                $("#resultUploadFile").text(response);
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

// UserDTO:
// {"id":1,
// "name":"tomer",
// "numberOfOrders":0,
// "averageItemsPrice":0.0,
// "averageDeliveryPrice":0.0,
// "type":"Customer"}
$(function getUserType(){
    $.ajax({
        url: USER_INFO_URL,
        success: function(user){
            console.log("USER - name:" + user.name + " type: " + user.type);
            console.log(user.type);
            if(user.type=="Customer"){
                $("#uploadFileButton").hide();
            }
            else{
                $("#transferMoneyButton").hide();
            }
        }
    })
});

//activate the timer calls after the page is loaded
$(function() {

    //The users list is refreshed automatically
    setInterval(ajaxUsersList, refreshRate);

    //The region table is refreshed automatically
    setInterval(ajaxRegionTable, refreshRate);
});