package com.santorini.java;
import java.util.ArrayList;

public class Player {
    private int playerId;
    private ArrayList<Worker> workers;
    private int movePoints = 1;
    private int buildPoints = 1;

    public Player(int playerId) {
        this.playerId = playerId;
        this.workers = new ArrayList<Worker>();
        // Each player starts with two workers.
        this.workers.add(new Worker(this, 0));
        this.workers.add(new Worker(this, 1));
    }

    // Initial placement of workers
    public void placeWorkerOnBoard(int workerIndex, Cell initialCell) {
        workers.get(workerIndex).placeInitialWorker(initialCell);
    }

    // Move functionality
    public void moveWorker(int workerIndex, Cell destinationCell) {
        workers.get(workerIndex).moveWorkerToCell(destinationCell);
        movePoints--;
    }

    // Build functionality
    public void build(int workerIndex, Cell targetCell) {
        workers.get(workerIndex).buildAt(targetCell);
        buildPoints--;
    }

    // Check if a player has won
    public boolean checkWin() {
        final int winHeight = 3;
        for (Worker worker : workers) {
            if (worker.getCurrentCell().getHeight() == winHeight) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the player has lost the game.
     * A player loses the game if all their workers have no possible moves.
     *
     * @param board the game board
     * @return true if the player has lost, false otherwise
     */
    public boolean checkLose(Board board) {
        for (Worker worker : workers) {
            if (checkMovePossibilities(worker, board)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the move possibilities for a given worker.
     * 
     * @param worker the worker for which to check move possibilities
     * @return true if there are valid move possibilities, false otherwise
     */
    private boolean checkMovePossibilities(Worker worker, Board board) {
        final int boardLengthIndex = 4; 
        Cell targetCell;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Offset the current cell by i and j
                int targetX = worker.getCurrentCell().getX() + i;
                int targetY = worker.getCurrentCell().getY() + j;
                // Inside the board
                if (targetX >= 0 && targetX <= boardLengthIndex && targetY >= 0 && targetY <= boardLengthIndex) {
                    targetCell = board.getCell(targetX, targetY);
                    if (!targetCell.isOccupied() && Math.abs(targetCell.getHeight() - worker.getCurrentCell().getHeight()) <= 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
    Helper methods for reseting and checking if a player has move/build points available
    */

    // Reset action points
    public void resetActionPoints() {
        movePoints = 1;
        buildPoints = 1;
    }

    // Move points validation
    public boolean checkMovePointsAvailable() {
        // Implement logic to check if a move is available
        if (movePoints == 0) {
            return false;
        }
        return true;
    }

    // Build points validation
    public boolean checkBuildPointsAvailable() {
        // Implement logic to check if a build is available
        if (buildPoints == 0) {
            return false;
        }
        return true;
    }

    /*
    Getters for Player
    */
    public Cell getWorkerCurrentCell(int workerIndex) {
        return workers.get(workerIndex).getCurrentCell();
    }
    
    public int getWorkerAmount() {
        return workers.size();
    }

    public Worker getWorker(int workerIndex) {
        return workers.get(workerIndex);
    }

    public int getPlayerId() {
        return playerId;
    }
}

