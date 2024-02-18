package com.santorini.java;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;

public class GameIntegrationTest {
    private Game game;
    private Player player1;
    private Player player2;
    private Board board;

    @Before
    public void setUp() {
        final int initialX1 = 0;
        final int initialY1 = 0;
        final int initialX2 = 1;
        final int initialY2 = 1;  
        final int initialX3 = 2;
        final int initialY3 = 2;
        final int initialX4 = 3;  
        final int initialY4 = 3;

        Cell initialCell = new Cell(initialX1, initialY1);
        Cell initialCell2 = new Cell(initialX2, initialY2);
        Cell initialCell3 = new Cell(initialX3, initialY3);
        Cell initialCell4 = new Cell(initialX4, initialY4);

        game = new Game();
        board = game.getBoard();
        player1 = new Player(0);
        player2 = new Player(1);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.setupInitialWorker(initialCell, initialCell2, initialCell3, initialCell4); // This should place workers on the board
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
        game.startGame(); // This should change the current player to player 2
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
        Cell buildCell = game.getBoard().getCell(1, 1);
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
    
    // @Test
    // public void testBlockingAndLosingConditions() {
    //     // Arrange
    //     Player player1 = game.getPlayers().get(0);
    //     Player player2 = game.getPlayers().get(1);
    //     Cell pointerCell;

    //     // Simulate the board being in a state where Player 1 is blocked
    //     // This will likely involve setting up a specific board state where player1 has no legal moves
    //     pointerCell = game.getBoard().getCell(0, );
    //     .setHeight

    
    //     // Act
    //     // No move is made since the player is assumed to be blocked already
    
    //     // Assert
    //     assertTrue("Player 1 should have lost", player1.checkLose());
    
    //     System.out.println("Board for testBlockingAndLosingConditions() is as follows:");
    // }
    
//     @Test
//     public void testGameFlow() {

//         /* Setup for the game  */
//         // Arrange
//         Game game = new Game();
//         Player player1 = new Player(0);
//         Player player2 = new Player(1);

//         // Act
//         game.addPlayer(player1);
//         game.addPlayer(player2);
//         game.setup();

//         // Assert
//         assertEquals( "The game should have two players", 2, game.getPlayers().size());
//         assertEquals("The first player should be Player 1", player1, game.getPlayers().get(0));
//         assertEquals("The second player should be Player 2", player2, game.getPlayers().get(1));

//         /* Player 0 start moving */
//         // Act
//         MoveResult result1 = game.makeMove(player1, new Move(/* parameters */));

//         // Assert
//         assertEquals(MoveResult.SUCCESS, result1, "Player 1's move should be successful");

//         // Act
//         MoveResult result2 = game.makeMove(player2, new Move(/* parameters */));

//         // Assert
//         assertEquals(MoveResult.SUCCESS, result2, "Player 2's move should be successful");
//     }

    @After
    public void tearDown() {
        printGrid();
        System.out.println();
        printOccupiedCells();
        System.out.println();
        System.out.println();
    }

    private void printGrid() {    
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