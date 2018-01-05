var websocket;
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojknockout', 'ojs/ojdatagrid', 'ojs/ojarraydatagriddatasource'],
 function(oj, ko, $, app) {

    function TERViewModel() {
		
		if (window.WebSocket==undefined) {
			alert("Lo sentimos, pero tu navegador no soporta este tipo de comunicación");
			return;
		}
		
		self.salir=function(){
			var mensaje = {
				tipo : "jugadorabandona",
				email : self.jugador()
			};
			
			websocket.send(JSON.stringify(mensaje));
			app.router.go("juegos");
			location.reload();
		};
		
		self.cerocero=function(){
			enviarmovimiento(0, 0);
		};
		
		self.cerouno=function(){
			enviarmovimiento(0, 1);
		};
		
		self.cerodos=function(){
			enviarmovimiento(0, 2);
		};
		
		self.unocero=function(){
			enviarmovimiento(1, 0);
		};
		
		self.unouno=function(){
			enviarmovimiento(1, 1);
		};
		
		self.unodos=function(){
			enviarmovimiento(1, 2);
		};
		
		self.doscero=function(){
			enviarmovimiento(2, 0);
		};
		
		self.dosuno=function(){
			enviarmovimiento(2, 1);
		};
		
		self.dosdos=function(){
			enviarmovimiento(2, 2);
		};
		
		
		self.jugador = ko.observable(sessionStorage.jugador);
		self.jugador1 = ko.observable();
		self.jugador2 = ko.observable();
		self.turno = ko.observable("O");
		self.tablero=ko.observable(new oj.ArrayDataGridDataSource([
				[' ',' ',' '],
				[' ',' ',' '],
				[' ',' ',' ']
			]));
		
		self.jugador1bind = ko.computed(function() {
			return JSON.stringify(self.jugador1());
			
		});
		
		self.jugador2bind = ko.computed(function() {
			return JSON.stringify(self.jugador2());
			
		});
		
		self.turnobind = ko.computed(function() {
			return JSON.stringify(self.turno());
			
		});
		
		var url="ws://localhost:8080/ServidorWeb/websocket?jugador=" + self.jugador();
		websocket=new WebSocket(url);
		
		websocket.onopen=function(){
			console.log("Conectado al WebSocket");
		}
		websocket.onerror=function(){
			alert("Error conectando al webSocket");
			app.router.go("juegos");
		}
		websocket.onclose=function(){
			console.log("Desconectado del webSocket");
		}
		websocket.onmessage=function(respuesta){
			var mensaje = JSON.parse(respuesta.data);
			
			if(mensaje.tipo == "recibirpartida"){
				mostrarinforpartida(mensaje);
				
			} else if(mensaje.tipo == "movimiento"){
				procesarmovimiento(mensaje);
				
			} else if(mensaje.tipo == "jugadorabandona"){
				alert(mensaje.mensaje);
				app.router.go("juegos");
				location.reload();
			}
		}
		
		function procesarmovimiento(mensaje){
			if(mensaje.legalidad == "ilegal"){
				alert(mensaje.tipoilegalidad);
				
			} else {
				hacermovimiento(mensaje);
			}
		}
		
		function hacermovimiento(mensaje){
			self.tablero(new oj.ArrayDataGridDataSource(mensaje.tablero));	
			
			if (mensaje.fin == "si"){
				alert("Gana: " + mensaje.ganador);
				partidaterminada();
				
			} else if (mensaje.fin == "empate"){
				alert("Empate");
				partidaterminada();
				
			}
			
			if (self.turno() == "O"){
				self.turno("X");
			} else {
				
				self.turno("O");
			}
		}
		
		function partidaterminada(){
			var mensaje = {
				tipo : "partidaterminada",
				email : self.jugador()
			};
			
			websocket.send(JSON.stringify(mensaje));
			app.router.go("juegos");
			location.reload();
		}
		
		waitForSocketConnection();
		
		//cogida de internet y ajustada
		function waitForSocketConnection(){
			setTimeout(
				function () {
					if (websocket.readyState === 1) {
						console.log("Connection is made")
						recibirpartida();
						return;

					} else {
						console.log("wait for connection...")
						waitForSocketConnection();
					}

        }, 5); // wait 5 milisecond for the connection...
		}
		
		function recibirpartida(){
			var mensaje = {
				tipo : "recibirpartida",
				email : self.jugador()
			};
			
			websocket.send(JSON.stringify(mensaje));
		}
		
		function mostrarinforpartida(info){
			self.jugador1("O" + " - " + info.jugador1);
			self.jugador2("X" + " - " + info.jugador2);
		}
		
		function enviarmovimiento(filaa, columnaa){
			var mensaje = {
				tipo : "enviarmovimiento",
				jugador : self.jugador(),
				fila : filaa,
				columna : columnaa
			};
			
			websocket.send(JSON.stringify(mensaje));
		}
		
	}
	
    return new TERViewModel();
  }
);
