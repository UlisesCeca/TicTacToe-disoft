package dominio;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import dao.DAOResultado;

public abstract class Partida {
	protected int idPartida;
	protected Usuario[] usuarios = new Usuario[2];
	protected Usuario usuarioConElTurno;
	protected Usuario ganador;
	protected Usuario perdedor;
	protected String juego;
	protected String [] fichas = new String[2];

	public Partida(Usuario jugador1, Usuario jugador2){
		this.idPartida = new Random().nextInt();
		this.usuarioConElTurno = jugador1;
		this.usuarios[0] = jugador1;
		this.usuarios[1] = jugador2;
		this.fichas[0] = "O";
		this.fichas[1] = "X";
		jugador1.setPartida(this);
		jugador2.setPartida(this);
		this.juego = jugador1.getJuego();
	}
	
	public Usuario getOponente(Usuario usuario){
		Usuario oponente = null;
		for (int i = 0; i < this.usuarios.length; i++){
			if (!this.usuarios[i].getEmail().equalsIgnoreCase(usuario.getEmail())){
				oponente = this.usuarios[i];
				break;
			}
		}
		return oponente;
	}
	
	public void setUsuarioConElTurno(Usuario usuarioConElTurno) {
		this.usuarioConElTurno = usuarioConElTurno;
	}

	public String[] getFichas() {
		return fichas;
	}

	public Usuario[] getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Usuario[] usuarios) {
		this.usuarios = usuarios;
	}
	
	public String getJuego() {
		return juego;
	}

	public Usuario getGanador() {
		return ganador;
	}

	public Usuario getPerdedor() {
		return perdedor;
	}
	
	public JSONObject mover(Movimiento m) throws Exception{
		JSONObject jso = new JSONObject();
		
		if (comprobarLegalidad(m, jso)){
			actualizarTablero(m, jso);
			if (!finPartida()){
				jso.put("fin", "no");
				setTurno(jso);
			} else {
				if (this.usuarioConElTurno != null){
					jso.put("fin", "si");
					establecerResultados();
					almacenarResultadosNoEmpate();
					jso.put("ganador", this.ganador.getEmail());
				} else {
					jso.put("fin", "empate");
					DAOResultado.almacenarResultadosEmpate(this, this.usuarios[0]);
					DAOResultado.almacenarResultadosEmpate(this, this.usuarios[1]);
				}
			}
		}
		
		return jso;
	}

	protected abstract boolean comprobarLegalidad(Movimiento m, JSONObject jso) throws Exception;
	protected abstract void actualizarTablero(Movimiento m, JSONObject jso) throws JSONException;
	protected abstract boolean finPartida() throws JSONException;

	protected void setTurno(JSONObject jso) throws JSONException{
		for (int i = 0; i < this.usuarios.length; i++){
			if (!this.usuarios[i].getEmail().equalsIgnoreCase(this.usuarioConElTurno.getEmail())){
				this.usuarioConElTurno = this.usuarios[i];
				break;
			}
		}
		jso.put("turno", this.usuarioConElTurno.getEmail());
	}
	
	protected Integer getIdPartida() {
		return idPartida;
	}
	
	public Usuario getJugadorConElTurno() {
		return usuarioConElTurno;
	}

	public void almacenarResultadosNoEmpate(){
		DAOResultado.almacenarResultadosGanador(this);
		DAOResultado.almacenarResultadosPerdedor(this);
	}
	
	public void establecerResultados() throws JSONException{
		for (int i = 0; i < this.usuarios.length; i++){
			if (!this.usuarios[i].getEmail().equalsIgnoreCase(this.usuarioConElTurno.getEmail())) this.perdedor = this.usuarios[i];
			else this.ganador = this.usuarioConElTurno;
		}
	}
	
	public abstract String[][] getTableroJugador1();

	public abstract String[][] getTableroJugador2();
}
