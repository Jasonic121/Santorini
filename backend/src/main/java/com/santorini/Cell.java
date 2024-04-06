package com.santorini;

/**
 * Represents a cell on the Santorini game board.
 */
public class Cell {
    private int xPosition;
    private int yPosition;
    private int height;
    private boolean hasDome;
    private boolean occupied;
    private int occupiedBy;

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
        this.occupied = false;
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
     * Sets the height of the cell to the specified value.
     *
     * @param height The height of the cell.
     * @return void
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    /**
     * Sets the player ID of the player occupying the cell.
     *
     * @param playerId The ID of the player occupying the cell.
     * @return void
     */
    public void setOccupiedBy(int playerId) {
        this.occupiedBy = playerId;
    }

    /**
     * Getter Methods
     * --------------
     */

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasDome() {
        return hasDome;
    }

    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"x\":%d,\"y\":%d,\"height\":%d,\"hasDome\":%b,\"occupied\":%b,\"occupiedBy\":%d}",
            xPosition, yPosition, height, hasDome, occupied, occupiedBy
        );
    }
}