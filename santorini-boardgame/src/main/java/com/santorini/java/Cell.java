package com.santorini.java;

/**
 * Represents a cell on the Santorini game board.
 */
public class Cell {
    private int xPosition;
    private int yPosition;
    private int height;
    private boolean hasDome;
    private boolean occupied;

    /**
     * Constructs a new Cell object with the specified x and y positions.
     *
     * @param x The x position of the cell.
     * @param y The y position of the cell.
     */
    public Cell(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        this.height = 0; // initial height is 0
        this.hasDome = false; // initially no dome
        this.occupied = false; // initially unoccupied
    }

    /*
    Building logic for the cell
    */

    /**
     * Builds a block on the cell. If the height is less than the maximum height, the height is increased by 1.
     * Otherwise, a dome is built on top of the block.
     */
    public void buildBlock() {
        final int maxHeight = 4;
        if (height < maxHeight - 1) {
            height++;
        } else {
            height++;
            this.buildDome();
        }
    }

    /**
     * Builds a dome on the cell.
     * If the cell already has a dome, an IllegalStateException is thrown.
     */
    private void buildDome() {
        if (hasDome) {
            throw new IllegalStateException("Cell already has a dome");
        }
        hasDome = true;
    }

    /**
     * Setter Methods
     * --------------
     */

    /**
     * Sets the occupied status of the cell.
     *
     * @param occupied The occupied status to set.
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    /**
     * Sets the height of the cell.
     *
     * @param height The height to set.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Getter Methods
     * --------------
     */

    /**
     * Returns the x position of the cell.
     *
     * @return The x position of the cell.
     */
    public int getX() {
        return xPosition;
    }

    /**
     * Returns the y position of the cell.
     *
     * @return The y position of the cell.
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
     * Returns whether the cell has a dome.
     *
     * @return True if the cell has a dome, false otherwise.
     */
    public boolean hasDome() {
        return hasDome;
    }

    /**
     * Returns whether the cell is occupied.
     *
     * @return True if the cell is occupied, false otherwise.
     */
    public boolean isOccupied() {
        return occupied;
    }
}
