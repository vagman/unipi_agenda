$(document).ready(function () {
    function buildAutocompleteListeners(){
        $('.user-result').click(function(){
            //close drawer
            $('.add-participants-popup').removeClass("open");
            $('.add-participants-popup-filter').fadeOut();

            let userData = $(this).html();
            let participantUsername = $(this).attr('data-username');

            $('.meeting-participants-list').append(`
                <div class="meeting-participant" data-username="`+participantUsername+`">
                    <div class="meeting-participant-info">
                        `+userData+`
                    </div>
                    <div></div>
                </div>
            `);

            let participantsConcat = $('#meeting_participants_input_hidden').val();
            if(participantsConcat != ''){
                $('#meeting_participants_input_hidden').val(participantsConcat+'__separator__'+participantUsername);
            }else{
                $('#meeting_participants_input_hidden').val(participantUsername);
            }
        });
    }


    $('.card').datepicker({

        onSelect: function (date) {
            $(this).parent().addClass("selected");
        }
    });
    $('.timepicker').timepicker({
        change: function () {
            $(this).parent().addClass("selected");
        }
    });
    $('.meeting-title input').keydown(function () {
        if ($(this).val() != '') {
            $('.input-group.form-group.meeting-title').addClass('selected');
        } else {
            $('.input-group.form-group.meeting-title').removeClass('selected');
        }
    });

    // participants popup
    $("button#add_participants").click(function (e) {
        e.preventDefault();
        $('.add-participants-popup').addClass("open");
        $('.add-participants-popup-filter').fadeIn();
        $('input#user_search').val('');
        $('.user-result').remove();
    });
    $('.add-participants-popup-filter, .add-participants-popup-close').click(function (e) {
        e.preventDefault();
        $('.add-participants-popup').removeClass("open");
        $('.add-participants-popup-filter').fadeOut();
    });
    $('input#user_search').keyup(function () {
        let search_query = $(this).val();
        $.ajax({
            url: "/search-user",
            method: "POST",
            data: { search_query: search_query }
        }).done(function (usersList) {
            $('div#user_search_results > *').remove();

            let html = '';
            for (let i = 0; i < usersList.length; i++) {
                let user = usersList[i];
                let iconColor = user.color;
                if(iconColor == '' || iconColor == null || iconColor == undefined){
                    let randomColor = "#" + Math.floor(Math.random()*16777215).toString(16);
                    iconColor = "#0d7377";
                }
                html += `
                    <div class="user-result" data-username="`+user.Username+`">
                        <div class="user-icon" style="background-color:`+iconColor+`">
                           <span>`+user.firstName.charAt(0)+`</span>
                           <span>`+user.lastName.charAt(0)+`</span>
                        </div>
                        <span class="username">`+user.fullName+`</span>
                    </div>
                `;
            }

            $('div#user_search_results').append(html);
            buildAutocompleteListeners();
        });
    });

});