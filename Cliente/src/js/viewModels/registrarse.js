
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojknockout', 'ojs/ojinputtext'],
 function(oj, ko, $, app) {

    function RegistrarseViewModel() {
		self.email=ko.observable();
		self.pwd1=ko.observable();
		self.pwd2=ko.observable();
		self.registrar=function(){
			var p = {tipo:"Registrarse",email: self.email(), pwd1:self.pwd1(), pwd2:self.pwd2()};
			$.ajax({
				type:"POST",
				url:"http://localhost:8080/ServidorWeb/Registrarse.jsp",
				data: "p="+JSON.stringify(p),
				success: function(respuesta, status, request){
					if (respuesta.tipo=="OK"){
						alert("Registro correcto");
						app.router.go("login");
					}else
						alert(respuesta.texto)
			}
			}).fail(function(respuesta){
				console.log(respuesta.responseText);
			}); 
		}; 
		
		self.cancelar=function(){
			app.router.go("login");
		};
	}
    return new RegistrarseViewModel();
  }
);
