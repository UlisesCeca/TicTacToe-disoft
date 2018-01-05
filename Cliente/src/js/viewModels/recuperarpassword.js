
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojknockout', 'ojs/ojinputtext'],
 function(oj, ko, $, app) {

    function RecuperarPasswordViewModel() {
		self.email=ko.observable();
		self.cambiar=function(){
			var p = {tipo:"RecuperarPassword",email: self.email()};
			$.ajax({
				type:"POST",
				url:"http://localhost:8080/ServidorWeb/RecuperarPassword.jsp",
				data: "p="+JSON.stringify(p),
				success: function(respuesta, status, request){
					if (respuesta.tipo=="OK"){
						alert("Nueva password enviada");
						app.router.go("login");
					}else
						alert(respuesta.texto);
			}
			}).fail(function(respuesta){
				console.log(respuesta.responseText);
			}); 
		};
		
		self.cancelar=function(){
			app.router.go("login");
		};
	}
    return new RecuperarPasswordViewModel();
  }
);
