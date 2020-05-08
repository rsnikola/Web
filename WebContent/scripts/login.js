$().ready(function(){

	$("button#btn_login").click (function () {
		if ($("input#tb_user").val() == "") {
			alert("Username can not be empty!");
			return;
		}
		if ($("input#tb_pass").val() == "") {
			alert("Password can not be empty");
			return;
		}
		$.ajax({
			type: "POST",
//			url: "http://localhost:8080/WebShopREST/rest/login",
			url: "http://localhost:8080/NarsProj/rest/login",
			data : { username : $("input#tb_user").val(), password : $("input#tb_pass").val() }, 
			contentType : "application/json;charset=utf-8",
			dataType : "json",
		}).then (function(data) {
			if (data != null) {
				document.title = "Ulogovan korisnik " + data.username;
				$("#btn_login").hide();
				$("#btn_logout").show();
//				document.location.href="http://localhost:8080/WebShopREST/";
				document.location.href="http://localhost:8080/NarsProj/";
			} else {
				document.title = "Korisnicko ime i lozinka ne idu zajedno";
			}
		});
	});
	
	$("#btn_logout").click (function () {
//		$.post("http://localhost:8080/WebShopREST/rest/logout");
		$.post("http://localhost:8080/NarsProj/rest/logout");
		$("#btn_login").show();
		$("#btn_logout").hide();
	});
	
});