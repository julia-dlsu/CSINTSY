package solver;

import java.util.ArrayList;

public class State {

    public State(ArrayList<Coordinate> walls, ArrayList<Coordinate> crates, ArrayList<Coordinate> goals,
                Coordinate player, String path, int width, int height, Deadlock checker){
            this.walls = walls;
            this.crates = crates;
            this.goals = goals;
            this.player = player;
            successors = new ArrayList<State>();
            this.checker = checker;
            this.path = path;
            this.width = width;
            this.height = height;
            heuristic = 0;
        }
    
    // computation for heuristics
    private void manhattanDistance(){
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
        heuristic = manhattanA + manhattanB;
    }

    public int getHeuristic(){
        manhattanDistance();
        return heuristic;
    }

    public String getPath(){
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
                if (!checker.checkDeadlock(newCrate)){
                    moveCrate = true;
                }
            }
        }

        // make the move
        State next;
        if (movePlayer){
            next = new State(walls, crates, goals, newPlayer, path + direction, width, height, checker);
        }
        else if (moveCrate){
            ArrayList<Coordinate> temp = new ArrayList<Coordinate>(crates);
            temp.remove(newPlayer);
            temp.add(newCrate);
            next = new State(walls, temp, goals, newPlayer, path + direction, width, height, checker);
        }
        else{
            next = null;
        }

        // add the created state to this state's set of successors
        if (next != null){
            successors.add(next);
        }
    }

    public void populateSuccessors(){
        int tempX = player.getX();
        int tempY = player.getY();
        move('u', new Coordinate(tempX-1, tempY), new Coordinate(tempX-2, tempY));
        move('d', new Coordinate(tempX+1, tempY), new Coordinate(tempX+2, tempY));
        move('l', new Coordinate(tempX, tempY-1), new Coordinate(tempX, tempY-2));
        move('r', new Coordinate(tempX, tempY+1), new Coordinate(tempX, tempY+2));
    }

    public ArrayList<State> getSuccessors(){
        return successors;
    }

    public boolean checkGoal(){
        int n = 0;

        for(int i = 0; i < crates.size(); i++){
            if (goals.contains(crates.get(i))){
                n++;
            }
        }
        if (n == goals.size()) return true;
        return false;
    }

    public ArrayList<Coordinate> getCrates(){
        return crates;
    }

    public Coordinate getPlayer(){
        return player;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof State){
            State temp = (State) obj;
            ArrayList<Coordinate> tempCrate1 = crates;
            ArrayList<Coordinate> tempCrate2 = temp.getCrates();
            Coordinate tempPlayer = temp.getPlayer();
            
            if (tempCrate1.equals(tempCrate2) && player.equals(tempPlayer)){
                return true;
            }
        }
         return false;
    }
    
    private ArrayList<Coordinate> walls;
    private ArrayList<Coordinate> crates;
    private ArrayList<Coordinate> goals;
    private Coordinate player;
    private ArrayList<State> successors;
    private Deadlock checker;
    private String path;
    private int heuristic;
    private int width;
    private int height;
}
