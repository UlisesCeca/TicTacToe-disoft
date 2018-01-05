
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojknockout', 'ojs/ojselectcombobox', 'promise', 'ojs/ojtable'],
 function(oj, ko, $, app) {

    function VerRankingViewModel() {
		self.volver=function(){
			app.router.go("juegos");
		};
		self.evtData = ko.observable();
		self.juegoelegido = ko.observable();
		self.filtroelegido = ko.observable();	
		var rankingArray = [];
		self.ranking = ko.observable();		
		
		self.optionChangedHandler = function (event, data) {
			if (data.option == "value") {
				self.juegoelegido(data.value);
				enviarPeticion();
			}
		}
		
		self.optionChangedHandler2 = function (event, data) {
			if (data.option == "value") {
				self.filtroelegido(data.value);
				enviarPeticion();
			}
		}
		
		function enviarPeticion(){
			var p = {tipo:"MostrarRanking",email: sessionStorage.jugador, juego: self.juegoelegido(), filtro: self.filtroelegido()};
					$.ajax({
					type:"POST",
					url:"http://localhost:8080/ServidorWeb/MostrarRanking.jsp",
					data: "p="+JSON.stringify(p),
					success: function(respuesta, status, request){
					if (respuesta.tipo=="OK"){
						self.evtData(respuesta.tuposicion);
						mostrarRanking(respuesta.ranking);
					}
			}
			}).fail(function(respuesta){
					console.log(respuesta.responseText);
			}); 
		}
		
		function mostrarRanking(rankingg){
			rankingArray = [];
			for(var i in rankingg){
				rankingArray.push({Posicion: parseInt(i)+1, Usuario: rankingg[i].usuario, Cantidad: rankingg[i].cantidad});
			}
			
			self.ranking(new oj.ArrayTableDataSource(rankingArray, {idAttribute: 'Posicion'}));
		}
		
		self.logMsg = ko.computed(function() {
			return JSON.stringify(self.evtData());
			
		});
		
	}
	
    return new VerRankingViewModel();
  }
);
