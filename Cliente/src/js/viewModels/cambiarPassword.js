
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojknockout', 'ojs/ojinputtext'],
 function(oj, ko, $, app) {

    function CambiarPasswordViewModel() {
		self.oldpassword=ko.observable();
		self.pwd1=ko.observable();
		self.pwd2=ko.observable();
		self.cambiar=function(){
			var p = {tipo:"CambiarPassword",email: sessionStorage.jugador, oldpassword: self.oldpassword(), pwd1:self.pwd1(), pwd2:self.pwd2()};
			$.ajax({
				type:"POST",
				url:"http://localhost:8080/ServidorWeb/CambiarPassword.jsp",
				data: "p="+JSON.stringify(p),
				success: function(respuesta, status, request){
					if (respuesta.tipo=="OK"){
						alert("Password Cambiada");
						app.router.go("juegos");
					}else
						alert(respuesta.texto);
			}
			}).fail(function(respuesta){
				console.log(respuesta.responseText);
			}); 
		}; 
		
		self.cancelar=function(){
			app.router.go("juegos");
		};
	}
    return new CambiarPasswordViewModel();
  }
);
