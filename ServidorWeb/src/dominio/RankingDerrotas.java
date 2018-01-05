package dominio;

import java.util.ArrayList;
import java.util.Hashtable;

import dao.DAOResultado;
import javafx.util.Pair;

public class RankingDerrotas extends TipoRanking {

	@Override
	public ArrayList<Pair<String,Integer>> obtenerRanking(String juego){
			return DAOResultado.obtenerRankingDerrotas(juego);
		}

}
