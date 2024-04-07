// Cell.java
package com.santorini;

/**
 * Represents a cell on the Santorini game board.
 */
public class Cell {
    private int xPosition;
    private int yPosition;
    private int height;
    private boolean hasDome;
    private Worker worker;

    /**
     * Constructs a new Cell object with the specified x and y positions.
     *
     * @param x The x position of the cell.
     * @param y The y position of the cell.
     */
    public Cell(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        this.height = 0;
        this.hasDome = false;
        this.worker = null;
    }

    /**
     * Builds a block on the cell. If the height is less than the maximum height, the height is increased by 1.
     * Otherwise, a dome is built on top of the block.
     */
    public void buildBlock() {
        final int maxHeight = 3;
        if (height == maxHeight) {
            hasDome = true;
            System.out.println("Dome built on cell " + xPosition + ", " + yPosition + ". Height increased to " + (height + 1));
        }
        height++;
    }

    /**
     * Setter Methods
     * --------------
     */

    /**
     * Sets the worker on the cell.
     */
    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    /**
     * Removes the worker from the cell.
     */
    public void removeWorker() {
        this.worker = null;
    }

    /**
     * Sets the height of the cell.
     *
     * @param height The height to be set for the cell.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets the dome status of the cell.
     *
     * @param hasDome The dome status to be set for the cell.
     */
    public void setDome(boolean hasDome) {
        this.hasDome = hasDome;
    }

    /**
     * Getter Methods
     * --------------
     */

    /**
     * Returns the x-coordinate of the cell.
     *
     * @return The x-coordinate of the cell.
     */
    public int getX() {
        return xPosition;
    }

    /**
     * Returns the y-coordinate of the cell.
     *
     * @return The y-coordinate of the cell.
     */
    public int getY() {
        return yPosition;
    }

    /**
     * Returns the height of the cell.
     *
     * @return The height of the cell.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns true if the cell has a dome, false otherwise.
     *
     * @return true if the cell has a dome, false otherwise
     */
    public boolean hasDome() {
        return hasDome;
    }

    /**
     * Returns true if the cell is occupied by a worker, false otherwise.
     *
     * @return true if the cell is occupied by a worker, false otherwise
     */
    public boolean isOccupied() {
        return worker != null;
    }

    /**
     * Returns the worker occupying the cell.
     *
     * @return The worker occupying the cell, or null if no worker is present.
     */
    public Worker getWorker() {
        return worker;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"x\":%d,\"y\":%d,\"height\":%d,\"hasDome\":%b,\"occupied\":%b,\"occupiedBy\":%d}",
            xPosition, yPosition, height, hasDome, isOccupied(), worker != null ? worker.getOwner().getPlayerId() : -1
        );
    }
}