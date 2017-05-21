var taskTable;
$("#btn-login-nav").click(function() {
  $("#welcome-msg").hide();
  $("#login-register").show();
  return false;
});
$("#btn-login, #btn-register").click(function() {
  $("#inp-q").val($(this).text());
});
$("#select-ci").change(searchTasks);
$("#form-search").submit(function() {
  searchTasks();
  return false;
});


function searchTasks() {
  taskTable.load(0, {
    complete: $("#select-ci").val(),
    search: $("#inp-search").val()
  });
}
function refreshCheckboxes() {
  var state = $("#cb-select-all").is(":checked");
  $("#tbl-tasks tbody input[type=checkbox]").each(function() {
    $(this).prop("checked", state);
  });
}
// tasks page
if ($("#tbl-tasks").length) {
  taskTable = $("#tbl-tasks").dataTable({ onDataLoad: refreshCheckboxes });
  searchTasks();
  // select all checkbox
  $("#cb-select-all").click(refreshCheckboxes);
  // mark complete nav button
  $("#btn-mark-complete-nav").click(function() {
    var ids = [];
    var trs = $("#tbl-tasks tbody tr:has(input:checked)").each(function() {
      ids.push($(this).data("id"));
    });
    if (ids.length) {
      $.ajax({
        method: "GET",
        url: "api/mark-complete/task",
        data: { ids: ids},
        crud: taskTable,
        trs: trs
      }).done(function(data) {
        this.crud.writeMessage(data);
        if (data.success && $("#select-ci").val() == "n")
          this.trs.fadeOut();
      }).fail(function(xhr) {
        this.crud.writeMessage({ success: false, msg: xhr.responseText });
      });
    } else {
      taskTable.writeMessage({ success: false, msg: "Please select at least one task to mark complete."});
    }
    return false;
  });

}