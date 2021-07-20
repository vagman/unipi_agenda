$(document).ready(function() {
    $('#desktop_view').click(function(){
        $.ajax({
            url: "/change-view",
            method: "POST",
            data: {
                view: "desktop",
            }
        }).done(function () {
            location.reload();
        });
    });
    $('#mobile_view').click(function(){
        $.ajax({
            url: "/change-view",
            method: "POST",
            data: {
                view: "mobile",
            }
        }).done(function () {
            location.reload();
        });
    });
});