// WorkerTest.java
package com.santorini;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;




public class WorkerTest {
    private Worker worker;
    private Player owner;
    
    private static final int INITIAL_X = 0;
    private static final int INITIAL_Y = 0;
    private static final int DESTINATION_X = 1;
    private static final int DESTINATION_Y = 0;
    
    @BeforeEach
    void setUp() {
        owner = new Player(0);
        worker = new Worker(owner, 0);
    }
    
    @Test
    void testPlaceInitialWorker() {
        Cell initialCell = new Cell(INITIAL_X, INITIAL_Y);
        worker.placeInitialWorker(initialCell);
        assertEquals(initialCell, worker.getCurrentCell());
    }
    
    @Test
    void testMoveWorkerToCell() {
        Cell initialCell = new Cell(INITIAL_X, INITIAL_Y);
        Cell destinationCell = new Cell(DESTINATION_X, DESTINATION_Y);
        worker.placeInitialWorker(initialCell);
        worker.moveWorkerToCell(destinationCell);
        assertEquals(destinationCell, worker.getCurrentCell());
    }
    
    @Test
    void testBuildAt() {
        Cell initialCell = new Cell(INITIAL_X, INITIAL_Y);
        Cell targetCell = new Cell(DESTINATION_X, DESTINATION_Y);
        worker.placeInitialWorker(initialCell);
        worker.buildAt(targetCell);
        assertEquals(1, targetCell.getHeight());
    }
    
}