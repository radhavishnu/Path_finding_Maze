import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class mazegeneration extends PApplet {

//Maze Generation
//Using DFS, Recursive Backtracking
//Extra concepts we are discussing : Stack,Arrays,Recursion

int cols, rows;
int w = 40;
ArrayList<Cell> grid = new ArrayList<Cell>();

Cell current;

ArrayList<Cell> stack = new ArrayList<Cell>();

public void setup() {
  
  cols = floor(width/w);
  rows = floor(height/w);
  //frameRate(5);

  for (int j = 0; j < rows; j++) {
    for (int i = 0; i < cols; i++) {
      Cell cell = new Cell(i, j);
      grid.add(cell);
    }
  }

  current = grid.get(0);

}

public void draw() {
  background(51);
  for (int i = 0; i < grid.size(); i++) {
    grid.get(i).show();
  }

  current.visited = true;
  current.highlight();
  
  // STEP 1
  Cell next = current.checkNeighbors();
  if (next != null) {
    next.visited = true;

    // STEP 2
    stack.add(current);

    // STEP 3
    removeWalls(current, next);

    // STEP 4
    current = next;
  } else if (stack.size() > 0) {
    current = stack.remove(stack.size()-1);
  }
}

public int index(int i, int j) {
  if (i < 0 || j < 0 || i > cols-1 || j > rows-1) {
    return 0;
  }
  return i + j * cols;
}



public void removeWalls(Cell a, Cell b) {
  int x = a.i - b.i;
  if (x == 1) {
    a.walls[3] = false;
    b.walls[1] = false;
  } else if (x == -1) {
    a.walls[1] = false;
    b.walls[3] = false;
  }
  int y = a.j - b.j;
  if (y == 1) {
    a.walls[0] = false;
    b.walls[2] = false;
  } else if (y == -1) {
    a.walls[2] = false;
    b.walls[0] = false;
  }
}

class Cell {
  int i, j;
  boolean[] walls = {true, true, true, true};
  boolean visited = false;
  
  Cell(int ii, int jj) {
    i = ii;
    j = jj;
  }

  public Cell checkNeighbors() {
    ArrayList<Cell> neighbors = new ArrayList<Cell>();

    Cell top    = grid.get(index(i, j-1));
    Cell right  = grid.get(index(i+1, j));
    Cell bottom = grid.get(index(i, j+1));
    Cell left   = grid.get(index(i-1, j));

    if (top != null && !top.visited) {
      neighbors.add(top);
    }
    if (right != null && !right.visited) {
      neighbors.add(right);
    }
    if (bottom != null && !bottom.visited) {
      neighbors.add(bottom);
    }
    if (left != null && !left.visited) {
      neighbors.add(left);
    }

    if (neighbors.size() > 0) {
      int r = floor(random(0, neighbors.size()));
      return neighbors.get(r);
    } else {
      return null;
    }
  }
  
  public void highlight() {
    int x = this.i*w;
    int y = this.j*w;
    noStroke();
    fill(0, 0, 255, 100);
    rect(x, y, w, w);

  }

  public void show() {
    int x = this.i*w;
    int y = this.j*w;
    stroke(255);
    if (this.walls[0]) {
      line(x    , y    , x + w, y);
    }
    if (this.walls[1]) {
      line(x + w, y    , x + w, y + w);
    }
    if (this.walls[2]) {
      line(x + w, y + w, x    , y + w);
    }
    if (this.walls[3]) {
      line(x    , y + w, x    , y);
    }

    if (this.visited) {
      noStroke();
      fill(255, 0, 255, 100);
      rect(x, y, w, w);
    }
  }
}
  public void settings() {  size(400, 400); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "mazegeneration" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
