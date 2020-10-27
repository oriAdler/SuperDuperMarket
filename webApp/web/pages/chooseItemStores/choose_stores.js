
const OWNER_STORES_LIST = buildUrlWithContextPath("ownerStoresList");
const APPROVE_ADD_ITEM_URL = buildUrlWithContextPath("approveAddItem");

//[{"id":1,
// "name":"Rami",
//..
function refreshStoresTable(stores){
    let storeTable = $('#storesTable');

    //clear all current items
    storeTable.empty();

    //append headers
    let tableHeader = $('<tr>' +
        '<th>Store Id</th>' +
        '<th>Store Name</th>' +
        '<th>Item Price</th>' +
        '</tr>');
    tableHeader.appendTo(storeTable);

    if(stores.length === 0){  //table is empty
        $('<tr>' +
            '<td>' + 'No content in table' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '<td>' + ' ' + '</td>' +
            '</tr>').appendTo(storeTable);
    }

    //rebuild the stores table:
    $.each(stores || [], function(index,store){
        let tableRow = $('<tr>' +
            '<td class="id">' + store.id + '</td>' +
            '<td>' + store.name + '</td>' +
            '</tr>').appendTo(storeTable);;

        tableRow.append('<td>' + '<button>Add to Store</button>' + '</td>')
            .find("button")
            .addClass("w3-button w3-block w3-green")
            .click(function (){
                console.log()
                let buttonCell = tableRow.find("button").parent();
                buttonCell.empty();
                buttonCell.append('<input type="number" id="amount" name="amount" min="1" step="1" value="1">');
                tableRow.find("input").attr('name', store.id);

                tableRow.addClass("inCart");    //in order to extract from table and send to server.
            });
    });
}

$(function ajaxOrderStoresList(){
    $.ajax({
        url: OWNER_STORES_LIST,
        error(errorObject){
            console.log("error: " + errorObject.responseText);
            $("#check").text(errorObject.responseText);
        },
        success: function (stores){
            refreshStoresTable(stores);
        }
    })
});

$(function chooseStoresFrom(){
    $("#chooseStoresForm").submit(function (){
        let storeWasChosen = false;  //let assume user didn't choose stores
        let tableRows = $("tr");    //get all table rows

        //check if user chose item:
        tableRows.each(function (){
            let tableRow = $(this);
            if(tableRow.hasClass("inCart")){
                storeWasChosen = true;
            }
        })
        if(storeWasChosen){
            let parameters = $(this).serialize();

            $.ajax({
                method: 'POST',
                data: parameters,
                url: APPROVE_ADD_ITEM_URL,
                timeout: 2000,
                error: function (errorObject) {
                    console.log(errorObject.responseText);
                },
                success: function () {
                    document.getElementById('approvalModal').style.display='block';
                }
            });

        }
        else{   //store wasn't chosen
            $("#error-placeholder").text("Item must be sold at least by one store, please chose a store");
        }

        return false;
    });
});

//close the upload file modal and clean message
$(function closeApprovalModal(){
    $("#closeApprovalModal").click(function(){
        window.location.assign("../stores/stores_vendor.html");
    });
});

// onload - capture the submit event on the form.
$(function submitApprovalModal() { // onload...do
    $("#approvalModal").submit(function() {
        window.location.assign("../stores/stores_vendor.html");
        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })
})


