package solver;

public class Coordinate {

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    // getter for x attribute
    public int getX(){
        return x;
    }

    // getter for y attribute
    public int getY(){
        return y;
    }

    // setter for x attribute
    public void setX(int x){
        this.x = x;
    }

    // setter for y attribute
    public void setY(int y){
        this.y = y;
    }

    @Override
    public boolean equals(Object coord){
        if (coord instanceof Coordinate){
            Coordinate c = (Coordinate) coord;
            if (c.getX() == this.x && c.getY() == this.y){
                return true;
            }
        }
        return false;
    }

    // computes the hashcode of the coordinate based on its x and y attributes
    @Override
    public int hashCode() {
        return x + y * 37;
    }

    private int x;
    private int y;

}