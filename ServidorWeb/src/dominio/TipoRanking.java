package dominio;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.util.Pair;

public abstract class TipoRanking {

	public abstract ArrayList<Pair<String,Integer>> obtenerRanking(String juego);
}
