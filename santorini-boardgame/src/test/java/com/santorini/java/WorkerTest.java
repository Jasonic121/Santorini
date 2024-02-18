package com.santorini.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import org.junit.Before;
import org.junit.Test;

public class WorkerTest {

    private Worker worker;
    private Cell initialCell;
    private Cell adjacentCell;
    private Cell nonAdjacentCell;
    private Player owner;

    @Before
    public void setup() {
        owner = new Player(0);
        worker = new Worker(owner, 0);
    }

    @Test
    public void testPlaceInitialWorker() {
        initialCell = new Cell(0, 0);
        worker.placeInitialWorker(initialCell);
        assertEquals(initialCell, worker.getCurrentCell());
        assertTrue(initialCell.isOccupied());
    }

    @Test (expected = IllegalStateException.class) 
    public void testPlaceInitialWorkerOutsideGrid() {
        final int invalidCoordinate = 5;
        initialCell = new Cell(invalidCoordinate, 0);
        worker.placeInitialWorker(initialCell);
    }

    @Test (expected = IllegalStateException.class) 
    public void testPlaceInitialWorkerOnOccupiedCell() {
        initialCell = new Cell(0, 0);
        worker.placeInitialWorker(initialCell);
        Worker anotherWorker = new Worker(owner, 1);
        anotherWorker.placeInitialWorker(initialCell);
    }


    @Test
    public void testMoveWorkerToCell() {
        initialCell = new Cell(0, 0);
        adjacentCell = new Cell(1, 1);
        worker.placeInitialWorker(initialCell);
        worker.moveWorkerToCell(adjacentCell);
        assertEquals(adjacentCell, worker.getCurrentCell());
        assertTrue(adjacentCell.isOccupied());
        assertFalse(initialCell.isOccupied());
    }

    @Test
    public void testMoveWorkerToNonAdjacentCell() {
        initialCell = new Cell(0, 0);
        nonAdjacentCell = new Cell(2, 2);
        worker.placeInitialWorker(initialCell);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.moveWorkerToCell(nonAdjacentCell);
        });
        assertEquals("Cannot move to a non-adjacent cell", thrown.getMessage());
    }

    @Test
    public void testMoveWorkerToOccupiedCell() {
        initialCell = new Cell(0, 0);
        adjacentCell = new Cell(1, 1);
        worker.placeInitialWorker(initialCell);
        Worker anotherWorker = new Worker(owner, 1);
        anotherWorker.placeInitialWorker(adjacentCell);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.moveWorkerToCell(adjacentCell);
        });
        assertEquals("Cannot move to an occupied cell", thrown.getMessage());
    }

    @Test
    public void testMoveWorkerToHigherCell() {
        initialCell = new Cell(0, 0);
        worker.placeInitialWorker(initialCell);
        Cell higherCell = new Cell(0, 1);
        higherCell.setHeight(2);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.moveWorkerToCell(higherCell);
        });
        assertEquals("Cannot move to a cell that is more than one level higher", thrown.getMessage());
    }

    @Test
    public void testBuildAtAdjacentCell() {
        initialCell = new Cell(0, 0);
        adjacentCell = new Cell(0, 1);
        worker.placeInitialWorker(initialCell);
        worker.buildAt(adjacentCell);
        assertEquals(1, adjacentCell.getHeight());
    }

    @Test
    public void testBuildAtNonAdjacentCell() {
        initialCell = new Cell(0, 0);
        nonAdjacentCell = new Cell(2, 2);
        worker.placeInitialWorker(initialCell);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.buildAt(nonAdjacentCell);
        });
        assertEquals("Cannot build on a non-adjacent cell", thrown.getMessage());
    }

    @Test
    public void testBuildAtOccupiedCell() {
        initialCell = new Cell(0, 0);
        adjacentCell = new Cell(0, 1);
        worker.placeInitialWorker(initialCell);
        Worker anotherWorker = new Worker(owner, 1);
        anotherWorker.placeInitialWorker(adjacentCell);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.buildAt(adjacentCell);
        });
        assertEquals("Cannot build on an occupied cell", thrown.getMessage());
    }
}