const refreshRate = 2000; //milli seconds
const GET_REGION_NAME = buildUrlWithContextPath("getRegion");

//Global Variables:
var regionName;

$(function getRegionNameAndAdjustPage(){
    $.ajax({
        url: GET_REGION_NAME,
        success: function(response){
            regionName = response;
            $("#logo").text(regionName);
        },
        error: function (error){
            console.log(error);
        }
    })
})