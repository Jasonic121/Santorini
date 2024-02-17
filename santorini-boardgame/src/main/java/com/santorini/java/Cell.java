package com.santorini.java;

public class Cell {
    private int xPosition;
    private int yPosition;
    private int height;
    private boolean hasDome;
    private boolean occupied;

    public Cell(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        this.height = 0; // initial height is 0
        this.hasDome = false; //initially no dome
        this.occupied = false; //initially unoccupied
    }

    /*
    Building logic for the cell
    */
    public void buildBlock() {
        final int maxHeight = 4;
        if (height < maxHeight - 1 ) {
            height++;
        } else {
            height++;
            this.buildDome();
        }
    }

    // Cannot be called from outside the class
    private void buildDome() {
        if (hasDome) {
            throw new IllegalStateException("Cell already has a dome");
        }
        hasDome = true;
    }

    /* 
    Setters for Cell
    */
    // Setter for Occupied
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    // Setter for Height
    public void setHeight(int height) {
        this.height = height;
    }

    /* 
    Getters for Cell
    */
    // Getter for xPosition
    public int getX() {
        return xPosition;
    }
    // Getter for yPositioN
    public int getY() {
        return yPosition;
    }
    // Getter for height
    public int getHeight() {
        return height;
    }

    // Getter for Dome
    public boolean hasDome() {
        return hasDome;
    }

    // Getter for Occupied 
    public boolean isOccupied() {
        return occupied;
    }

}
