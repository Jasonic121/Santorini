package com.santorini.java;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    private Player player;

    @Before
    public void setUp() {
        player = new Player(0);
    }
    @Test
    public void testWorkerAmount() {
        assertEquals(2, player.getWorkerAmount());
    }

    @Test
    public void testPlaceWorkerOnBoard() {
        Cell initialCell = new Cell(0, 0);
        player.placeWorkerOnBoard(0, initialCell);
        assertEquals(initialCell, player.getWorkerCurrentCell(0));
    }

    @Test
    public void testMoveWorker() {
        Cell initialCell = new Cell(0, 0);
        Cell destinationCell = new Cell(1, 1);
        player.placeWorkerOnBoard(0, initialCell);
        player.moveWorker(0, destinationCell);
        assertEquals(destinationCell, player.getWorkerCurrentCell(0));
    }

    @Test
    public void testBuild() {
        Cell initialCell = new Cell(0, 0);
        player.placeWorkerOnBoard(0, initialCell);
        Cell targetCell = new Cell(0, 1);
        player.build(0, targetCell);
        assertEquals(1, targetCell.getHeight());
    }
}