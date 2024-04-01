package com.santorini;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BoardTest {
    private Game game;
    private Board board;
    private final int gridLength = 5;

    @Before
    public void setUp() {
        game = new Game();
        board = game.getBoard();
    }

    // Test the size initialization of the board   
    @Test
    public void testBoardInitialization() {
        Cell cell;

        // Verify that the grid is initialized with the correct dimensions
        assertEquals(gridLength, board.getGrid().size());
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++)
                assertNotNull(board.getCell(i, j));
        }

        // Verify that all heights are initially 0 and domes are false
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                cell = board.getCell(i, j);
                assertEquals(0, cell.getHeight());
                assertFalse(cell.hasDome());
            }
        }

        // Verify that all cells are initially unoccupied
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                cell = board.getCell(i, j);
                assertFalse(cell.isOccupied());
            }
        }
        System.out.println("Board initialization test passed");
    }

    @Test
    public void testAddBlock() {
        final int x = 0;
        final int y = 0;
        Cell cell = board.getCell(x, y);
        
        // Add a block to the specified cell
        cell.buildBlock();
        // Verify that the cell's height is increased by 1
        assertEquals("Cell height should be 1 after adding one block.", 1, cell.getHeight());

        // Add a block to the specified cell
        cell.buildBlock();
        // Verify that the cell's height is increased by 1
        assertEquals("Cell height should be 2 after adding two blocks.", 2, cell.getHeight());

    }

    @Test
    public void testAddDome() {
        final int x = 1;
        final int y = 0;
        Cell cell = board.getCell(x, y);

        // Add three blocks to the specified cell
        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();

        // Verify that the cell's dome is false
        assertFalse("The cell should not have a dome yet.", cell.hasDome());

        // Add a block (Dome) to the specified cell
        cell.buildBlock();

        // Verify that the cell's dome status is true
        assertTrue("The cell should have a dome.", cell.hasDome());
    }


    @After
    public void tearDown() {
        System.out.println("Below is the Board Height Grid:");
        printGrid();
        System.out.println();
    }

    private void printGrid() {    
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                Cell cell = board.getCell(i, j);
                System.out.print(cell.getHeight() + " ");
            }
            System.out.println();
        }
    }
}
