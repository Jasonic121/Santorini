package com.santorini;

/**
 * Represents a worker in the Santorini board game.
 */
public class Worker {
    private Cell currentCell;
    private Player owner;
    private int workerId;

    /**
     * Constructs a new Worker object with the specified owner and worker ID.
     *
     * @param owner The player who owns the worker.
     * @param workerId The ID of the worker.
     */
    public Worker(Player owner, int workerId) {
        this.owner = owner;
        this.workerId = workerId;
        this.currentCell = null;
    }

    /**
     * Places the worker on the initial cell.
     *
     * @param initialCell The initial cell where the worker is placed.
     * @throws IllegalStateException if the initial cell is outside the board or already occupied.
     */
    public void placeInitialWorker(Cell initialCell) {
        // List of conditions that must be met for a worker to be placed
        final int boardIndexSize = 4;
        if (initialCell.getX() > boardIndexSize || initialCell.getY() > boardIndexSize) {
            throw new IllegalStateException("Cannot place a worker outside the board");
        }
        if (initialCell.isOccupied()) {
            throw new IllegalStateException("Cannot place a worker on an occupied cell");
        }
        // Place the worker on the initial cell
        initialCell.setOccupied(true);
        initialCell.setOccupiedBy(owner.getPlayerId());
        currentCell = initialCell;
        System.out.println("Current cell: " + currentCell.getX() + ", " + currentCell.getY());
    }

    /**
     * Moves the worker to the specified destination cell.
     *
     * @param destination The destination cell where the worker is moved to.
     * @throws IllegalStateException if the destination cell is not adjacent, already occupied, or more than one level higher.
     */
    public void moveWorkerToCell(Cell destination) {
        if (validateMove(destination)) {
            // Move the worker to the destination cell
            this.currentCell.setOccupied(false);
            destination.setOccupied(true);
            destination.setOccupiedBy(owner.getPlayerId());
            this.currentCell = destination;
        }
    }

    /**
     * Validates a move to the specified destination cell.
     *
     * @param destination The destination cell to move to.
     * @throws IllegalStateException if the move is not valid.
     */
    private boolean validateMove(Cell destination) {
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
        if (destination.hasDome()) {
            throw new IllegalStateException("Cannot move to a cell that has a dome");
        }
        else return true;
    }

    /**
     * Builds a block at the specified target cell.
     *
     * @param targetCell The target cell where the block is built.
     * @throws IllegalStateException if the target cell is not adjacent or already occupied.
     */
    public void buildAt(Cell targetCell) {
        if(validateBuild(targetCell)) {
            targetCell.buildBlock();
        }
    }

    /**
     * Validates a build at the specified target cell.
     *
     * @param targetCell The target cell to build at.
     * @throws IllegalStateException if the build is not valid.
     */
    private boolean validateBuild(Cell targetCell) {
        if (!isAdjacent(targetCell)) {
            throw new IllegalStateException("Cannot build on a non-adjacent cell");
        }
        if (targetCell.isOccupied()) {
            throw new IllegalStateException("Cannot build on an occupied cell");
        }
        if (targetCell.hasDome()) {
            throw new IllegalStateException("Cannot build on a cell that has a dome");
        }
        else return true;
    }

    /**
     * Checks if a cell is adjacent to the current cell.
     *
     * @param targetCell The cell to check for adjacency.
     * @return true if the cell is adjacent to the current cell, false otherwise.
     */
    private boolean isAdjacent(Cell targetCell) {
        if (targetCell == null) {
            return false;
        }


        int currentX = this.currentCell.getX(); 
        int currentY = this.currentCell.getY(); 
        int targetX = targetCell.getX();
        int targetY = targetCell.getY();

        return Math.abs(currentX - targetX) <= 1 && Math.abs(currentY - targetY) <= 1
                && !(currentX == targetX && currentY == targetY);
    }

    /**
     * Gets the current cell of the worker.
     *
     * @return The current cell of the worker.
     */
    public Cell getCurrentCell() {
        return currentCell;
    }

    /**
     * Gets the owner of the worker.
     *
     * @return The owner of the worker.>
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Returns the ID of the worker.
     *
     * @return the ID of the worker
     */
    public int getWorkerId() {
        return workerId;
    }
}
