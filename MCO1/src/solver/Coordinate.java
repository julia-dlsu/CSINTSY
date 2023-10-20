package solver;

public class 
Coordinate {
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

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

    @Override
    public int hashCode() {
        return x + y * 7;
    }

    private int x;
    private int y;

}