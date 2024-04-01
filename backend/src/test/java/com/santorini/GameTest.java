package com.santorini;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;

public class GameTest {
    private Game game;
    private Board board;
    private Player player1;
    private Player player2;
    private Cell initialCell;
    private Cell initialCell2;
    private Cell initialCell3;
    private Cell initialCell4;

    @Before
    public void setUp() {
        final int initialCell4index = 3;
        game = new Game();
        board = game.getBoard();
        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);

        initialCell = board.getCell(0,0);
        initialCell2 = board.getCell(1, 1);
        initialCell3 = board.getCell(2, 2);
        initialCell4 = board.getCell(initialCell4index, initialCell4index);

        game.setupInitialWorker(initialCell, initialCell2, initialCell3, initialCell4); // This should place workers on the board

        System.out.println("Board for testInitialSetupAndPlayerTurnOrder() is as follows:");
        printOccupiedCells();
    }

    @Test
    public void testInitialSetupAndPlayerTurnOrder() {
        System.out.println("Board for testInitialSetupAndPlayerTurnOrder() is as follows:");

        // Assert that two players are added
        assertEquals("The game should have two players", 2, game.getPlayers().size());

        // Check if the first player is set correctly
        assertSame("The first player should be Player 1", player1, game.getCurrentPlayer());

        // Simulate a turn for player 1 and check if player 2 is next
        game.executeTurn(0, 1, 0, 0, 1); // This should change the current player to player 2
        assertSame("The second player should now be the current player", player2, game.getCurrentPlayer());
    }
    
    @Test
    public void testWorkerPlacement() {
        System.out.println("Board for testWorkerPlacement() is as follows:");

        Cell worker1Cell = player1.getWorkerCurrentCell(0); 
        Cell worker2Cell = player2.getWorkerCurrentCell(0);

        assertTrue("Worker 1 should be on the board", worker1Cell.isOccupied());
        assertTrue("Worker 2 should be on the board", worker2Cell.isOccupied());

    }

    @Test
    public void testPlayerMovement() {
        System.out.println("Board for testPlayerMovement() is as follows:");

        // Arrange
        Player player1 = game.getPlayers().get(0);
        Cell destinationCell = game.getBoard().getCell(1, 0);
    
        // Act
        player1.moveWorker(0, destinationCell);
    
        // Assert
        assertSame("Player 1's worker should have moved", destinationCell, player1.getWorkerCurrentCell(0));
    
    }
    

    @Test
    public void testBuildingMechanism() {
        System.out.println("Board for testBuildingMechanism() is as follows:");

        // Arrange
        Player player1 = game.getPlayers().get(0);
        Cell buildCell = game.getBoard().getCell(2, 0);
        Cell destinationCell = game.getBoard().getCell(1, 0);

        // Act
        player1.moveWorker(0, destinationCell); // Place the worker near the build cell
        player1.build(0, buildCell); // Build on the cell
    
        // Assert
        assertEquals("A block should have been built on the cell", 1, buildCell.getHeight());
    
    }
    
    @Test
    public void testWinningCondition() {
        System.out.println("Board for testWinningCondition() is as follows:");
        
        final int winHeight = 3;
        // Arrange
        Player player1 = game.getPlayers().get(0);

        // Act
        player1.build(0, game.getBoard().getCell(1, 0)); // Build to the height: 1
        player1.moveWorker(0, game.getBoard().getCell(1, 0)); // Move to the cell

        player1.build(0, game.getBoard().getCell(2, 0)); // Build to the height: 2
        player1.build(0, game.getBoard().getCell(2, 0)); 
        player1.moveWorker(0, game.getBoard().getCell(2, 0)); // Move to the win cell
        
        player1.build(0, game.getBoard().getCell(winHeight, 0)); // Build to the height: 3
        player1.build(0, game.getBoard().getCell(winHeight, 0)); 
        player1.build(0, game.getBoard().getCell(winHeight, 0)); 
        player1.moveWorker(0, game.getBoard().getCell(winHeight, 0)); // Move to the win cell

        // Assert
        assertTrue("Player 1 should have won the game", player1.checkWin());
    }

    @After
    public void tearDown() {
        printGrid();
        System.out.println();
        printOccupiedCells();
        System.out.println();
        System.out.println();
    }

    private void printGrid() {    
        System.out.println("Grid:");
        final int gridLength = 5; 
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                Cell cell = game.getBoard().getCell(i, j);
                System.out.print(cell.getHeight() + " ");
            }
            System.out.println();
        }
    }

    private void printOccupiedCells() {
        System.out.println("Occupied cells:");
        final int gridLength = 5; 
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                Cell cell = game.getBoard().getCell(i, j);
                int integer = cell.isOccupied() ? 1 : 0;
                System.out.print(integer + " ");
            }
            System.out.println();
        }
    }
}