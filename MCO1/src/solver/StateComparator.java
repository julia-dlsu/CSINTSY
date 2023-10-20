package solver;

import java.util.Comparator;

public class StateComparator implements Comparator<State> {
    @Override
    public int compare(State s1, State s2){
        int cost1 = s1.getPath().length();
        int cost2 = s2.getPath().length();

        return Integer.compare(cost1 + s1.getHeuristic(), cost2 + s2.getHeuristic());
    }
}