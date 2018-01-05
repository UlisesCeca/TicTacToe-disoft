package dominio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import javax.websocket.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoWriteException;

import hundirflota.HundirFlota;
import mail.MailSender;
import tresenraya.TresEnRaya;
import websocket.WSServer;

public class Manager {
	private static Manager yo;
	private Hashtable<String, Usuario> usuariosOciosos;
	private Hashtable<String, Usuario> usuariosOnline;
	private Hashtable<Integer, Partida> partidas;
	private ArrayList<String> juegos = new ArrayList<String>();
	
	private Manager(){
		this.usuariosOciosos = new Hashtable<String, Usuario>();
		this.usuariosOnline = new Hashtable<String, Usuario>();
		this.partidas = new Hashtable<Integer, Partida>();
		this.juegos.add("TresEnRaya");
		this.juegos.add("HundirFlota");
	}
	
	public static Manager get(){
		if (yo==null) {
			synchronized (Manager.class) {
				if (yo == null)
					yo = new Manager();				
			}
		}
		return yo;
	}
	
	public Ranking getRanking(){
		return Ranking.get();
	}
	
	public void login(String email, String password) throws Exception {
		Usuario usuario = new Usuario(email, generarPasswordMD5(password));
		
		if (estaOnline(usuario.getEmail())) throw new Exception(usuario.getEmail() + " ya ha iniciado sesion");
		
		usuario.login();
		addOnline(usuario);
		
		System.out.println("Ha iniciado sesion " + usuario.getEmail());
		
	}
	
	private void addOnline(Usuario usuario) {
		this.usuariosOnline.put(usuario.getEmail(), usuario);
		
	}

	public boolean estaOnline(String nombre) {
		if (this.usuariosOnline.get(nombre) != null){
			return true;
		}
		
		return false;
	}
	
	private void crearPartida(Usuario jugador1, Usuario jugador2) throws JSONException {
		quitarOcioso(jugador1.getEmail());
		quitarOcioso(jugador2.getEmail());
		if(jugador1.getJuego().equalsIgnoreCase("TresEnRaya")) addPartida(new TresEnRaya(jugador1, jugador2));
		else if(jugador1.getJuego().equalsIgnoreCase("HundirFlota")) addPartida(new HundirFlota(jugador1, jugador2));
		
	} 
	
	public void addOcioso(Usuario usuario) {
		this.usuariosOciosos.put(usuario.getEmail(), usuario);
		
	}
	
	public void quitarOcioso(String email){
		this.usuariosOciosos.remove(email);
	}
	
	public void quitarJuego(String email){
		this.usuariosOciosos.get(email).setJuego(null);
	}
	
	public void buscarPartida(String email) throws JSONException{
		Usuario usuario = this.usuariosOciosos.get(email);
		String claveaux;
		Enumeration<String> clave = this.usuariosOciosos.keys();
		JSONObject jso = new JSONObject();
		
		while(clave.hasMoreElements()) {
			claveaux = clave.nextElement();
			if (this.usuariosOciosos.get(claveaux).getJuego().equalsIgnoreCase(usuario.getJuego()) 
					&& !this.usuariosOciosos.get(claveaux).getEmail().equalsIgnoreCase(email)){
				System.out.println("Oponente encontrado: " + this.usuariosOciosos.get(claveaux).getEmail());
				
				jso.put("tipo", "oponenteencontrado");
				jso.put("juego", usuario.getJuego());
				WSServer.send(usuario.getSession(), jso);
				WSServer.send(this.usuariosOciosos.get(claveaux).getSession(), jso);
				quitarSession(email);
				quitarSession(this.usuariosOciosos.get(claveaux).getEmail());
				crearPartida(usuario, this.usuariosOciosos.get(claveaux));
				mandarSalaEspera();
				break;
			}
				
		}
	}
	
	private void addPartida(Partida partida){
		this.partidas.put(partida.getIdPartida(), partida);
		System.out.println("Se ha creado la partida " + partida.getIdPartida());
		System.out.println(partida.juego);
		System.out.println(partida);
	} 
	
