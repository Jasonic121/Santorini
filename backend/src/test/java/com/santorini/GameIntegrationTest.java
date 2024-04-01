package com.santorini;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;

import org.junit.After;

public class GameIntegrationTest {
    private Game game;
    private Board board;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() {
        final int intitialCell3index = 3;
        final int intitialCell4index = 4;

        game = new Game();
        board = game.getBoard();

        Cell initialCell = board.getCell(0, 0);
        Cell initialCell2 = board.getCell(0, 1);
        Cell initialCell3 = board.getCell(0, intitialCell3index);
        Cell initialCell4 = board.getCell(0, intitialCell4index);

        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.setupInitialWorker(initialCell2, initialCell4, initialCell, initialCell3); // This should place workers on the board
        printOccupiedCells();
    }

    @Test
    public void testFirstPlayerWin() {
        System.out.println("Board for testFirstPlayerWin() is as follows:");
        final int indexThree = 3;
        final int indexFour = 4;

        game.executeTurn(0, 1, 1, 1, 0); // Player 1 moves worker 0 to (1, 1) and builds a level 1 block at (1, 0)
        game.executeTurn(0, 1, 0, 2, 0); // Player 2 moves worker 0 to (1, 0) and builds a level 1 block at (2, 0)
        game.executeTurn(0, 2, 1, 2, 0); // Player 1 moves worker 0 to (2, 1) and builds a level 1 block at (2, 0)
        game.executeTurn(0, 2, 0, indexThree, 0); // Player 2 moves worker 0 to (2, 0) and builds a level 1 block at (3, 0)
        game.executeTurn(0, indexThree, 1, indexThree, 0); // Player 1 moves worker 0 to (3, 1) and builds a level 1 block at (3, 0)
        game.executeTurn(1, 1, indexThree, 0, indexThree); // Player 2 moves worker 1 to (1, 3) and builds a level 1 block at (0, 3)
        game.executeTurn(0, indexFour, 1, indexThree, 0); // Player 1 moves worker 0 to (4, 1) and builds a level 1 block at (3, 0)
        game.executeTurn(0, indexThree, 0, indexFour, 0); // Player 2 win
        assertTrue(game.getEndGameFlag());
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