// PlayerTest.java
package com.santorini;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(0);
    }

    @Test
    void testWorkerPlacement() {
        Cell cell = new Cell(0, 0);
        player.placeWorkerOnBoard(0, cell);
        assertEquals(cell, player.getWorkerCurrentCell(0));
    }

    @Test
    void testWorkerMovement() {
        Cell initialCell = new Cell(0, 0);
        Cell destinationCell = new Cell(0, 1);
        player.placeWorkerOnBoard(0, initialCell);
        player.moveWorker(0, destinationCell);
        assertEquals(destinationCell, player.getWorkerCurrentCell(0));
    }

    @Test
    void testBuildAction() {
        Cell initialCell = new Cell(0, 0);
        Cell targetCell = new Cell(0, 1);
        player.placeWorkerOnBoard(0, initialCell);
        player.build(0, targetCell);
        assertEquals(1, targetCell.getHeight());
    }

    @Test
    void testWinCondition() {
        Cell initialCell = new Cell(0, 0);
        initialCell.buildBlock();
        initialCell.buildBlock();
        initialCell.buildBlock();
        player.placeWorkerOnBoard(0, initialCell);
        assertTrue(player.checkWin());
    }

    // @Test
    // void testLoseCondition() {
    //     Board board = new Board();
    //     for (int i = 0; i < 5; i++) {
    //         for (int j = 0; j < 5; j++) {
    //             if (i != 2 || j != 2) {
    //                 board.getCell(i, j).buildBlock();
    //                 board.getCell(i, j).buildBlock();
    //                 board.getCell(i, j).buildBlock();
    //                 board.getCell(i, j).buildBlock();
    //             }
    //         }
    //     }
    //     player.placeWorkerOnBoard(0, board.getCell(2, 2));
    //     assertTrue(player.checkLose(board));
    // }
}