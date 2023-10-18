package solver;

import java.util.ArrayList;

public class Deadlock {

    public Deadlock (ArrayList<Coordinate> walls, ArrayList<Coordinate> crates, ArrayList<Coordinate> goals, int width, int height){
        this.walls = walls;
        this.crates = crates;
        this.goals = goals;
        this.width = width;
        this.height = height;
        boundaries = new ArrayList<Coordinate>();
    }
    
    public boolean checkDeadlock(Coordinate cratePos){
        Coordinate left = new Coordinate(cratePos.getX(), cratePos.getY() - 1);
        Coordinate right = new Coordinate(cratePos.getX(), cratePos.getY() + 1);
        Coordinate up = new Coordinate(cratePos.getX() - 1, cratePos.getY());
        Coordinate down = new Coordinate(cratePos.getX() + 1, cratePos.getY());
        boolean corner = false;

        // CORNER DEADLOCK //
        if (walls.contains(left) || walls.contains(right)){
            if (walls.contains(up) || walls.contains(down)){
                corner = true;
            }
        }

        if (corner){
            if (!goals.contains(cratePos)){
                return true; // corner deadlock
            }
        }

        // BOUNDARY DEADLOCK //
        if (checkBoundaries(cratePos)){
            return true;
        }

        return false; // not deadlock
    }

    public void populateBoundaries(){
        ArrayList<Coordinate> whitelist = new ArrayList<Coordinate>();

        for (int i = 0; i <goals.size(); i++){
            Coordinate temp = goals.get(i);
            
            // left wall
            if (walls.contains(new Coordinate(temp.getX(), temp.getY()-1))){
                for (int j = 0; j < height; j++){
                    whitelist.add(new Coordinate(j, temp.getY()-1));
                }
            }
            // right wall
            else if (walls.contains(new Coordinate(temp.getX(), temp.getY()+1))){
                for (int j = 0; j < height; j++){
                    whitelist.add(new Coordinate(j, temp.getY()+1));
                }
            }
            
            // up wall
            if (walls.contains(new Coordinate(temp.getX()-1, temp.getY()))){
                for (int j = 0; j < width; j++){
                    whitelist.add(new Coordinate(temp.getX()-1, j));
                }
            }
            // down wall
            else if (walls.contains(new Coordinate(temp.getX()+1, temp.getY()))){
                for (int j = 0; j < width; j++){
                    whitelist.add(new Coordinate(temp.getX()+1, j));
                }
            }
        }

        for (int a = 0; a < height; a++){
            Coordinate left = new Coordinate(a, 1);
            Coordinate right = new Coordinate(a, width-2);
            // leftmost side
            if (!whitelist.contains(left)){
                boundaries.add(left);
            }
            // rightmost side
            if (!whitelist.contains(right)){
                boundaries.add(right);
            }
        }

        for (int a = 0; a < width; a++){
            Coordinate up = new Coordinate(1, a);
            Coordinate down = new Coordinate(height-2, a);
            // topmost side
            if (!whitelist.contains(up)){
                boundaries.add(up);
            }
            // bottommost side
            if (!whitelist.contains(down)){
                boundaries.add(down);
            }
        }
    }

    public boolean checkBoundaries(Coordinate cratePos){
        if (boundaries.contains(cratePos)){
            return true;
        }
        return false;
    }

    private ArrayList<Coordinate> boundaries;
    private ArrayList<Coordinate> walls;
    private ArrayList<Coordinate> crates;
    private ArrayList<Coordinate> goals;
    private int width;
    private int height;
}
