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
        $(this).parent().addClass("editing");
        $('.meeting-participants').addClass('hidden');
        $('.meeting-participants-edit').addClass('visible');
    });
    $('button#edit_title').click(function(){
        $('textarea.meeting-title').prop("disabled",false).removeClass('disabled');
        $('button#edit_title').hide();
        $('button#save_title').show();
    });
    $('button#save_date').click(function(){
        let meeting_date = $('#edit_meeting_date').val();
        let id_meeting = $(this).attr('data-id-meeting');
        $.ajax({
            url: "/update-meeting-date",
            method: "POST",
            data: {
                id_meeting: id_meeting,
                meeting_date: meeting_date,
            }
        }).done(function () {
            $('button#save_date').hide();
            $('button#edit_date').show();
            $('#edit_meeting_date').prop("disabled",true);
        });
    });
    $('button#edit_date').click(function(){
        $('#edit_meeting_date').prop("disabled",false);
        $('button#edit_date').hide();
        $('button#save_date').show();
    });
    $('button#save_title').click(function(){
        let meeting_title = $('.meeting-title').val();
        let id_meeting = $(this).attr('data-id-meeting');
        $.ajax({
            url: "/update-meeting-title",
            method: "POST",
            data: {
                id_meeting: id_meeting,
                meeting_title: meeting_title,
            }
        }).done(function (usersList) {
            $('button#save_title').hide();
            $('button#edit_title').show();
            $('textarea.meeting-title').prop("disabled",true).addClass('disabled');
        });
    });
    $("button.remove-participant").click(function () {
        let id_meeting = $(this).attr('data-id-meeting');
        let participant_username = $(this).attr('data-username');
        let meeting_admin = $('#meeting_username_hidden').val();
        $.ajax({
            url: "/remove-participant",
            method: "POST",
            data: {
                id_meeting: id_meeting,
                meeting_admin: meeting_admin,
                participant_username: participant_username,
            }
        }).done(function (usersList) {
             location.reload();
        });
    });
    $('#edit_meeting_date').datetimepicker();
    $("#add_participant").click(function (){
        var randomNumber = Math.round(Math.random()*100000000000,2);
        var html = `
            <div class="meeting-participant search">
               <input class="search-participant" id="search_users_`+randomNumber+`" placeholder="Search...">
               <div id="search_results_`+randomNumber+`" class="search-results"></div>
               <button class="remove-search-participant">
                   <img src="img/exit-black.svg">
               </button>
            </div>
        `;
        $('.meeting-participants-edit').append(html);
        $('.remove-search-participant').click(function(){
            $(this).parent().remove();
        })
        $('#search_users_'+randomNumber).keyup(function () {
            let search_query = $(this).val();
            $.ajax({
                url: "/search-user",
                method: "POST",
                data: { search_query: search_query }
            }).done(function (usersList) {
                $('#search_results_'+randomNumber+' > *').removeClass('filled').remove();

                let html = '';
                for (let i = 0; i < usersList.length; i++) {
                    let user = usersList[i];
                    let iconColor = user.color;
                    if(iconColor == '' || iconColor == null || iconColor == undefined){
                        let randomColor = "#" + Math.floor(Math.random()*16777215).toString(16);
                        iconColor = "#0d7377";
                    }
                    html += `
                    <div class="user-result" data-username="`+user.username+`">
                        <div class="user-icon" style="background-color:`+iconColor+`">
                           <span>`+user.firstName.charAt(0)+`</span>
                           <span>`+user.lastName.charAt(0)+`</span>
                        </div>
                        <span class="username">`+user.fullName+`</span>
                    </div>
                `;
                }

                $('#search_results_'+randomNumber).addClass('filled').append(html);

                $('.user-result').click(function(){
                    let participantUsername = $(this).attr('data-username');
                    let idMeeting = $('#id_meeting_hidden').val();
                    let meetingAdmin = $('#meeting_username_hidden').val();
                    $('#search_results_'+randomNumber+' > *').remove();
                    $.ajax({
                        url: "/add-participant-to-meeting",
                        method: "POST",
                        data: {
                            participantUsername: participantUsername,
                            idMeeting: idMeeting,
                            meetingAdmin: meetingAdmin,
                        }
                    }).done(function (usersList) {
                        $('.floating-menu .search-results > *').remove();
                        $('.floating-menu .search-results').removeClass('filled');

                        $('.participants-container').append("<span class='successfull_addition'>" +
                            "An invitation has been sent to the user." +
                            "</span>");
                        setTimeout(function (){
                            $('.successfull_addition').slideUp();
                        },3000);

                    });
                });
            });
        });
    });
});