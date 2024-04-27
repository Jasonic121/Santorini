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
    }

    /**
     * Places the worker on the initial cell.
     *
     * @param initialCell The initial cell where the worker is placed.
     */
    public void placeInitialWorker(Cell initialCell) {
        if (initialCell.isOccupied()) {
            throw new IllegalStateException("Cannot place a worker on an occupied cell");
        }
        initialCell.setWorker(this);
        currentCell = initialCell;
    }

    /**
     * Moves the worker to the specified destination cell.
     *
     * @param destination The destination cell where the worker is moved to.
     */
    public void moveWorkerToCell(Cell destination) {
        if (validateMove(destination)) {
            currentCell.removeWorker();
            destination.setWorker(this);
            currentCell = destination;
        }
    }

    /**
     * Validates a move to the specified destination cell.
     *
     * @param destination The destination cell to move to.
     */
    private boolean validateMove(Cell destination) {
        if (destination.isOccupied()) {
            throw new IllegalStateException("Cannot move to an occupied cell");
        }
        if (destination.getHeight() > currentCell.getHeight()) { // Moving up
            if (destination.getHeight() - currentCell.getHeight() > 1) {
                throw new IllegalStateException("Cannot move to a cell that is more than one level higher");
            }
        }
        if (destination.hasDome()) {
            throw new IllegalStateException("Cannot move to a cell that has a dome");
        }
        return true;
    }

    /**
     * Builds a block at the specified target cell.
     *
     * @param targetCell The target cell where the block is built.
     */
    public void buildAt(Cell targetCell) {
        if (validateBuild(targetCell)) {
            targetCell.buildBlock();
        }
    }

    /**
     * Validates a build at the specified target cell.
     *
     * @param targetCell The target cell to build at.
     * @return true if the build is valid, false otherwise.
     */
    public boolean validateBuild(Cell targetCell) {
        if (targetCell.isOccupied()) {
            throw new IllegalStateException("Cannot build on an occupied cell");
        }
        if (targetCell.hasDome()) {
            throw new IllegalStateException("Cannot build on a cell that has a dome");
        }
        return true;
    }

    /**
     * Getter Methods
     * --------------
     */

    /**
     * Represents a cell in the Santorini game board.
     * @return the current cell where the worker is located
     */
    public Cell getCurrentCell() {
        return currentCell;
    }
    /**
     * Returns the player who owns the worker.
     *
     * @return the player who owns the worker
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
    /**
     * Sets the current cell of the worker.
     * @param currentCell the current cell of the worker
     */
    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }
}