var taskTable;
$("#btn-login-nav").click(function() {
  $("#welcome-msg").hide();
  $("#login-register").show();
  return false;
});
$("#btn-show-complete-nav").click(function() {
  if ($(this).hasClass("active")) {
    $(this).removeClass("active").find("span").removeClass("fa-check-square-o").addClass("fa-square-o");
  }
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
// tasks page
if ($("#tbl-tasks").length) {
  taskTable = $("#tbl-tasks").dataTable();
  searchTasks();
}