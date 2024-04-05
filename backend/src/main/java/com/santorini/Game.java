package com.santorini;

import java.util.ArrayList;

/**
 * The Game class represents a game of Santorini.
 * It manages the board, players, and game flow.
 */
public class Game {
    private Board board;
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private Player currentPlayer;
    private boolean endGameFlag;
    private Player winner;
    private Cell[] validCells;

    // Constructor (initializes the board and players array list, and sets the first player to start the game)
    public Game() {
        endGameFlag = false;
        board = new Board();
        players = new ArrayList<>();

        // Create and add two players to the game
        players.add(new Player(0));
        players.add(new Player(1));

        // Set which player starts the game
        currentPlayerIndex = 0; // Player 1 starts.
        currentPlayer = players.get(currentPlayerIndex);
        
        validCells = null;
    }

    /**
     * Sets up the initial placement of a worker on the game board.
     * 
     * @param initialCell the initial cell where the worker will be placed
     * @param playerId the ID of the player placing the worker
     * @param workerIndex the index of the worker being placed
     */
    public void setupInitialWorker(Cell initialCell, int playerId, int workerIndex) {
        players.get(playerId).placeWorkerOnBoard(workerIndex, initialCell);
        System.out.println("Initial worker " + workerIndex + " placement has been set up for Player " + playerId + ".");
    }

    /**
     * Executes a turn for the current player in the game.
     * 
     * @param workerId the ID of the worker to be moved and built with
     * @param x the X-coordinate of the destination cell to move the worker to
     * @param y the Y-coordinate of the destination cell to move the worker to
     **/
    public void executeMoveTurn(int workerId, int x, int y) {
        System.out.println("Player " + currentPlayerIndex + "'s Move turn.");
        currentPlayer.resetActionPoints();

        validCells = this.board.validateCellsForMoving(currentPlayer.getWorker(workerId).getCurrentCell());
        moveWorkerUntilPointsExhausted(workerId, x, y);
        winCondition();
    }

    public void executeBuildTurn(int workerId, int x, int y) {
        System.out.println("Player " + currentPlayerIndex + "'s Build turn.");
        currentPlayer.resetActionPoints();

        validCells = this.board.validateCellsForBuilding(currentPlayer.getWorker(workerId).getCurrentCell());
        buildUntilPointsExhausted(workerId, x, y);
        winCondition();
        nextPlayer();
    }

    /**
     * Moves the specified worker until the current player's move points are exhausted.
     *
     * @param workerId the ID of the worker to move
     * @param moveX the X-coordinate of the destination cell
     * @param moveY the Y-coordinate of the destination cell
     */
    public void moveWorkerUntilPointsExhausted(int workerId, int moveX, int moveY) {
        while (currentPlayer.checkMovePointsAvailable()) {
            Cell targetCell = board.getCell(moveX, moveY);
            if (isValidCell(targetCell, validCells)) {
                currentPlayer.moveWorker(workerId, targetCell);
            }
        }
    }

    /**
     * Builds on the specified cell until the current player's build points are exhausted.
     *
     * @param workerId the ID of the worker performing the build
     * @param buildX the x-coordinate of the cell to build on
     * @param buildY the y-coordinate of the cell to build on
     */
    public void buildUntilPointsExhausted(int workerId, int buildX, int buildY) {
        while (currentPlayer.checkBuildPointsAvailable()) {
            Cell targetCell = board.getCell(buildX, buildY);
            if (isValidCell(targetCell, validCells)) {
                currentPlayer.build(workerId, targetCell);
            }
        }
    }

    /**
     * Checks if the specified cell is a valid cell to move or build on.
     *
     * @param targetCell the cell to check
     * @param validCells the array of valid cells
     * @return true if the cell is valid, false otherwise
     */
    private boolean isValidCell(Cell targetCell, Cell[] validCells) {
        for (Cell cell : validCells) {
            if (cell.getX() == targetCell.getX() && cell.getY() == targetCell.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Advances the game to the next player.
     * Updates the currentPlayerIndex and currentPlayer variables accordingly.
     */
    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
    }

    /**
     * Checks if the current player has won the game.
     * If the current player has won, it prints a message and ends the game.
     */
    private void winCondition() {
        if (currentPlayer.checkWin()) {
            System.out.println("Player " + currentPlayer.getPlayerId() + " has won!");
            setWinner(currentPlayer.getPlayerId());
            endGame();
        }
    }

    /**
     * Ends the game and sets the endGameFlag to true.
     */
    private void endGame() {
        System.out.println("Game over.");
        endGameFlag = true;
    }

    /**
     * Finds the worker at the specified position on the game board.
     *
     * @param x the X-coordinate of the position
     * @param y the Y-coordinate of the position
     * @return the worker at the specified position, or null if no worker is found
     */
    public Worker findWorkerAtPosition(int x, int y) {
        for (Player player : players) {
            for (Worker worker : player.getWorkers()) {
                Cell workerCell = worker.getCurrentCell();
                if (workerCell != null && workerCell.getX() == x && workerCell.getY() == y) {
                    System.out.println("Worker found at position (" + x + ", " + y + ")");
                    return worker;
                }
            }
        }
        return null;
    }

    /**
     * Getter Methods
     * --------------
     */

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNextPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    public Board getBoard() {
        return board;
    }

    public boolean getEndGameFlag() {
        return endGameFlag;
    }

    public void setWinner(int playerId) {
        winner = players.get(playerId);
    }

    public Cell[] getValidCells() {
        return validCells;
    }

    public Player getWinner() {
        return winner;
    }

    public int getWinnerId() {
        return winner != null ? winner.getPlayerId() : -1;
    }
}