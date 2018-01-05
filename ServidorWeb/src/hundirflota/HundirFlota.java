package hundirflota;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import dominio.Movimiento;
import dominio.Partida;
import dominio.Usuario;

public class HundirFlota extends Partida {
	
	public String[][] tableroJugador1 = new String [3][3];
	private String[][] tableroEnemigoJugador1 = new String [3][3];
	public  String[][] tableroJugador2 = new String [3][3];
	private String[][] tableroEnemigoJugador2 = new String [3][3];
	int barcos1 = 3;
	int barcos2 = 3;
	

	public HundirFlota(Usuario ganador, Usuario perdedor) throws JSONException {
		super(ganador, perdedor);
		iniciarTablero(this.tableroJugador1);
		iniciarTablero(this.tableroEnemigoJugador1);
		iniciarTablero(this.tableroJugador2);
		iniciarTablero(this.tableroEnemigoJugador2);
		colocarBarcos(this.tableroJugador1);
		colocarBarcos(this.tableroJugador2);
		
	} 
	
	private void colocarBarcos(String [][] tablero) {
		ArrayList<Integer> barcox = new ArrayList<Integer>();
		ArrayList<Integer> barcoy = new ArrayList<Integer>();
		
		for (int i = 0; i < tablero.length; i++) {
			barcox.add(i);
			barcoy.add(i);
        }
		
		Collections.shuffle(barcox);
		Collections.shuffle(barcoy);
        
		for(int i = 0; i < tablero.length; i++){
			tablero[barcox.get(i)][barcoy.get(i)] = "O";	
		}
	}

	private void iniciarTablero(String [][] tablero) {
		
		for(int i = 0; i < tablero.length; i++){
			for(int j = 0; j < tablero[0].length; j++){
				tablero[i][j]=" ";
			}
		}
		
		
	}

	@Override
	protected boolean comprobarLegalidad(Movimiento m,JSONObject jso) throws Exception{
		
		if (this.usuarioConElTurno != m.getJugador()){
			  jso.put("legalidad", "ilegal");
			  jso.put("tipoilegalidad", "No es tu turno");
			  return false;
		  } 
		  
		  if (m.getColumna() > this.tableroJugador1[0].length - 1|| m.getColumna() < 0 || m.getFila() > this.tableroJugador1.length - 1|| m.getFila() < 0){
			  jso.put("legalidad", "ilegal");
			  jso.put("tipoilegalidad", "No mueva fuera del tablero");
			  return false;
		  }
		  
		  if (this.tableroEnemigoJugador1[m.getFila()][m.getColumna()] != " " && this.usuarioConElTurno == this.usuarios[0]){
			  jso.put("legalidad", "ilegal");
			  jso.put("tipoilegalidad", "Ya ha atacado ahí");
			  return false;
		  }
		  
		  if (this.tableroEnemigoJugador2[m.getFila()][m.getColumna()] != " " && this.usuarioConElTurno == this.usuarios[1]){
			  jso.put("legalidad", "ilegal");
			  jso.put("tipoilegalidad", "Ya ha atacado ahí");
			  return false;
		  }

		  jso.put("legalidad", "legal");
		  return true;
	}

	@Override
	protected void actualizarTablero(Movimiento m, JSONObject jso) throws JSONException{
		if (this.usuarioConElTurno == this.usuarios[0]){
			mueveJugador1(m, jso);
		} else {
			mueveJugador2(m, jso);
		}
		jso.put("barcosrestantes1", Integer.toString(this.barcos1));
		jso.put("barcosrestantes2", Integer.toString(this.barcos2));
	}
	
	private void mueveJugador1(Movimiento m, JSONObject jso) throws JSONException{
		if (this.tableroJugador2[m.getFila()][m.getColumna()] == "O"){
			this.barcos2--;
			this.tableroEnemigoJugador1[m.getFila()][m.getColumna()] = "O";
			this.tableroJugador2[m.getFila()][m.getColumna()] = "X";
			System.out.println("BARCO HUNDIDO");
			
		} else {
			this.tableroEnemigoJugador1[m.getFila()][m.getColumna()] = "X";
			jso.put("tablero",tableroEnemigoJugador1);
			System.out.println("DISPARO FALLIDO");
		}
		
		jso.put("tableroatacante", this.tableroEnemigoJugador1);
		jso.put("tableroatacado", this.tableroJugador2);
	}
	
	private void mueveJugador2(Movimiento m, JSONObject jso) throws JSONException{
		if (this.tableroJugador1[m.getFila()][m.getColumna()] == "O"){
			this.barcos1--;
			this.tableroEnemigoJugador2[m.getFila()][m.getColumna()] = "O";
			this.tableroJugador1[m.getFila()][m.getColumna()] = "X";
			System.out.println("BARCO HUNDIDO");
			
		} else {
			this.tableroEnemigoJugador2[m.getFila()][m.getColumna()] = "X";
			jso.put("tablero",tableroEnemigoJugador1);
			System.out.println("DISPARO FALLIDO");
		}
		
		jso.put("tableroatacante", this.tableroEnemigoJugador2);
		jso.put("tableroatacado", this.tableroJugador1);
	}

	@Override
	public String[][] getTableroJugador1() {
		return this.tableroJugador1;
	}

	@Override
	public String[][] getTableroJugador2() {
		return this.tableroJugador2;
	}

	@Override
	protected boolean finPartida(){
		if(this.barcos1 == 0 || this.barcos2 == 0) return true;
		
		return false;
	}

	

}
