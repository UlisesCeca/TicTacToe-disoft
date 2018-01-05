package dominio;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.util.Pair;

public class Ranking {
	
	private TipoRanking ranking;
	private static Ranking yo;
	
	public Ranking(){
		this.ranking = new RankingVictorias();
		
	}
	
	public static Ranking get(){
		if (yo==null) {
			synchronized (Ranking.class) {
				if (yo == null)
					yo = new Ranking();				
			}
		}
		return yo;
	}
	
	
	public JSONObject obtenerRanking(String juego, String jugador) throws JSONException{
		return construirJSON(ranking.obtenerRanking(juego), jugador);
	}
	
	public JSONObject obtenerRankingVictorias(String juego, String jugador) throws JSONException{
		this.ranking = new RankingVictorias();	
		return obtenerRanking(juego, jugador);
	}
	
	public JSONObject obtenerRankingDerrotas(String juego, String jugador) throws JSONException{
		this.ranking = new RankingDerrotas();	
		return obtenerRanking(juego, jugador);
	}
	
	public JSONObject obtenerRankingPartidas(String juego, String jugador) throws JSONException{
		this.ranking = new RankingPartidas();	
		return obtenerRanking(juego, jugador);
	}
	
	public JSONObject construirJSON(ArrayList<Pair<String, Integer>> ranking, String jugador) throws JSONException{
		String usuario;
		int cantidad;
		JSONObject json = new JSONObject();
		JSONObject aux;
		JSONArray jsona = new JSONArray();
			
		for(int i = 0; i < ranking.size(); i++){
			if (ranking.get(i).getKey().equalsIgnoreCase(jugador)) json.put("tuposicion", i + 1);
			aux = new JSONObject();
			aux.put("usuario", ranking.get(i).getKey());
			aux.put("cantidad", ranking.get(i).getValue());
			jsona.put(aux);
		}
			
		json.put("ranking", jsona);
		
		return json;
	}
}
