package auxiliar;

import java.util.Comparator;

import javafx.util.Pair;

public class PairComparator implements Comparator<Pair<String, Integer>> {
	
    public int compare(Pair<String, Integer> a, Pair<String, Integer> b) {
        return b.getValue().compareTo(a.getValue());    
    }
    
}
