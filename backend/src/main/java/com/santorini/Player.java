// Player.java
package com.santorini;

/**
 * Represents a player in the Santorini board game.
 */
public class Player {
    private int playerId;
    private Worker[] workers;
    private int movePoints = 1;
    private int buildPoints = 1;
    private GodCard godCard;    
    /**
     * Constructs a new player with the given player ID.
     * Initializes the player with two workers.
     *
     * @param playerId the ID of the player
     */
    public Player(int playerId) {
        this.playerId = playerId;
        this.godCard = godCard;
        this.workers = new Worker[2];
        this.workers[0] = new Worker(this, 0);
        this.workers[1] = new Worker(this, 1);
    }

    /**
     * Places a worker on the game board at the specified initial cell.
     *
     * @param workerIndex the index of the worker to be placed
     * @param initialCell the initial cell where the worker will be placed
     */
    public void placeWorkerOnBoard(int workerIndex, Cell initialCell) {
        workers[workerIndex].placeInitialWorker(initialCell);
    }

    /**
     * Moves the specified worker to the given destination cell.
     * Decreases the move points of the player.
     * 
     * @param workerIndex the index of the worker to be moved
     * @param destinationCell the cell where the worker will be moved to
     */
    public void moveWorker(int workerIndex, Cell destinationCell) {
        workers[workerIndex].moveWorkerToCell(destinationCell);
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
        Cell currentCell = worker.getCurrentCell();
        Cell[] validCells = board.validateCellsForMoving(currentCell);
        return validCells.length > 0;
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
        return movePoints > 0;
    }

    /**
     * Checks if the player has build points available.
     * 
     * @return true if build points are available, false otherwise.
     */
    public boolean checkBuildPointsAvailable() {
        return buildPoints > 0;
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
        return workers[workerIndex].getCurrentCell();
    }
    
    /**
     * Returns the number of workers the player has.
     *
     * @return the number of workers the player has
     */
    public int getWorkerCount() {
        return workers.length;
    }

    /**
     * Retrieves the worker at the specified index.
     *
     * @param workerIndex the index of the worker to retrieve
     * @return the worker at the specified index
     */
    public Worker getWorker(int workerIndex) {
        return workers[workerIndex];
    }

    /**
     * Returns an array of the player's workers.
     *
     * @return an array of the player's workers
     */
    public Worker[] getWorkers() {
        return workers;
    }

    /**
     * Returns an array of cells occupied by the player's workers.
     *
     * @return an array of cells occupied by the player's workers
     */
    public Cell[] getWorkerCells() {
        Cell[] workerCells = new Cell[workers.length];
        for (int i = 0; i < workers.length; i++) {
            workerCells[i] = workers[i].getCurrentCell();
        }
        return workerCells;
    }

    /**
     * Returns the player's ID.
     *
     * @return the player's ID
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Returns the player's remaining move points.
     *
     * @return the player's remaining move points
     */
    public int getMovePoints() {
        return movePoints;
    }

    /**
     * Returns the player's remaining build points.
     *
     * @return the player's remaining build points
     */
    public int getBuildPoints() {
        return buildPoints;
    }
    
    /**
     * Returns the GodCard associated with this player.
     *
     * @return the GodCard associated with this player
     */
    public GodCard getGodCard() {
        return godCard;
    }

    /**
     * Sets the God card for the player.
     * 
     * @param godCard the God card to set
     */
    public void setGodCard(GodCard godCard) {
        this.godCard = godCard;
    }

    /** 
     * Sets the player's move points.
     * 
     * @param movePoints the move points to set
     */
    public void setMovePoints(int movePoints) {
        this.movePoints = movePoints;
    }

    /**
     * Sets the player's build points.
     * 
     * @param buildPoints the build points to set
     */
    public void setBuildPoints(int buildPoints) {
        this.buildPoints = buildPoints;
    }
}