<%@ page language="java" contentType="application/json ; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "org.json.*,dominio.Manager"%>
<%
response.setHeader("Access-Control-Allow-Origin", "*");

String p=request.getParameter("p");
JSONObject resultado=new JSONObject();

try{
	
 JSONObject jso= new JSONObject(p);
 if(!jso.getString("tipo").equals("CambiarPassword")){
  resultado.put("tipo","NOK");
  resultado.put("texto","Mensaje inesperado");
 }else{
	 String email = jso.getString("email");
	 String oldPassword = jso.getString("oldpassword");
	 String newPassword = jso.getString("pwd1");
	 String newPassword2 = jso.getString("pwd2");
  Manager.get().cambiarPassword(email, oldPassword, newPassword, newPassword2);
  resultado.put("tipo","OK");
  resultado.put("texto","Password cambiada a " + email);
 }
}
catch(Exception e){
 resultado.put("tipo","NOK");
 resultado.put("texto",e.getMessage());
}
%>

<%= resultado.toString()%>