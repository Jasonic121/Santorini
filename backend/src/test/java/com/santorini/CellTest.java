import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// CellTest.java
package com.santorini;


class CellTest {
    private Cell cell;
    private static final int INITIAL_HEIGHT = 0;
    private static final int INITIAL_WORKER_ID = 0;
    
    @BeforeEach
    void setUp() {
        cell = new Cell(INITIAL_HEIGHT, INITIAL_HEIGHT);
    }
    
    @Test
    void testBuildBlock() {
        cell.buildBlock();
        assertEquals(1, cell.getHeight());
    }
    
    @Test
    void testSetWorker() {
        Worker worker = new Worker(new Player(INITIAL_WORKER_ID), INITIAL_WORKER_ID);
        cell.setWorker(worker);
        assertEquals(worker, cell.getWorker());
    }
    
    @Test
    void testRemoveWorker() {
        Worker worker = new Worker(new Player(INITIAL_WORKER_ID), INITIAL_WORKER_ID);
        cell.setWorker(worker);
        cell.removeWorker();
        assertNull(cell.getWorker());
    }
}
