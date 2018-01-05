package websocket;



import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import dominio.Manager;

@ServerEndpoint(value="/websocket")

public class WSServer {
private static Hashtable<String, Session> sesiones = new Hashtable<String, Session>();
	
	@OnOpen
	public void open(Session session) throws JSONException{
		String sessionId = session.getId();
		Map<String, List<String>> parametros = session.getRequestParameterMap();
		String email = parametros.get("jugador").get(0);
		Manager.get().setSession(email, session);
		sesiones.put(session.getId(), session);
		System.out.println("Creada sesion de websocket para " + email + ": "  + sessionId);
		JSONObject jso = new JSONObject();
		try{
			jso.put("tipo", "sessionId");
			jso.put("sessionId", session.getId());
		}
		catch (Exception e){}
		send(session, jso);
	}
	
	public static void send(Session session, JSONObject jso){
		try{
			session.getBasicRemote().sendText(jso.toString());
		}
		catch (IOException e){
			sesiones.remove(session.getId());
		}
	}
	
	@OnMessage
	public void recibir(Session session, String msg){
		try {
			JSONObject jso = new JSONObject(msg);
			if (jso.getString("tipo").equalsIgnoreCase("saladeespera")) {
				Manager.get().mandarSalaEspera();
				Manager.get().buscarPartida(jso.getString("email"));
				
			} else if (jso.getString("tipo").equalsIgnoreCase("salirsalaespera")) { 
				Manager.get().quitarJuego(jso.getString("email"));
				Manager.get().quitarSession(jso.getString("email"));
				Manager.get().quitarOcioso(jso.getString("email"));
				Manager.get().mandarSalaEspera();
					
			} else if (jso.getString("tipo").equalsIgnoreCase("recibirpartida")) { 
				Manager.get().mandarPartida(jso.getString("email"));
					
			} else if (jso.getString("tipo").equalsIgnoreCase("enviarmovimiento")) { 
				Manager.get().hacerMovimiento(jso);
					
			} else if (jso.getString("tipo").equalsIgnoreCase("partidaterminada")) { 
				Manager.get().terminarPartida(jso.getString("email"));
					
			} else if (jso.getString("tipo").equalsIgnoreCase("jugadorabandona")) { 
				Manager.get().jugadorAbandona(jso.getString("email"));
		}} catch (Exception e){
			
		}
	}
	
	@OnClose
	public void close (Session session){
		sesiones.remove(session.getId());
		System.out.println(session.getId() + " cerrada sesion");
	}
}