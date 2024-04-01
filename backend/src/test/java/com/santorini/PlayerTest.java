package com.santorini;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    private Game game;
    private Board board;
    private Player player;

    @Before
    public void setUp() {
        game = new Game();
        board = game.getBoard();
        player = game.getPlayers().get(0);
    }
    @Test
    public void testWorkerAmount() {
        assertEquals(2, player.getWorkerAmount());
    }

    @Test
    public void testPlaceWorkerOnBoard() {
        Cell initialCell = board.getCell(0, 0);
        player.placeWorkerOnBoard(0, initialCell);
        assertEquals(initialCell, player.getWorkerCurrentCell(0));
    }

    @Test
    public void testMoveWorker() {
        Cell initialCell = board.getCell(0, 0);
        Cell destinationCell = board.getCell(1, 1);
        player.placeWorkerOnBoard(0, initialCell);
        player.moveWorker(0, destinationCell);
        assertEquals(destinationCell, player.getWorkerCurrentCell(0));
    }

    @Test
    public void testBuild() {
        Cell initialCell = board.getCell(0, 0);
        player.placeWorkerOnBoard(0, initialCell);
        Cell targetCell = board.getCell(0, 1);
        player.build(0, targetCell);
        assertEquals(1, targetCell.getHeight());
    }
}