package solver;

import java.util.ArrayList;

public class State {

    public State(ArrayList<Coordinate> walls, ArrayList<Coordinate> crates, ArrayList<Coordinate> goals,
                Coordinate player, StringBuilder path){
            this.walls = walls;
            this.crates = crates;
            this.goals = goals;
            this.player = player;
            successors = new ArrayList<>();
            this.path = path;
            heuristic = 0;
        }
    
    // computation for heuristics
    private int manhattanDistance(){
        int x = player.getX();
        int y = player.getY();
        int manhattanA = 0; //player to crate
        int manhattanB = 0; // crate to goal

        for (int i = 0; i < crates.size(); i++){
            Coordinate tempCrate = crates.get(i);
            manhattanA += Math.abs(x - tempCrate.getX()) + Math.abs(y - tempCrate.getY());
        }

        for (int i = 0; i < crates.size(); i++){
            Coordinate tempCrate = crates.get(i);
            
            for (int j = 0; j < goals.size(); j++){
                Coordinate tempGoal = goals.get(j);
                manhattanB += Math.abs(tempCrate.getX() - tempGoal.getX()) + Math.abs(tempCrate.getY() - tempGoal.getY());
            }
        }
        return manhattanA + manhattanB;
    }
    
    private ArrayList<Coordinate> walls;
    private ArrayList<Coordinate> crates;
    private ArrayList<Coordinate> goals;
    private Coordinate player;
    private ArrayList<State> successors;
    private StringBuilder path;
    private int heuristic;
}
