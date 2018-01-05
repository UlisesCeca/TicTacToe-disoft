package tresenraya;

import org.json.JSONException;
import org.json.JSONObject;

import dominio.Movimiento;
import dominio.Partida;

import dominio.Usuario;

public class TresEnRaya extends Partida {
	private String [][] tablero;
	
	public TresEnRaya(Usuario jugador1, Usuario jugador2){
		super(jugador1, jugador2);
		tablero = new String[3][3];
		iniciarTablero();
	}
	
	private void iniciarTablero() {
		for(int i = 0; i < this.tablero.length; i++)
			for(int j = 0; j < this.tablero[0].length; j++)
				tablero[i][j]=" ";
		
	}

	@Override
	protected boolean comprobarLegalidad(Movimiento m, JSONObject jso) throws Exception {
		  
		  if (this.usuarioConElTurno != m.getJugador()){
			  jso.put("legalidad", "ilegal");
			  jso.put("tipoilegalidad", "No es tu turno");
			  return false;
		  } 
		  
		  if (m.getColumna() > this.tablero[0].length - 1 || m.getColumna() < 0 || m.getFila() > this.tablero.length - 1 || m.getFila() < 0){
			  jso.put("legalidad", "ilegal");
			  jso.put("tipoilegalidad", "No mueva fuera del tablero");
			  return false;
		  }
		  
		  if (this.tablero[m.getFila()][m.getColumna()] != " "){
			  jso.put("legalidad", "ilegal");
			  jso.put("tipoilegalidad", "Casilla ocupada");
			  return false;
		  }
		  
		  jso.put("legalidad", "legal");
		  return true;
	}
	
	@Override
	protected void actualizarTablero(Movimiento m, JSONObject jso) throws JSONException {
		if (m.getJugador() == this.usuarios[0]){
			this.tablero[m.getFila()][m.getColumna()] = "O";

		} else {
			this.tablero[m.getFila()][m.getColumna()] = "X";			
		}
		
		jso.put("tablero", this.tablero);
	}

	@Override
	protected boolean finPartida() {
		
		for(int i = 0; i < this.fichas.length;i++){
			if(this.tablero[0][0]==this.fichas[i] && this.tablero[0][1]==this.fichas[i] && this.tablero[0][2]==this.fichas[i] ){
				return true;
				
			} else if(this.tablero[1][0]==this.fichas[i] && this.tablero[1][1]==this.fichas[i] && this.tablero[1][2]==this.fichas[i] ){
				return true;
				
			} else if(this.tablero[2][0]==this.fichas[i] && this.tablero[2][1]==this.fichas[i] && this.tablero[2][2]==this.fichas[i] ){
				return true;
				
			} else if(this.tablero[0][0]==this.fichas[i] && this.tablero[1][0]==this.fichas[i] && this.tablero[2][0]==this.fichas[i] ){
				return true;
				
			} else if(this.tablero[0][1]==this.fichas[i] && this.tablero[1][1]==this.fichas[i] && this.tablero[2][1]==this.fichas[i] ){
				return true;
				
			} else if(this.tablero[0][2]==this.fichas[i] && this.tablero[1][2]==this.fichas[i] && this.tablero[2][2]==this.fichas[i] ){
				return true;
				
			} else if(this.tablero[0][0]==this.fichas[i] && this.tablero[1][1]==this.fichas[i] && this.tablero[2][2]==this.fichas[i] ){
				return true;
				
			} else if(this.tablero[2][0]==this.fichas[i] && this.tablero[1][1]==this.fichas[i] && this.tablero[0][2]==this.fichas[i] ){
				return true;
			}
		}
		

		if (checkEmpate()) {
			this.usuarioConElTurno = null;
			return true;
		}
		
		return false;
	}
	
	protected boolean checkEmpate() {
		
		for(int i = 0; i < this.tablero.length; i++){
			for(int j = 0; j < this.tablero[0].length; j++){
				if(this.tablero[i][j] == " ") return false;
			}
		}
		
		return true;
	}

	@Override
	public String[][] getTableroJugador1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getTableroJugador2() {
		// TODO Auto-generated method stub
		return null;
	}

}