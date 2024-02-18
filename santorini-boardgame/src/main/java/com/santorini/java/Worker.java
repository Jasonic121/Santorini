package com.santorini.java;

public class Worker {
    private Cell currentCell;
    private Player owner;
    private int workerId;

    // Constructor
    public Worker(Player owner, int workerId) {
        this.owner = owner;
        this.workerId = workerId;
    }

    // Initial placement of workers
    public void placeInitialWorker(Cell initialCell) {
        // List of conditions that must be met for a worker to be place
        final int boardIndexSize = 4;
        if (initialCell.getX() > boardIndexSize || initialCell.getY() > boardIndexSize) {
            throw new IllegalStateException("Cannot place a worker outside the board");
        }
        if (initialCell.isOccupied()) {
            throw new IllegalStateException("Cannot place a worker on an occupied cell");
        }
        // Place the worker on the initial cell
        initialCell.setOccupied(true);
        this.currentCell = initialCell;
    }

    // Move functionality
    public void moveWorkerToCell(Cell destination) {

        // List of conditions that must be met for a move to be valid
        if (!isAdjacent(destination)) {
            throw new IllegalStateException("Cannot move to a non-adjacent cell");
        }
        if (destination.isOccupied()) {
            throw new IllegalStateException("Cannot move to an occupied cell");
        }
        if (Math.abs(destination.getHeight() - this.currentCell.getHeight()) > 1) {
            throw new IllegalStateException("Cannot move to a cell that is more than one level higher");
        }

        // Move the worker to the destination cell
        this.currentCell.setOccupied(false);
        destination.setOccupied(true);
        this.currentCell = destination;
    }

    // Build functionality
    public void buildAt(Cell targetCell) {
        if (!isAdjacent(targetCell)) {
            throw new IllegalStateException("Cannot build on a non-adjacent cell");
        }
        if(targetCell.isOccupied()){
            throw new IllegalStateException("Cannot build on an occupied cell");
        }
        targetCell.buildBlock();
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

    /**
     * Checks the possible move possibilities for the worker.
     * 
     * @return true if there is at least one valid move possibility, false otherwise.
     */
    public boolean checkMovePossibilities() {
        final int boardLengthIndex = 4; 
        Cell targetCell;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Offset the current cell by i and j
                int targetX = this.currentCell.getX() + i;
                int targetY = this.currentCell.getY() + j;
                // Inside the board
                if (targetX >= 0 && targetX <= boardLengthIndex && targetY >= 0 && targetY <= boardLengthIndex) {
                    targetCell = new Cell(targetX, targetY);
                    if (!targetCell.isOccupied() && Math.abs(targetCell.getHeight() - this.currentCell.getHeight()) <= 1) {
                        return true;
                    }
                }
            }
        }
        return false;
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
