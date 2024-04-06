// BoardTest.java
package com.santorini;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testBoardInitialization() {
        assertEquals(25, board.getGrid().length);
    }

    @Test
    void testCellRetrieval() {
        Cell cell = board.getCell(0, 0);
        assertNotNull(cell);
        assertEquals(0, cell.getX());
        assertEquals(0, cell.getY());
    }

    @Test
    void testValidMoveCells() {
        Cell workerCell = board.getCell(2, 2);
        Cell[] validCells = board.validateCellsForMoving(workerCell);
        assertEquals(8, validCells.length);
    }

    @Test
    void testValidBuildCells() {
        Cell workerCell = board.getCell(2, 2);
        Cell[] validCells = board.validateCellsForBuilding(workerCell);
        for (Cell cell : validCells) {
            System.out.println(cell.getX() + " " + cell.getY());
        }
        assertEquals(8, validCells.length);
    }
}