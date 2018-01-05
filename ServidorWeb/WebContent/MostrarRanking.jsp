<%@ page language="java" contentType="application/json ; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "org.json.*,dominio.Manager"%>
<%
response.setHeader("Access-Control-Allow-Origin", "*");

String p=request.getParameter("p");
JSONObject resultado=new JSONObject();

try{
	
 JSONObject jso= new JSONObject(p);
 if(!jso.getString("tipo").equals("MostrarRanking")){
  resultado.put("tipo","NOK");
  resultado.put("texto","Mensaje inesperado");
 }else{
	String email = jso.getString("email");
	String juego = jso.getString("juego");
	String filtro = jso.getString("filtro");
	juego = juego.substring(2, juego.length() - 2);
	if (filtro.equalsIgnoreCase("[\"victorias\"]")) resultado = Manager.get().getRanking().obtenerRankingVictorias(juego, email);
	else if (filtro.equalsIgnoreCase("[\"derrotas\"]")) resultado = Manager.get().getRanking().obtenerRankingDerrotas(juego, email);
	else if (filtro.equalsIgnoreCase("[\"partidas\"]")) resultado = Manager.get().getRanking().obtenerRankingPartidas(juego, email);
	resultado.put("tipo","OK");
	
 }
}
catch(Exception e){
 resultado.put("tipo","NOK");
 resultado.put("texto",e.getMessage());
}
%>

<%= resultado.toString()%>