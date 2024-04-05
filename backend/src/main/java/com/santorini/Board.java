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
     * Validates the adjacent cells for moving from the given worker cell.
     * 
     * @param workerCell the cell of the worker
     * @return an array of valid adjacent cells for moving
     */
    public Cell[] validateCellsForMoving(Cell workerCell) {
        int x = workerCell.getX();
        int y = workerCell.getY();
        ArrayList<Cell> validCells = new ArrayList<Cell>();
        for (int i = x - 1 ; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i < 0 || i >= gridLength || j < 0 || j >= gridLength) {
                    continue;
                }
                if (isAdjacent(x, y, i, j) && !isOccupied(i, j) && !hasDome(x, y) && heightDifference(workerCell, grid.get(i).get(j))) {
                    validCells.add(grid.get(i).get(j));
                }
            }
        }
        return validCells.toArray(new Cell[validCells.size()]);
    }

    /**
     * Validates the adjacent cells for building from the given worker cell.
     * 
     * @param workerCell the cell of the worker
     * @return an array of valid adjacent cells for building
     */
    public Cell[] validateCellsForBuilding(Cell workerCell) {
        int x = workerCell.getX();
        int y = workerCell.getY();
        ArrayList<Cell> validCells = new ArrayList<Cell>();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i < 0 || i >= gridLength || j < 0 || j >= gridLength) {
                    continue;
                }
                if (isAdjacent(x, y, i, j) && !isOccupied(i, j) && !hasDome(x, y)) {
                    validCells.add(grid.get(i).get(j));
                }
            }
        }
        return validCells.toArray(new Cell[validCells.size()]);
    }

    /**
     * Checks if the height difference between two cells is at most 1.
     *
     * @param workerCell The cell where the worker is currently located.
     * @param targetCell The cell where the worker wants to move.
     * @return true if the height difference is at most 1, false otherwise.
     */
    private boolean heightDifference(Cell workerCell, Cell targetCell) {
        return Math.abs(workerCell.getHeight() - targetCell.getHeight()) <= 1;
    }

    /**
     * Checks if a specific cell on the board has a dome.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return true if the cell has a dome, false otherwise
     */
    private boolean hasDome(int x, int y) {
        return grid.get(x).get(y).hasDome();
    }

    /**
     * Checks if two positions on the board are adjacent to each other.
     *
     * @param x1 the x-coordinate of the first position
     * @param y1 the y-coordinate of the first position
     * @param x2 the x-coordinate of the second position
     * @param y2 the y-coordinate of the second position
     * @return true if the positions are adjacent, false otherwise
     */
    private boolean isAdjacent(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1;
    }

    /**
     * Checks if a specific position on the board is occupied by a player.
     *
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @return true if the position is occupied, false otherwise.
     */
    private boolean isOccupied(int x, int y) {
        return grid.get(x).get(y).isOccupied();
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