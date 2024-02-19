package com.santorini.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class CellTest {
    private Game game;
    private Board board;
    private Cell cell;
    
    @Before
    public void setUp() {
        game = new Game();
        board = game.getBoard();
        cell = board.getCell(0, 0);
    }

    @Test
    public void testInitialHeight() {
        // Verify that the initial height of the cell is 0
        assertEquals(0, cell.getHeight());
    }

    @Test
    public void testBuildBlock() {
        // Add a block to the cell
        cell.buildBlock();

        // Verify that the height of the cell is increased by 1
        assertEquals(1, cell.getHeight());
    }

    @Test
    public void testBuildDome() {
        // Add three blocks to the cell
        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();

        // Verify that the cell does not have a dome yet
        assertFalse(cell.hasDome());

        // Add a block (dome) to the cell
        cell.buildBlock();

        // Verify that the cell has a dome
        assertTrue(cell.hasDome());
    }

    @Test
    public void testBuildBlockMaxHeight() {
        final int maxHeight = 4;
        // Add three blocks to the cell
        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();

        // Verify that the height of the cell is 4 (has a dome)
        assertEquals(maxHeight, cell.getHeight());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            // Add another block to the cell (Should throw an exception)
            cell.buildBlock();
        });
        assertEquals("Cell already has a dome", thrown.getMessage());
    }

    @Test
    public void testIsOccupied() {
        // Verify that the cell is initially unoccupied
        assertFalse(cell.isOccupied());

        // Add a block to the cell
        cell.buildBlock();

        // Verify that the cell is still unoccupied
        assertFalse(cell.isOccupied());

        // Add a dome to the cell
        cell.buildBlock();

        // Verify that the cell is still unoccupied
        assertFalse(cell.isOccupied());
    }

    @Test
    public void testBuildBlockWithMaxHeight() {
        final int expectedHeight1 = 3;
        final int expectedHeight2 = 4;
        // Add three blocks to the cell
        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();

        // Verify that the height of the cell is 3
        assertEquals(expectedHeight1, cell.getHeight());

        // Verify that the cell does not have a dome
        assertFalse(cell.hasDome());

        // Add another block to the cell
        cell.buildBlock();

        // Verify that the height of the cell remains at 4
        assertEquals(expectedHeight2, cell.getHeight());

        // Verify that the cell has a dome
        assertTrue(cell.hasDome());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildDomeTwice() {
        // Add three blocks to the cell
        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();

        // Verify that the cell does not have a dome yet
        assertFalse(cell.hasDome());

        // Add a block (dome) to the cell
        cell.buildBlock();

        // Verify that the cell has a dome
        assertTrue(cell.hasDome());

        // Try to add another block (dome) to the cell, should throw an exception
        cell.buildBlock();
    }
    // END: Additional Test Cases
}