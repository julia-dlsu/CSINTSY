package solver;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.*;

public class SokoBot {

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {

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
      HashSet<State> visited = new HashSet<State>();

      // A STAR LOOP
      // keep looping until queue is empty
      while (true){
        State currState = states.poll(); // get a state
        visited.add(currState); // set as visited

        if (!currState.checkGoal()){
          currState.populateSuccessors();
          for (State i : currState.getSuccessors()){
            if (!visited.contains(i)){
              states.add(i); // get curr state's successors
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
    return "durl";
  }

  private void setMapCoordinates(int width, int height, char[][] mapData){
    walls = new HashSet<>();
    goals = new HashSet<>();

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
    crates = new HashSet<>();

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

  private HashSet<Coordinate> walls;
  private HashSet<Coordinate> crates;
  private HashSet<Coordinate> goals;
  private Coordinate player;

}