package com.santorini.java;
import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<Cell>> grid;
    private final int gridLength = 5;

    // Constructor
    public Board() {
        this.grid = new ArrayList<ArrayList<Cell>>();
        for (int i = 0; i < gridLength; i++) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < gridLength; j++) {
                row.add(new Cell(i, j));
            }
            grid.add(row);
        }
    }



    /* 
    Getters for the grid
    */
    // Getter for grid
    public ArrayList<ArrayList<Cell>> getGrid() {
        return grid;
    }

    // Getter for cell
    public Cell getCell(int x, int y) {
        return grid.get(x).get(y);
    }
}