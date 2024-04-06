// WorkerTest.java
package com.santorini;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {
    private Worker worker;
    private Player owner;

    @BeforeEach
    void setUp() {
        owner = new Player(0);
        worker = new Worker(owner, 0);
    }

    @Test
    void testInitialPlacement() {
        Cell initialCell = new Cell(0, 0);
        worker.placeInitialWorker(initialCell);
        assertEquals(initialCell, worker.getCurrentCell());
        assertTrue(initialCell.isOccupied());
    }

    @Test
    void testMovement() {
        Cell initialCell = new Cell(0, 0);
        Cell destinationCell = new Cell(0, 1);
        worker.placeInitialWorker(initialCell);
        worker.moveWorkerToCell(destinationCell);
        assertEquals(destinationCell, worker.getCurrentCell());
        assertFalse(initialCell.isOccupied());
        assertTrue(destinationCell.isOccupied());
    }

    @Test
    void testBuildAction() {
        Cell initialCell = new Cell(0, 0);
        Cell targetCell = new Cell(0, 1);
        worker.placeInitialWorker(initialCell);
        worker.buildAt(targetCell);
        assertEquals(1, targetCell.getHeight());
    }

    @Test
    void testInvalidMovement() {
        Cell initialCell = new Cell(0, 0);
        Cell occupiedCell = new Cell(0, 1);
        occupiedCell.setOccupied(true);
        worker.placeInitialWorker(initialCell);
        assertThrows(IllegalStateException.class, () -> worker.moveWorkerToCell(occupiedCell));
    }

    @Test
    void testInvalidBuild() {
        Cell initialCell = new Cell(0, 0);
        Cell domeCell = new Cell(0, 1);
        domeCell.buildBlock();
        domeCell.buildBlock();
        domeCell.buildBlock();
        domeCell.buildBlock();
        worker.placeInitialWorker(initialCell);
        assertThrows(IllegalStateException.class, () -> worker.buildAt(domeCell));
    }
}