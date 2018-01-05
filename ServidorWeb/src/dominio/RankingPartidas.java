package dominio;

import java.util.ArrayList;
import java.util.Hashtable;

import dao.DAOResultado;
import javafx.util.Pair;

public class RankingPartidas extends TipoRanking{

	@Override
	public ArrayList<Pair<String, Integer>> obtenerRanking(String juego) {
		return DAOResultado.obtenerRankingPartidas(juego);
	}
}
