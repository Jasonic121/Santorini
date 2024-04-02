package com.santorini;
import java.util.ArrayList;

/**
 * Represents the game board for the Santorini game.
 */
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

    /**
     * Getter Methods
     * --------------
     */

    /**
     * Returns the grid of cells on the board.
     * 
     * @return the grid of cells
     */
    public Cell[] getGrid() {
        int rows = grid.size();
        int cols = grid.get(0).size();
    
        // Create a one-dimensional array to store the cells
        Cell[] flattenedGrid = new Cell[rows * cols];
    
        // Flatten the two-dimensional grid into the one-dimensional array
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flattenedGrid[index] = grid.get(i).get(j);
                index++;
            }
        }
        return flattenedGrid;
    }

    /**
     * Returns the cell at the specified coordinates on the board.
     * 
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return the cell at the specified coordinates
     */
    public Cell getCell(int x, int y) {
        return grid.get(x).get(y);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                sb.append(grid.get(i).get(j).toString());
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();

    }
}