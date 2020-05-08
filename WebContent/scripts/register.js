$().ready (function () {
	
	$("#btn_register").click (function () {
		if ($("#tb_username").val() == "") {
			alert("Username can not be empty!");
			return;
		}
		if ($("#tb_password").val() == "") {
			alert("Password can not be empty!");
			return;
		}
		if ($("#tb_email").val() == "") {
			alert("Email can not be empty!");
			return;
		}
		document.location.href="http://localhost:8080/WebShopREST/";
	});
	
});