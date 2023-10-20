package solver;

import java.util.Comparator;

public class StateComparator implements Comparator<State> {
    @Override
    public int compare(State s1, State s2){
        return Integer.compare(s1.getPath().length() + s1.getHeuristic(), s2.getPath().length() + s2.getHeuristic());
    }
}