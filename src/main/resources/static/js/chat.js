$(document).ready(function(){
    $('#message_send').click(function(){
        let messageInput = $('#message_input');
        let message_text = messageInput.val();
        let id_meeting = messageInput.attr('data-id-meeting');
        let username = messageInput.attr('data-username');
        $.ajax({
            url: "/send-meeting-message",
            method: "POST",
            data: {
                message_text: message_text,
                id_meeting: id_meeting,
                username: username
            }
        }).done(function (usersList) {
            location.reload();
        });
    });
    $('#floating_window_toggler').click(function(){
        $('.floating-menu, .floating-menu-filter').addClass('active');
    });
    $('.floating-menu-filter, #close_floating_menu').click(function(){
        $('.floating-menu, .floating-menu-filter').removeClass('active');
    });
    $('button#edit_description').click(function(){
        $('textarea.meeting-decription').prop("disabled",false).removeClass('disabled');
        $('button#edit_description').hide();
        $('button#save_description').show();
    });
    $('button#save_description').click(function(){
        let meeting_description = $('.meeting-decription').val();
        let id_meeting = $(this).attr('data-id-meeting');
        $.ajax({
            url: "/update-meeting-description",
            method: "POST",
            data: {
                id_meeting: id_meeting,
                meeting_description: meeting_description,
            }
        }).done(function (usersList) {
            $('button#save_description').hide();
            $('button#edit_description').show();
            $('textarea.meeting-decription').prop("disabled",true).addClass('disabled');
        });
    });
    $('button#edit_participants').click(function(){
        $('.meeting-participants').addClass('hidden');
        $('.meeting-participants-edit').addClass('visible');
    });
});