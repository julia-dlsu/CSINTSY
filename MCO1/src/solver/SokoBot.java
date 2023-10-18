package solver;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class SokoBot {

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    
    // for (int h = 0; h < height; h++){
    //   for (int w = 0; w < width; w++){
    //     System.out.print(mapData[h][w]);
    //   }
    //   System.out.println();
    // }

    setMapCoordinates(width, height, mapData);
    setItemCoordinates(width, height, itemsData);

    // A STAR IMPLEMENTATION
    try {
      // sort states by heuristic + cost (length of moves)
      StateComparator comparator = new StateComparator();
      PriorityQueue<State> states = new PriorityQueue<State>(comparator);
      Deadlock checker = new Deadlock(walls, crates, goals, width, height);
      checker.populateBoundaries();
      State intial = new State(walls, crates, goals, player, "", width, height, checker);
      states.add(intial);

      // keep track of visited states from the queue
      ArrayList<State> visited = new ArrayList<State>();

      // A STAR LOOP
      // keep looping until queue is empty
      while (true){
        State currState = states.poll(); // get a state
        visited.add(currState); // set as visited

        if (!currState.checkGoal()){
          currState.populateSuccessors();
          for (int i = 0;  i < currState.getSuccessors().size(); i++){
            if (!visited.contains(currState.getSuccessors().get(i))){
              states.add(currState.getSuccessors().get(i)); // get curr state's successors
            }
          }
        }
        else {
          System.out.println(currState.getPath());
          return currState.getPath();
        }
      }

    } catch (NullPointerException e) {
      System.out.println("UNSOLVABLE");
      e.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return "";
  }

  private void setMapCoordinates(int width, int height, char[][] mapData){
    walls = new ArrayList<Coordinate>();
    goals = new ArrayList<Coordinate>();

    for (int h = 0; h < height; h++){
      for (int w = 0; w < width; w++){
        // WALLS
        if (mapData[h][w] == '#'){
          Coordinate tempWall = new Coordinate(h, w);
          this.walls.add(tempWall);
        }
        // GOALS
        else if (mapData[h][w] == '.'){
          Coordinate tempGoal = new Coordinate(h, w);
          this.goals.add(tempGoal);
        }
      }
    }
  }

  private void setItemCoordinates(int width, int height, char[][] itemsData){
    crates = new ArrayList<Coordinate>();

    for (int h = 0; h < height; h++){
      for (int w = 0; w < width; w++){
        // PLAYER
        if (itemsData[h][w] == '@'){
          this.player = new Coordinate(h, w);
        }
        // CRATES
        else if (itemsData[h][w] == '$'){
          this.crates.add(new Coordinate(h, w));
        }
      }
    }
  }

  private ArrayList<Coordinate> walls;
  private ArrayList<Coordinate> crates;
  private ArrayList<Coordinate> goals;
  private Coordinate player;

}
