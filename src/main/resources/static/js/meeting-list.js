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
        $('#notifications_button').toggleClass('open');
    });

    $('.desktop .meeting-container').click(function (){
        var id_meeting = $(this).attr('data-meeting-id');
        $('.meeting-chat-container,  .meeting-container').removeClass("open");
        $(this).addClass("open");
        $('.meeting-chat-container[data-meeting-id="'+id_meeting+'"]').addClass("open");
    });
})

