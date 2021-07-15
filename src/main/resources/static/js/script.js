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

// TODO: Remove margin-left of "btn-sucess":
// No Admin ==> delete/edit buttons do not show up