package com.santorini.java;
import java.util.ArrayList;

public class Worker {
    private Cell currentCell;
    private Player owner;
    private int workerId;

    // Constructor
    public Worker(Player owner, int workerId) {
        this.owner = owner;
        this.workerId = workerId;
    }

    public void moveWorkerToCell(Cell destination) {
        if (destination.isOccupied()) {
            throw new IllegalStateException("Cannot move to an occupied cell");
        }

        if (Math.abs(destination.getHeight() - this.currentCell.getHeight()) > 1) {
            throw new IllegalStateException("Cannot move to a cell that is more than one level higher");
        }

        this.currentCell.setOccupied(false);
        destination.setOccupied(true);
        this.currentCell = destination;
    }

    public void buildAt(Cell targetCell) {
        // Implement logic to build at the target cell.
        // This may involve checking if the cell is adjacent, not occupied, etc.
    }

    // Utility method for checking if a cell is adjacent to the current cell
    private boolean isAdjacent(Cell targetCell) {
        int currentX = this.currentCell.getX(); 
        int currentY = this.currentCell.getY(); 
        int targetX = targetCell.getX();
        int targetY = targetCell.getY();

        return Math.abs(currentX - targetX) <= 1 && Math.abs(currentY - targetY) <= 1
                && !(currentX == targetX && currentY == targetY);
    }

    /* 
    Getters for Worker properties
    */
    public Cell getCurrentCell() {
        return currentCell;
    }

    public Player getOwner() {
        return owner;
    }

    public int getWorkerId() {
        return workerId;
    }
}
