const refreshRate = 2000; //milli seconds
const USER_LIST_URL = buildUrlWithContextPath("usersList");
const REGION_TABLE_URL = buildUrlWithContextPath("regionList");
const UPLOAD_FILE_URL = buildUrlWithContextPath("upload");

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
            '<img src="https://www.w3schools.com/w3css/img_avatar2.png" alt="" class="w3-bar-item w3-circle" style="width:85px">' +
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

    //TODO: after setting "clickableRow" where to place onClick?
    regionTable.empty();
    $('<tr>' +
        '<th>Owner name</th>' +
        '<th>Region name</th>' +
        '<th>Items No.</th>' +
        '<th>Stores No.</th>' +
        '<th>Orders No.</th>' +
        '<th>Orders average price</th>' +
    '</tr>').appendTo(regionTable).addClass("clickableRow");

    //rebuild the region's table:
    $.each(regions || [], function (index, region){
        console.log("Adding region #" + index + ": " + region);
        //TODO: Unresolved variable 'ownerName' ? in refresh user works just fine
        $('<tr>' +
            '<td>' + region.ownerName + '</td>' +
            '<td>' + region.regionName + '</td>' +
            '<td>' + region.numOfItemsType + '</td>' +
            '<td>' + region.numOfStores + '</td>' +
            '<td>' + region.numOfOrders + '</td>' +
            '<td>' + region.ordersAveragePrice + '</td>' +
            '<td>' + '<button>Go to region >></button>' + '</td>' +
            '</tr>').appendTo(regionTable).addClass("clickableRow");
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

//activate the timer calls after the page is loaded
$(function() {

    //The users list is refreshed automatically
    setInterval(ajaxUsersList, refreshRate);

    //The region table is refreshed automatically
    setInterval(ajaxRegionTable, refreshRate*3);
});

// step 1: onload - capture the submit event on the form.
$(function() { // onload...do
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
                $("#result").text("Failed to get result from server " + error);
            },
            success: function(response) {
                $("#result").text(response);
            }
        });

        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })
})

//close the upload file modal and clean message
$(function(){
    $("#closeModal").click(function(){
        $("#result").text("")
        $("#uploadFileModal").fadeOut(500);
    });
});

$(function(){
    $(".clickableRow").click(function() {
        $(this).hide();
    });
});