	public void registrarse(String email, String password1, String password2) throws Exception{
		MailSender webserver = new MailSender();
		Usuario usuario = new Usuario(email, password1);
		
		if (!webserver.esEmailValido(email)) throw new Exception("El dominio del email introducido no es valido");
		
		if(!usuario.comprobarPassword(password2)) throw new Exception("Las contraseñas no coinciden");
		
		usuario.setPassword(generarPasswordMD5(usuario.getPassword()));
		
		System.out.println("Se va registrar "+ usuario.getEmail());
		try {
			usuario.registrarse();
		} catch(MongoWriteException e){
			if(e.getCode()==11000) throw new Exception("Este email ya está registrado no esta disponible");
		}
	}

	public void setSession(String email, Session session) {
		if (usuariosOnline.get(email) != null) usuariosOnline.get(email).setSession(session);
	}
	
	public void quitarSession(String email) {
		if (usuariosOnline.get(email).getSession() != null) usuariosOnline.get(email).setSession(null);
	}
	
	public void mandarSalaEspera() throws JSONException{
		String claveaux;
		Enumeration<String> clave = this.usuariosOciosos.keys();
		JSONObject jso = new JSONObject();
		jso.put("tipo", "saladeespera");
		jso.put("usuarios", crearJSONASalaEspera());
		
		while(clave.hasMoreElements()) {
			claveaux = clave.nextElement();
			WSServer.send(this.usuariosOciosos.get(claveaux).getSession(), jso);
		}			
	}
	
	public void mandarPartidaTER(Partida partida) throws JSONException{
		JSONObject jso = new JSONObject();
		
		jso.put("tipo", "recibirpartida");
		jso.put("jugador1", partida.getUsuarios()[0].getEmail());
		jso.put("jugador2", partida.getUsuarios()[1].getEmail());

		WSServer.send(partida.getUsuarios()[0].getSession(), jso);
		WSServer.send(partida.getUsuarios()[1].getSession(), jso);			
	}
	
	public void mandarPartidaHF(Partida partida) throws JSONException{
		JSONObject jso = new JSONObject();
		
		jso.put("tipo", "recibirpartida");
		jso.put("jugador1", partida.getUsuarios()[0].getEmail());
		jso.put("jugador2", partida.getUsuarios()[1].getEmail());
		jso.put("tableroJugador1", partida.getTableroJugador1());
		WSServer.send(partida.getUsuarios()[0].getSession(), jso);

		jso.remove("tableroJugador1"); //por seguridad
		jso.put("tableroJugador2", partida.getTableroJugador2());
		WSServer.send(partida.getUsuarios()[1].getSession(), jso);
			
			
	}
	
	public void mandarPartida(String email) throws JSONException{
		Partida partida = this.partidas.get(this.usuariosOnline.get(email).getPartida().getIdPartida());
		
		if (partida.getJuego().equalsIgnoreCase("TresEnRaya")) mandarPartidaTER(partida);
		else if (partida.getJuego().equalsIgnoreCase("HundirFlota")); mandarPartidaHF(partida);
	}
	
	public void hacerMovimiento(JSONObject movimientojso) throws Exception{
		Usuario jugador = this.usuariosOnline.get(movimientojso.get("jugador"));
		int fila = (int) movimientojso.get("fila");
		int columna = (int) movimientojso.get("columna");
		Movimiento movimiento = new Movimiento(jugador, fila, columna);
		Partida partida = jugador.getPartida();
		JSONObject jso = partida.mover(movimiento);
		jso.put("tipo", "movimiento");
		if (jso.getString("legalidad").equalsIgnoreCase("ilegal")){
			WSServer.send(jugador.getSession(), jso);
		} else {
			WSServer.send(partida.getUsuarios()[0].getSession(), jso);
			WSServer.send(partida.getUsuarios()[1].getSession(), jso);
			
		}
	}
	
