$("#btn-login-nav").click(function() {
  $("#welcome-msg").hide();
  $("#login-register").show();
  return false;
});
$("#btn-login, #btn-register").click(function() {
  $.ajax({
    url: "api/validateLogin",
    data: $("#form-login").serialize(),
  }).done(function(data) {
    alert(data);
  }).fail(function(xhr) {
    alert(xhr.responseText);
  });
});
