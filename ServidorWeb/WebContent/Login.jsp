<%@ page language="java" contentType="application/json ; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "org.json.*,dominio.Manager"%>
<%
response.setHeader("Access-Control-Allow-Origin", "*");

String p=request.getParameter("p");
JSONObject resultado=new JSONObject();

try{
	
 JSONObject jso= new JSONObject(p);
 if(!jso.getString("tipo").equals("Login")){
  resultado.put("tipo","NOK");
  resultado.put("texto","Mensaje inesperado");
 }else{
  String email = jso.getString("email");
  String password = jso.getString("password");
  Manager.get().login(email, password);
  resultado.put("tipo","OK");
  resultado.put("texto","Ha iniciado sesion" + email);
 }
}
catch(Exception e){
 resultado.put("tipo","NOK");
 resultado.put("texto",e.getMessage());
}
%>

<%= resultado.toString()%>