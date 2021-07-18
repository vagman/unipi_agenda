$(document).ready(function (){
    $('li.task').click(function () {
        var toggle = $(this).attr('data-toggle');
        if (!$(this).hasClass('open')) {
            $('.task-details[data-toggle="' + toggle + '"]').slideDown();
            $(this).addClass('open');
        } else {
            $('.task-details[data-toggle="' + toggle + '"]').slideUp();
            $(this).removeClass('open');
        }
    });

    $('#notifications_button,.notification-popup-filter').click(function () {
        $('.notification-popup,.notification-popup-filter').toggleClass('open')
        $(this).toggleClass('open');
    });
})

