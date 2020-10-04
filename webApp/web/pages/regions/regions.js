var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("usersList");
var UPLOAD_FILE_URL = buildUrlWithContextPath("upload");

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
        $('<li>' + user.name + " - " + user.type + '</li>').appendTo($("#usersList"));
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

//activate the timer calls after the page is loaded
$(function() {

    //The users list is refreshed automatically
    setInterval(ajaxUsersList, refreshRate);
});

// step 1: onload - capture the submit event on the form.
$(function() { // onload...do
    $("#uploadForm").submit(function() {

        var file = this[0].files[0];

        var formData = new FormData();
        formData.append("file-key", file);
        formData.append("name-key", this[1].value);

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