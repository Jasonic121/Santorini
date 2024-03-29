package com.santorini.java;
import java.util.ArrayList;

/**
 * Represents a player in the Santorini board game.
 */
public class Player {
    private int playerId;
    private ArrayList<Worker> workers;
    private int movePoints = 1;
    private int buildPoints = 1;

    /**
     * Constructs a new player with the given player ID.
     * Initializes the player with two workers.
     *
     * @param playerId the ID of the player
     */
    public Player(int playerId) {
        this.playerId = playerId;
        this.workers = new ArrayList<Worker>();
        // Each player starts with two workers.
        this.workers.add(new Worker(this, 0));
        this.workers.add(new Worker(this, 1));
    }

    /**
     * Places a worker on the game board at the specified initial cell.
     *
     * @param workerIndex the index of the worker to be placed
     * @param initialCell the initial cell where the worker will be placed
     */
    public void placeWorkerOnBoard(int workerIndex, Cell initialCell) {
        workers.get(workerIndex).placeInitialWorker(initialCell);
    }

    /**
     * Moves the specified worker to the given destination cell.
     * Decreases the move points of the player.
     *
     * @param workerIndex the index of the worker to be moved
     * @param destinationCell the cell where the worker will be moved to
     */
    public void moveWorker(int workerIndex, Cell destinationCell) {
        workers.get(workerIndex).moveWorkerToCell(destinationCell);
        movePoints--;
    }

    /**
     * Builds a structure on the specified target cell using the worker at the given index.
     * Decreases the build points by one.
     *
     * @param workerIndex the index of the worker to perform the build action
     * @param targetCell  the cell on which to build the structure
     */
    public void build(int workerIndex, Cell targetCell) {
        getWorker(workerIndex).buildAt(targetCell);
        buildPoints--;
    }

    /**
     * Checks if any of the player's workers have reached the win height.
     * The win height is defined as a height of 3.
     *
     * @return true if any worker has reached the win height, false otherwise.
     */
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

    /**
     * Helper Methods
     * --------------
     */

    /**
     * Resets the action points of the player.
     * The player's move points and build points are set to 1.
     */
    public void resetActionPoints() {
        movePoints = 1;
        buildPoints = 1;
    }

    /**
     * Checks if the player has move points available.
     * 
     * @return true if move points are available, false otherwise.
     */
    public boolean checkMovePointsAvailable() {
        // Implement logic to check if a move is available
        if (movePoints == 0) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the player has build points available.
     * 
     * @return true if build points are available, false otherwise.
     */
    public boolean checkBuildPointsAvailable() {
        // Implement logic to check if a build is available
        if (buildPoints == 0) {
            return false;
        }
        return true;
    }

    /**
     * Getter Methods
     * --------------
     */

    /**
     * Returns the current cell of the specified worker.
     *
     * @param workerIndex the index of the worker
     * @return the current cell of the worker
     */
    public Cell getWorkerCurrentCell(int workerIndex) {
        return workers.get(workerIndex).getCurrentCell();
    }
    
    public int getWorkerAmount() {
        return workers.size();
    }

    /**
     * Retrieves the worker at the specified index.
     *
     * @param workerIndex the index of the worker to retrieve
     * @return the worker at the specified index
     */
    public Worker getWorker(int workerIndex) {
        return workers.get(workerIndex);
    }

    public int getPlayerId() {
        return playerId;
    }
}

