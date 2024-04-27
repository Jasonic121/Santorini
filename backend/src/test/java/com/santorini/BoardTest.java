// BoardTest.java
package com.santorini;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {
    private static final int BOARD_SIZE = 3;
    private Board board;
    
    @BeforeEach
    void setUp() {
        board = new Board();
    }
    
    @Test
    void testValidateCellsForMoving() {
        Cell workerCell = board.getCell(0, 0);
        Cell[] validCells = board.validateCellsForMoving(workerCell);
        assertEquals(BOARD_SIZE, validCells.length);
    }
    
    @Test
    void testValidateCellsForBuilding() {
        Cell workerCell = board.getCell(0, 0);
        Cell[] validCells = board.validateCellsForBuilding(workerCell);
        assertEquals(BOARD_SIZE, validCells.length);
    }
    
    @Test
    void testGetCell() {
        Cell cell = board.getCell(0, 0);
        assertEquals(0, cell.getX());
        assertEquals(0, cell.getY());
    }
    
}
