package solver;

import java.util.HashSet;

public class Deadlock {

    public Deadlock (HashSet<Coordinate> walls, HashSet<Coordinate> crates, HashSet<Coordinate> goals, int width, int height){
        this.walls = walls;
        this.crates = crates;
        this.goals = goals;
        this.width = width;
        this.height = height;
        boundaries = new HashSet<>();
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
        HashSet<Coordinate> whitelist = new HashSet<>();

        for (Coordinate i : goals){
            //Coordinate temp = i;
            Coordinate temp2;
            whitelist.add(i);

            // checking for the leftmost wall
            if(i.getY() - 1 == 0){
                if (walls.contains(new Coordinate(i.getX(), i.getY()-1))){
                    for (int j = 0; j < height; j++){
                        temp2 = new Coordinate(j, i.getY());
                        if(!walls.contains(temp2))  whitelist.add(temp2);
                    }
                }
            }

            //checking for the rightmost wall
            else if(i.getY() + 1 == width - 1){
                if (walls.contains(new Coordinate(i.getX(), i.getY()+1))){
                    for (int j = 0; j < height; j++){
                        temp2 = new Coordinate(j, i.getY());
                        if(!walls.contains(temp2))  whitelist.add(temp2);
                    }
                }
            }

            //checking for the uppermost wall
            if(i.getX() - 1 == 0){
                if (walls.contains(new Coordinate(i.getX()-1, i.getY()))){
                    for (int j = 0; j < width; j++){
                        temp2 = new Coordinate(i.getX(), j);
                        if(!walls.contains(temp2))  whitelist.add(temp2);
                    }
                }
            }

            //checking for the bottommost wall
            else if(i.getX() + 1 == height - 1){
                if (walls.contains(new Coordinate(i.getX()+1, i.getY()))){
                    for (int j = 0; j < width; j++){
                        temp2 = new Coordinate(i.getX(), j);
                        if(!walls.contains(temp2))  whitelist.add(temp2);
                    }
                }
            }
        }

        for(Coordinate i : walls){
            if(!whitelist.contains(i)){
                whitelist.add(i);
            }
        }

        for (int a = 0; a < height; a++){
            Coordinate left = new Coordinate(a, 1);
            Coordinate right = new Coordinate(a, width-2);
            // leftmost side
            if (!whitelist.contains(left) && !boundaries.contains(left)){
                boundaries.add(left);
            }
            // rightmost side
            if (!whitelist.contains(right) && !boundaries.contains(right)){
                boundaries.add(right);
            }
        }

        for (int a = 0; a < width; a++){
            Coordinate up = new Coordinate(1, a);
            Coordinate down = new Coordinate(height-2, a);
            // topmost side
            if (!whitelist.contains(up) && !boundaries.contains(up)){
                boundaries.add(up);
            }
            // bottommost side
            if (!whitelist.contains(down) && !boundaries.contains(down)){
                boundaries.add(down);
            }
        }

        for(Coordinate i : whitelist){
            System.out.println("Whitelist " + i + ": x = " + i.getX() + " | y = " + i.getY());
        }
        for(Coordinate i : boundaries){
            System.out.println("Boundary " + i + ": x = " + i.getX() + " | y = " + i.getY());
        }


    }

    public boolean checkBoundaries(Coordinate cratePos){
        if (boundaries.contains(cratePos)){
            return true;
        }
        return false;
    }

    private HashSet<Coordinate> boundaries;
    private HashSet<Coordinate> walls;
    private HashSet<Coordinate> crates;
    private HashSet<Coordinate> goals;
    private int width;
    private int height;
}