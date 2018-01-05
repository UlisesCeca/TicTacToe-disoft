var websocket;

define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojknockout', 'promise', 'ojs/ojtable'],
 function(oj, ko, $, app) {

    function SalaEsperaViewModel() {
		
		if (window.WebSocket==undefined) {
			alert("Lo sentimos, pero tu navegador no soporta este tipo de comunicación");
			return;
		}
		self.jugador=ko.observable(sessionStorage.jugador);
		
		self.volver=function(){
			var mensaje = {
				tipo : "salirsalaespera",
				email : self.jugador()
			};
			
			websocket.send(JSON.stringify(mensaje));
			app.router.go("juegos");
			location.reload();
		};
		
		var usuariosArray = [];
		self.usuariosesperando = ko.observable();
		
		var url="ws://localhost:8080/ServidorWeb/websocket?jugador=" + self.jugador();
		websocket=new WebSocket(url);
		
		websocket.onopen=function(){
			console.log("Conectado al WebSocket");
		}
		websocket.onerror=function(){
			alert("Error conectando al webSocket");
			app.router.go("login");
		}
		websocket.onclose=function(){
			console.log("Desconectado del webSocket");
		}
		websocket.onmessage=function(respuesta){
			var mensaje = JSON.parse(respuesta.data);
			
			if(mensaje.tipo == "saladeespera"){
				mostrarUsuarios(mensaje.usuarios);
				
			} else if (mensaje.tipo == "oponenteencontrado"){
				if (mensaje.juego == "TresEnRaya"){
					app.router.go("tresenraya");
				} else if (mensaje.juego == "HundirFlota"){
					app.router.go("hundirlaflota");
				}
				location.reload();
			}
		}
		
		
		
		waitForSocketConnection();
		
		//cogida de internet y ajustada
		function waitForSocketConnection(){
			setTimeout(
				function () {
					if (websocket.readyState === 1) {
						console.log("Connection is made")
						solicitarUsuarios();
						return;

					} else {
						console.log("wait for connection...")
						waitForSocketConnection();
					}

        }, 5); // wait 5 milisecond for the connection...
	}
		
		function solicitarUsuarios(){
			var mensaje = {
				tipo : "saladeespera",
				email : self.jugador()
			};
			
			websocket.send(JSON.stringify(mensaje));
		}
		
		function mostrarUsuarios(usuarios){
			usuariosArray = [];
			for(var i in usuarios){
				usuariosArray.push({Usuario: String(usuarios[i].email), Juego: String(usuarios[i].juego)});
			}
			self.usuariosesperando(new oj.ArrayTableDataSource(usuariosArray, {idAttribute: 'Usuario'}));
		}
		
	}
	
    return new SalaEsperaViewModel();
  }
);
