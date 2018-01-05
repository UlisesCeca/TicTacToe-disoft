<%@ page language="java" contentType="application/json ; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "org.json.*,dominio.Manager"%>
<%
response.setHeader("Access-Control-Allow-Origin", "*");

String p = request.getParameter("p");
JSONObject resultado = new JSONObject();
try{
	
 JSONObject jso = new JSONObject(p);
 if(!jso.getString("tipo").equals("Registrarse")){
  resultado.put("tipo","NOK");
  resultado.put("texto","Mensaje inesperado");
 }else{
  String email=jso.getString("email");
  String pwd1=jso.getString("pwd1");
  String pwd2=jso.getString("pwd2");
  Manager.get().registrarse(email, pwd1, pwd2);
  resultado.put("tipo","OK");
  resultado.put("texto","Te has registrado con el email " + email);
  
 }
}

catch(Exception e){
 resultado.put("tipo","NOK");
 resultado.put("texto", e.getMessage());
}
%>

<%=resultado.toString()%>