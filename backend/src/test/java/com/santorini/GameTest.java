// GameTest.java
package com.santorini;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void testInitialWorkerSetup() {
        Cell initialCell = game.getBoard().getCell(0, 0);
        game.setupInitialWorker(initialCell, 0, 0);
        assertTrue(initialCell.isOccupied());
    }
    // @Test
    // void testMoveExecution() {
    //     Cell initialCell = game.getBoard().getCell(0, 0);
    //     game.setupInitialWorker(initialCell, 0, 0);
        
    //     // Place the worker on the initial cell
    //     Worker worker = game.getCurrentPlayer().getWorker(0);
    //     worker.placeInitialWorker(initialCell);
        
    //     game.executeMoveTurn(0, 0, 1);
    //     assertTrue(game.getBoard().getCell(0, 1).isOccupied());
    // }

    // @Test
    // void testBuildExecution() {
    //     Cell initialCell = game.getBoard().getCell(0, 0);
    //     game.setupInitialWorker(initialCell, 0, 0);
    //     game.executeBuildTurn(0, 1, 0);
    //     assertEquals(1, game.getBoard().getCell(1, 0).getHeight());
    // }

    @Test
    void testPlayerSwitch() {
        game.nextPlayer();
        assertEquals(1, game.getCurrentPlayer().getPlayerId());
    }

    @Test
    void testWorkerFinding() {
        Cell initialCell = game.getBoard().getCell(0, 0);
        game.setupInitialWorker(initialCell, 0, 0);
        Worker worker = game.findWorkerAtPosition(0, 0);
        assertNotNull(worker);
        assertEquals(0, worker.getWorkerId());
    }

    // @Test
    // void testWinCondition() {
    //     Cell initialCell = game.getBoard().getCell(0, 0);
    //     initialCell.buildBlock();
    //     initialCell.buildBlock();
    //     initialCell.buildBlock();
    //     game.setupInitialWorker(initialCell, 0, 0);
    //     game.executeMoveTurn(0, 0, 0);
    //     assertTrue(game.getEndGameFlag());
    //     assertNotNull(game.getWinner());
    // }
}