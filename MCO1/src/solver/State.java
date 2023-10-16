package solver;

import java.util.ArrayList;

public class State {

    public State(ArrayList<Coordinate> walls, ArrayList<Coordinate> crates, ArrayList<Coordinate> goals,
                Coordinate player, StringBuilder path, int width, int height){
            this.walls = walls;
            this.crates = crates;
            this.goals = goals;
            this.player = player;
            successors = new ArrayList<>();
            this.path = path;
            this.width = width;
            this.height = height;
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

    public int getHeuristic(){
        return heuristic;
    }

    public StringBuilder getPath(){
        return path;
    }

    public void move(char direction, Coordinate newPlayer, Coordinate newCrate){
        // checks if move is within bounds
        if (newPlayer.getX() <= 1 && newPlayer.getY() <= 1 && newPlayer.getX() >= width && newPlayer.getY() >= height){
            return;
        }
        if (newCrate.getX() <= 1 && newCrate.getY() <= 1 && newCrate.getX() >= width && newCrate.getY() >= height){
            return;
        }
        
        boolean isWall = true;
        boolean isWall2 = true;
        boolean isCrate = false;
        boolean isCrate2 = true;
        boolean movePlayer = false;
        boolean moveCrate = false;

        // check if player is not trying to walk into a wall
        if (!walls.contains(newPlayer)){
            isWall = false;
        }
        
        // check if crate
        if (crates.contains(newPlayer)){
            isCrate = true;
        }

        // move player to a space
        if (!isWall && !isCrate){
            movePlayer = true;
        }

        // check if crate destination is not a wall
        if (!walls.contains(newCrate)){
            isWall2 = false;
        }

        // check if crate destination is not another crate
        if (!crates.contains(newCrate)){
            isCrate2 = false;
        }

        // move crate to space
        if (!isWall && isCrate){
            if (!isWall2 && !isCrate2){
                moveCrate = true;
            }
        }

        // make the move
        State next;
        if (movePlayer){
            next = new State(walls, crates, goals, newPlayer, path.append(direction), width, height);
        }
        else if (moveCrate){
            ArrayList<Coordinate> temp = new ArrayList<>(crates);
            temp.remove(newPlayer);
            temp.add(newCrate);
            next = new State(walls, temp, goals, newPlayer, path.append(direction), width, height);
        }
        else{
            next = null;
        }

        // add the created state to this state's set of successors
        if (next != null){
            successors.add(next);
        }

    }
    
    private ArrayList<Coordinate> walls;
    private ArrayList<Coordinate> crates;
    private ArrayList<Coordinate> goals;
    private Coordinate player;
    private ArrayList<State> successors;
    private StringBuilder path;
    private int heuristic;
    private int width;
    private int height;
}
