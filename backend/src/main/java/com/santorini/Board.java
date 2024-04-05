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
        return validateAdjacentCells(workerCell, false);
    }

    /**
     * Validates the adjacent cells for building from the given worker cell.
     *
     * @param workerCell the cell of the worker
     * @return an array of valid adjacent cells for building
     */
    public Cell[] validateCellsForBuilding(Cell workerCell) {
        return validateAdjacentCells(workerCell, true);
    }

    /**
     * Validates the adjacent cells for moving or building from the given worker cell.
     *
     * @param workerCell the cell of the worker
     * @param isBuilding true if validating for building, false for moving
     * @return an array of valid adjacent cells
     */
    private Cell[] validateAdjacentCells(Cell workerCell, boolean isBuilding) {
        int x = workerCell.getX();
        int y = workerCell.getY();
        ArrayList<Cell> validCells = new ArrayList<Cell>();

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i < 0 || i >= gridLength || j < 0 || j >= gridLength) {
                    continue;
                }
                Cell cell = grid.get(i).get(j);
                System.out.println("Checking worker height: " + workerCell.getHeight() + " and cell height: " + cell.getHeight());
                if (isAdjacent(x, y, i, j) && !cell.isOccupied() && !cell.hasDome()) {
                    if(isBuilding) { // building validation
                        validCells.add(cell);
                    } else { // moving validation
                        // if (cell.getHeight() - workerCell.getHeight() <= 1 && cell.getHeight() <= 3) {
                        //     System.out.println("Valid moving cell: " + cell.toString());
                        //     validCells.add(cell);
                        // }
                        if (cell.getHeight() > workerCell.getHeight()) { //moving up
                            if (cell.getHeight() - workerCell.getHeight() <= 1 && cell.getHeight() <= 3) {
                                System.out.println("Valid moving cell: " + cell.toString());
                                validCells.add(cell);
                            }
                        } else { // moving down
                            validCells.add(cell);
                        }
                    }
                }
            }
        }

        return validCells.toArray(new Cell[validCells.size()]);
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
     * Getter Methods
     * --------------
     */

    /**
     * Returns the grid of cells on the board.
     *
     * @return the grid of cells
     */
    public Cell[] getGrid() {
        ArrayList<Cell> flattenedGrid = new ArrayList<Cell>();
        for (ArrayList<Cell> row : grid) {
            flattenedGrid.addAll(row);
        }
        return flattenedGrid.toArray(new Cell[flattenedGrid.size()]);
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
        for (ArrayList<Cell> row : grid) {
            for (Cell cell : row) {
                sb.append(cell.toString());
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}