package solver;

import java.util.HashSet;

public class State {

    public State(HashSet<Coordinate> walls, HashSet<Coordinate> crates, HashSet<Coordinate> goals,
                Coordinate player, String path, int width, int height, Deadlock checker){
            this.walls = walls;
            this.crates = crates;
            this.goals = goals;
            this.player = player;
            successors = new HashSet<>();
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
        this.heuristic = 0;
        HashSet<Coordinate> cratesLeft = new HashSet<>(crates);
        HashSet<Coordinate> goalsLeft = new HashSet<>(goals);

        // so that heuristics will no longer consider crates and goal that are completed
        for(Coordinate i : crates){
            if(goals.contains(i)){
                goalsLeft.remove(i);
                cratesLeft.remove(i);
            }
        }

        // player to crate manhattan distance
        for (Coordinate i : cratesLeft){
            if(!goalsLeft.contains(i)) {
                this.heuristic += Math.abs(x - i.getX()) + Math.abs(y - i.getY());
            }
        }

        // crate to goal manhattan distance
        for (Coordinate i : cratesLeft){
            if(!goalsLeft.contains(i)){
                for (Coordinate j : goalsLeft){
                    this.heuristic += Math.abs(i.getX() - j.getX()) + Math.abs(j.getY() - i.getY());
                }
            }
        }
    }

    // getter for heuristic attribute
    public int getHeuristic(){
        manhattanDistance();
        return heuristic;
    }

    // getter for path attribute
    public String getPath(){
        return path;
    }

    /* checks for the validity of a move (state transition) and 
    makes the state transition according to validity, heuristics, & deadlocks*/
    public void move(char direction, Coordinate newPlayer, Coordinate newCrate){
        // checks if move is within bounds
        if (newPlayer.getX() <= 1 && newPlayer.getY() <= 1 && newPlayer.getX() >= width && newPlayer.getY() >= height)
            return;
        
        if (newCrate.getX() <= 1 && newCrate.getY() <= 1 && newCrate.getX() >= width && newCrate.getY() >= height)
            return;

        boolean isWall = true;
        boolean isWall2 = true;
        boolean isCrate = false;
        boolean isCrate2 = true;
        boolean movePlayer = false;
        boolean moveCrate = false;

        // check if player is not trying to walk into a wall
        if (!walls.contains(newPlayer))
            isWall = false;

        // check if crate
        if (crates.contains(newPlayer))
            isCrate = true;

        // move player to a space
        if (!isWall && !isCrate)
            movePlayer = true;

        // check if crate destination is not a wall
        if (!walls.contains(newCrate))
            isWall2 = false;

        // check if crate destination is not another crate
        if (!crates.contains(newCrate))
            isCrate2 = false;

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
            HashSet<Coordinate> temp = new HashSet<>(crates);
            temp.remove(newPlayer);
            temp.add(newCrate);
            next = new State(walls, temp, goals, newPlayer, path + direction, width, height, checker);
        }
        else{
            next = null;
        }

        // add the created state to this state's set of successors
        if (next != null)
            successors.add(next);
    }

    // initializes the successor (u,d,l,r) states of this object state
    public void populateSuccessors(){
        int tempX = player.getX();
        int tempY = player.getY();
        move('u', new Coordinate(tempX-1, tempY), new Coordinate(tempX-2, tempY));
        move('d', new Coordinate(tempX+1, tempY), new Coordinate(tempX+2, tempY));
        move('l', new Coordinate(tempX, tempY-1), new Coordinate(tempX, tempY-2));
        move('r', new Coordinate(tempX, tempY+1), new Coordinate(tempX, tempY+2));
    }

    // getter for succussors attributes
    public HashSet<State> getSuccessors(){
        return successors;
    }

    // checks if all goal coordinates have a crate
    public boolean checkGoal(){
        int n = 0;

        for(Coordinate i : crates){
            if (goals.contains(i))
                n++;
        }
        if (n == goals.size()) return true;
        return false;
    }

    // getter for crates attribute
    public HashSet<Coordinate> getCrates(){
        return crates;
    }

    // getter for player attribute
    public Coordinate getPlayer(){
        return player;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true; // reference equality

        if (obj instanceof State){
            State temp = (State) obj;
            if (crates.equals(temp.crates) && player.equals(temp.getPlayer())) return true;
        }
        return false;
    }

    /*  computes the hash of the State object based on the:
        hashcode of its crates and location of its player */    
    @Override
    public int hashCode() {
        int Hash = 0;
        for (Coordinate x : crates) {
            Hash += 19 * x.hashCode();
            //Hash *= 19;
        }
        return player.getX() * Hash +  player.getY() * 97;
    }

    private HashSet<Coordinate> walls;
    private HashSet<Coordinate> crates;
    private HashSet<Coordinate> goals;
    private Coordinate player;
    private HashSet<State> successors;
    private Deadlock checker;
    private String path;
    private int heuristic;
    private int width;
    private int height;
}