$(document).ready(function () {
    $('.datepicker').datepicker({
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
        $('.add-participants-popup').fadeIn();
        $('.add-participants-popup-filter').fadeIn();
    });
    $('.add-participants-popup-filter').click(function (e) {
        $('.add-participants-popup').fadeOut();
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
                let user = usersList[0];
                html += `
                    <div class="user-result">
                        <span class="username">`+user._Username+`</span>
                        <span class="full_name">`+user.fullName+`</span>
                    </div>
                `;
            }

            $('div#user_search_results').append(html);
        });
    })
});