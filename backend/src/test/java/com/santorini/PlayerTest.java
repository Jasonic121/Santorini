// PlayerTest.java
package com.santorini;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;




public class PlayerTest {
    private Player player;
    private static final int WORKER_INDEX = 0;
    private static final int INITIAL_CELL_X = 0;
    private static final int INITIAL_CELL_Y = 0;
    private static final int DESTINATION_CELL_X = 1;
    private static final int DESTINATION_CELL_Y = 0;
    
    @BeforeEach
    void setUp() {
        player = new Player(WORKER_INDEX);
    }
    
    @Test
    void testPlaceWorkerOnBoard() {
        Cell initialCell = new Cell(INITIAL_CELL_X, INITIAL_CELL_Y);
        player.placeWorkerOnBoard(WORKER_INDEX, initialCell);
        assertEquals(initialCell, player.getWorker(WORKER_INDEX).getCurrentCell());
    }
    
    @Test
    void testMoveWorker() {
        Cell initialCell = new Cell(INITIAL_CELL_X, INITIAL_CELL_Y);
        Cell destinationCell = new Cell(DESTINATION_CELL_X, DESTINATION_CELL_Y);
        player.placeWorkerOnBoard(WORKER_INDEX, initialCell);
        player.moveWorker(WORKER_INDEX, destinationCell);
        assertEquals(destinationCell, player.getWorker(WORKER_INDEX).getCurrentCell());
    }
    
    @Test
    void testBuild() {
        Cell initialCell = new Cell(INITIAL_CELL_X, INITIAL_CELL_Y);
        Cell targetCell = new Cell(DESTINATION_CELL_X, DESTINATION_CELL_Y);
        player.placeWorkerOnBoard(WORKER_INDEX, initialCell);
        player.build(WORKER_INDEX, targetCell);
        assertEquals(1, targetCell.getHeight());
    }
    
}