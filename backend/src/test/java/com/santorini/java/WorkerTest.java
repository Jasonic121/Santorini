package com.santorini.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;

import static org.junit.Assert.assertThrows;
import org.junit.Before;
import org.junit.Test;

public class WorkerTest {
    private Game game;
    private Board board;
    private Cell initialCell;
    private Cell adjacentCell;
    private Cell nonAdjacentCell;
    private Player player1;
    private Worker worker;
    private Worker worker2;

    @Before
    public void setup() {
        game = new Game();
        board = game.getBoard();
        player1 = game.getPlayers().get(0);
        worker = player1.getWorker(0);
        worker2 = player1.getWorker(1);
    }

    @Test
    public void testPlaceInitialWorker() {
        initialCell = board.getCell(0, 0);
        worker.placeInitialWorker(initialCell);
        assertEquals(initialCell, worker.getCurrentCell());
        assertTrue(initialCell.isOccupied());
    }

    @Test (expected = IllegalStateException.class) 
    public void testPlaceInitialWorkerOutsideGrid() {
        final int invalidCoordinate = 5;
        Cell initialCell = new Cell(invalidCoordinate, 0);
        worker.placeInitialWorker(initialCell);
    }

    @Test (expected = IllegalStateException.class) 
    public void testPlaceInitialWorkerOnOccupiedCell() {
        initialCell = board.getCell(0, 0);
        worker.placeInitialWorker(initialCell);
        Worker anotherWorker = player1.getWorker(1);
        anotherWorker.placeInitialWorker(initialCell);
    }


    @Test
    public void testMoveWorkerToCell() {
        initialCell = board.getCell(0, 0);
        adjacentCell = board.getCell(1, 1);
        worker.placeInitialWorker(initialCell);
        worker.moveWorkerToCell(adjacentCell);
        assertEquals(adjacentCell, worker.getCurrentCell());
        assertTrue(adjacentCell.isOccupied());
        assertFalse(initialCell.isOccupied());
    }

    @Test
    public void testMoveWorkerToNonAdjacentCell() {
        initialCell = board.getCell(0, 0);
        nonAdjacentCell = board.getCell(2, 2);
        worker.placeInitialWorker(initialCell);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.moveWorkerToCell(nonAdjacentCell);
        });
        assertEquals("Cannot move to a non-adjacent cell", thrown.getMessage());
    }

    @Test
    public void testMoveWorkerToOccupiedCell() {
        initialCell = board.getCell(0, 0);
        adjacentCell = board.getCell(1, 1);
        worker.placeInitialWorker(initialCell);
        Worker anotherWorker = player1.getWorker(1);
        anotherWorker.placeInitialWorker(adjacentCell);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.moveWorkerToCell(adjacentCell);
        });
        assertEquals("Cannot move to an occupied cell", thrown.getMessage());
    }

    @Test
    public void testMoveWorkerToHigherCell() {
        initialCell = board.getCell(0, 0);
        worker.placeInitialWorker(initialCell);
        Cell higherCell = board.getCell(0, 1);
        higherCell.setHeight(2);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.moveWorkerToCell(higherCell);
        });
        assertEquals("Cannot move to a cell that is more than one level higher", thrown.getMessage());
    }

    @Test
    public void testBuildAtAdjacentCell() {
        initialCell = board.getCell(0, 0);
        adjacentCell = board.getCell(0, 1); 
        worker.placeInitialWorker(initialCell);
        worker.buildAt(adjacentCell);
        assertEquals(1, adjacentCell.getHeight());
    }

    @Test
    public void testBuildAtNonAdjacentCell() {
        initialCell = board.getCell(0, 0);
        nonAdjacentCell = board.getCell(2, 2);
        worker.placeInitialWorker(initialCell);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.buildAt(nonAdjacentCell);
        });
        assertEquals("Cannot build on a non-adjacent cell", thrown.getMessage());
    }

    @Test
    public void testBuildAtOccupiedCell() {
        initialCell = board.getCell(0, 0);
        adjacentCell = board.getCell(0, 1);
        worker.placeInitialWorker(initialCell);
        Worker anotherWorker = player1.getWorker(1);
        anotherWorker.placeInitialWorker(adjacentCell);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            worker.buildAt(adjacentCell);
        });
        assertEquals("Cannot build on an occupied cell", thrown.getMessage());
    }
    @Test
    public void testCheckMovePossibilities() {
        worker.placeInitialWorker(board.getCell(0, 0));
        worker2.placeInitialWorker(board.getCell(1, 0));

        Worker anotherWorker2 = game.getPlayers().get(1).getWorker(0);
        Worker anotherWorker3 = game.getPlayers().get(1).getWorker(1);
        anotherWorker2.placeInitialWorker(board.getCell(0, 1));
        anotherWorker3.placeInitialWorker(board.getCell(1, 1));

        // Test when there are move possibilities
        assertFalse(player1.checkLose(board));

        // Build blocks to make it impossible to move
        board.getCell(2, 0).setHeight(2);
        board.getCell(2, 1).setHeight(2);

        printOccupiedCells();
        assertTrue(player1.checkLose(board));
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