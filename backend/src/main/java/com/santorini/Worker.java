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
        initialCell.setOccupied(true);
        initialCell.setOccupiedBy(owner.getPlayerId());
        currentCell = initialCell;
    }

    /**
     * Moves the worker to the specified destination cell.
     *
     * @param destination The destination cell where the worker is moved to.
     */
    public void moveWorkerToCell(Cell destination) {
        if (validateMove(destination)) {
            currentCell.setOccupied(false);
            destination.setOccupied(true);
            destination.setOccupiedBy(owner.getPlayerId());
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
        if (Math.abs(destination.getHeight() - currentCell.getHeight()) > 1) {
            throw new IllegalStateException("Cannot move to a cell that is more than one level higher");
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
     */
    private boolean validateBuild(Cell targetCell) {
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