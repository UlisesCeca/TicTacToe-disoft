var websocket;
define(['ojs/ojcore', 'knockout', 'jquery', 'appController'],
 function(oj, ko, $,app) {

    function JuegosViewModel() {
		self.logout=function(){
			var p = {tipo:"Logout",email: sessionStorage.jugador};
			$.ajax({
				type:"POST",
				url:"http://localhost:8080/ServidorWeb/Logout.jsp",
				data: "p="+JSON.stringify(p),
				success: function(respuesta, status, request){
					if (respuesta.tipo=="OK"){
						app.router.go("login");
						location.reload();
					}else
						alert(respuesta.texto);
			}
			}).fail(function(respuesta){
				console.log(respuesta.responseText);
			}); 
		}; 
		
		self.ter=function(){
			var p = {tipo:"EstablecerJuego",email: sessionStorage.jugador, juego: "TresEnRaya"};
			enviarMensaje(p);
			app.router.go("saladeespera");
		};
		
		self.flota=function(){
			var p = {tipo:"EstablecerJuego",email: sessionStorage.jugador, juego: "HundirFlota"};
			enviarMensaje(p);
			app.router.go("saladeespera");
		};
		
		function enviarMensaje(p){
			$.ajax({
					type:"POST",
					url:"http://localhost:8080/ServidorWeb/EstablecerJuego.jsp",
					data: "p="+JSON.stringify(p),
					success: function(respuesta, status, request){
						if (respuesta.tipo=="OK"){
							app.router.go("saladeespera");
						}
				}
				}).fail(function(respuesta){
					console.log(respuesta.responseText);
				}); 
		}
		
		self.cambiarPassword=function(){
			app.router.go("cambiarPassword");
		};
		
		self.verRanking=function(){
			app.router.go("verranking");
		};
	}
    return new JuegosViewModel();
  }
);

