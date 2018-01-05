
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojknockout', 'ojs/ojinputtext'],
 function(oj, ko, $, app) {

    function LoginViewModel() {
		self.nombreDeUsuario=ko.observable();
		self.pwd=ko.observable();
		self.entrar=function(){
			var p = {tipo:"Login",email: self.nombreDeUsuario(), password:self.pwd()};
			$.ajax({
				type:"POST",
				url:"http://localhost:8080/ServidorWeb/Login.jsp",
				data: "p="+JSON.stringify(p),
				success: function(respuesta, status, request){
					if (respuesta.tipo=="OK"){
						sessionStorage.jugador=self.nombreDeUsuario();
						app.router.go("juegos");
					}else
						alert(respuesta.texto);
			}
			}).fail(function(respuesta){
				console.log(respuesta.responseText);
			}); 
		}; 
		self.registro=function(){
					app.router.go("registrarse");
		}
		
		self.recuperarpassword=function(){
					app.router.go("recuperarpassword");
		}
	}
    return new LoginViewModel();
  }
);
