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
    public ArrayList<ArrayList<Cell>> getGrid() {
        return grid;
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