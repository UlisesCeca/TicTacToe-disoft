package dominio;

import javax.websocket.Session;

import org.json.JSONException;
import org.json.JSONObject;

import dao.DAOUsuario;

public class Usuario {
	private String email;
	private String juego;
	private String password;
	private Session session;
	private Partida partida;
		
	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	public Usuario(String email, String password){
		this.email=email;
		this.password = password;
	}
	
	public Usuario(String email) {
		this.email=email;
	}

	public boolean comprobarPassword(String password2){
		return this.password.equalsIgnoreCase(password2);
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getEmail() {
		return email;
	}

	public String getJuego() {
		return juego;
	}

	public String getPassword() {
		return password;
	}
	
	public void login() throws Exception{
		DAOUsuario.login(this);
	}
	
/*	public void salirDePartida(String motivo) throws JSONException{
		this.getPartida().sacarJugador(this, motivo);
	} */
	
	public void cambiarPassword() throws Exception{
		DAOUsuario.cambiarPassword(this);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void registrarse() throws Exception {
		DAOUsuario.registrarse(this);
		
	}

	public void setJuego(String juego) {
		this.juego = juego;
	}

	public void existe() throws Exception {
		DAOUsuario.existe(this);
	}
}