	public JSONArray crearJSONASalaEspera() throws JSONException{
		String claveaux;
		Enumeration<String> clave = this.usuariosOciosos.keys();
		JSONArray jsona = new JSONArray();
		JSONObject jso = new JSONObject();
		
		while(clave.hasMoreElements()) {
			claveaux = clave.nextElement();
			jso = new JSONObject();
			jso.put("email", claveaux);
			jso.put("juego", this.usuariosOciosos.get(claveaux).getJuego());
			jsona.put(jso);
		}
		
		return jsona;
	}

	public void logout(String email) throws Exception {

		   if (estaOnline(email)) {
			   quitarOnline(email);
			   System.out.println(email + " ha cerrado sesión");
		   } else throw new Exception("El usuario no está en linea");
	}
	
	private void quitarOnline(String email) {
		this.usuariosOnline.remove(email);
		
	}
 	
 	public void establecerJuego(String email, String juego) throws Exception{
 		boolean existe = false;
 		for(int i = 0; i < this.juegos.size(); i++){
 			if (this.juegos.get(i).equalsIgnoreCase(juego));
 				existe = true;
 				break;
 		}
 		
 		if (!existe) throw new Exception("El juego seleccionado no existe");
 		
 		Usuario usuario = this.usuariosOnline.get(email);
 		usuario.setJuego(juego);
 		System.out.println("El usuario " + usuario.getEmail() + " ha seleccionado el juego " + usuario.getJuego());
 		
 		addOcioso(usuario); 	
 	}
 	
 	public void cambiarPassword(String email, String oldPassword, String newPassword, String newPassword2) throws Exception{
 		Usuario usuario = this.usuariosOnline.get(email);
 		
 		if (usuario == null) throw new Exception("El usuario debe estar online");
 		
 		if (usuario.comprobarPassword(generarPasswordMD5(oldPassword)) && newPassword.equalsIgnoreCase(newPassword2)){
 			usuario.setPassword(generarPasswordMD5(newPassword));
 			usuario.cambiarPassword();
 		} else throw new Exception("Las passwords no coinciden");
 		
 	}
 	
 	public void recuperarPassword(String email) throws Exception{
 		Random randomGenerator = new Random();
 		String newPassword = String.valueOf(randomGenerator.nextInt(100000));
 		Usuario usuario = new Usuario(email, generarPasswordMD5(newPassword));
 		usuario.existe();
 		usuario.cambiarPassword();
		MailSender mailSender = new MailSender();
		mailSender.sendMail(usuario.getEmail(), newPassword); 		
 	}
 	
 	
 	//cogido de internet y ajustado
 	public String generarPasswordMD5(String password) throws NoSuchAlgorithmException{
 		String passwordMD5 = null;

			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			//Add password bytes to digest
			md.update(password.getBytes());
			//Get the hash's bytes 
			byte[] bytes = md.digest();
			//This bytes[] has bytes in decimal format;
			//Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			//Get complete hashed password in hex format
			passwordMD5 = sb.toString();
 		
 		return passwordMD5;
 	}

	public void terminarPartida(String email) {
		Usuario usuario = this.usuariosOnline.get(email);
		quitarJuego(usuario.getEmail());
		quitarSession(usuario.getEmail());
		this.partidas.remove(usuario.getPartida().getIdPartida());
		usuario.setPartida(null);
	}

	public void jugadorAbandona(String email) throws JSONException {
		JSONObject jso = new JSONObject();
		Usuario usuario = this.usuariosOnline.get(email);
		Usuario oponente;
		Partida partida = this.partidas.get(usuario.getPartida().getIdPartida());
		jso.put("tipo", "jugadorabandona");
		jso.put("mensaje", "El Oponente ha abandonado. Tú ganas.");
		oponente = partida.getOponente(usuario);
		partida.setUsuarioConElTurno(oponente);
		partida.establecerResultados();
		partida.almacenarResultadosNoEmpate();
		WSServer.send(oponente.getSession(), jso);
		terminarPartida(usuario.getEmail());
		terminarPartida(oponente.getEmail());		
	}
	
}
