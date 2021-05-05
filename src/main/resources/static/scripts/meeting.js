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
    $('.meeting-title input').keydown(function(){
       if($(this).val() != ''){
         $('.input-group.form-group.meeting-title').addClass('selected');
       }else{
          $('.input-group.form-group.meeting-title').removeClass('selected');
       }
    });
});