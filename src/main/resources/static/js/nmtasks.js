$("#btn-login-nav").click(function() {
  $("#welcome-msg").hide();
  $("#login-register").show();
  return false;
});
$("#btn-login, #btn-register").click(function() {
  $("#inp-q").val($(this).text());
});
