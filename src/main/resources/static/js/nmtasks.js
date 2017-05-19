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
function searchTasks() {
  taskTable.load(0, {
    complete: $("#select-ci").val(),
    search: $("#inp-search").val()
  });
}
$("#form-search").submit(function() {
  searchTasks();
  return false;
});
// tasks page
if ($("#tbl-tasks").length) {
  taskTable = $("#tbl-tasks").dataTable();
  searchTasks();
}