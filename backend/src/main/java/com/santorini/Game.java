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
    private int workerId = 0;
    private Player winner;
    private Cell[] validCells;

    // Constructor (initializes the board and players array list, and sets the first player to start the game)
    public Game() {
        endGameFlag = false;
        board = new Board();
        players = new ArrayList<>();

        // Create and add two players to the game
        Player player1 = new Player(0);
        Player player2 = new Player(1);
        players.add(player1);
        players.add(player2);

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
        currentPlayer = players.get(currentPlayerIndex);

        // Reset action points at the start of the turn
        currentPlayer.resetActionPoints();

        // System.out.println("Game.java executeMoveTurn: Moving from " + currentPlayer.getWorker(workerId).getCurrentCell().getX() + ", " + currentPlayer.getWorker(workerId).getCurrentCell().getY()  + " to cell " + x + ", " + y);
        validCells = this.board.validateCellsForMoving(currentPlayer.getWorker(workerId).getCurrentCell());

        // Move worker until move points are exhausted
        moveWorkerUntilPointsExhausted(workerId, x, y);

        // Check if the game has ended
        winCondition();
    }

    public void executeBuildTurn(int workerId, int x, int y) {
        System.out.println("Player " + currentPlayerIndex + "'s Build turn.");

        // System.out.println("Game.java executeBuildTurn: Building at cell " + x + ", " + y);

        currentPlayer = players.get(currentPlayerIndex);

        // Reset action points at the start of the turn
        currentPlayer.resetActionPoints();

        validCells = this.board.validateCellsForBuilding(currentPlayer.getWorker(workerId).getCurrentCell());

        // Build until build points are exhausted
        buildUntilPointsExhausted(workerId, x, y);

        // Check if the game has ended
        winCondition();

        // Change player
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

            validCells = this.board.validateCellsForMoving(currentPlayer.getWorker(workerId).getCurrentCell());

            // Validate whether moveX and moveY cell is a valid cell to move to
            for (Cell cell : validCells) {
                if (cell.getX() == moveX && cell.getY() == moveY) {
                    currentPlayer.moveWorker(workerId, board.getCell(moveX, moveY));
                    break;
                }
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
            // System.out.println("Game.java buildUntilPointsExhausted: Worker is at cell " + currentPlayer.getWorker(workerId).getCurrentCell().getX() + ", " + currentPlayer.getWorker(workerId).getCurrentCell().getY() + " and building at cell " + buildX + ", " + buildY);
            currentPlayer.build(workerId, board.getCell(buildX, buildY));
        }
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
     * Checks if the current player has lost the game.
     * If the current player has lost, it prints a message and ends the game.
     */
    private void loseCondition() {
        if (currentPlayer.checkLose(board)) {
            System.out.println("Player " + currentPlayer.getPlayerId() + " has lost!");
            endGame();
        }
    }

    /**
     * Checks if the current player has won the game.
     * If the current player has won, it prints a message and ends the game.
     */
    private void winCondition() {
        if (currentPlayer.checkWin()) {
        System.out.println("Player " + currentPlayer.getPlayerId() + " has won!");
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
     * Performs the worker action (move and build) at the specified position.
     *
     * @param worker the worker performing the action
     * @param x the X-coordinate of the position
     * @param y the Y-coordinate of the position
     */
    public void performWorkerAction(Worker worker, int x, int y) {
        // Move the worker to the specified position
        currentPlayer.moveWorker(worker.getWorkerId(), board.getCell(x, y));

        // Check if the game has ended
        if (currentPlayer.checkWin()) {
            setWinner(currentPlayer);
        } else if (currentPlayer.checkLose(board)) {
            setWinner(getNextPlayer());
        } else {
            // Switch to the next player
            nextPlayer();
        }
    }

    /**
     * Setter Methods
     * --------------
     */
    
    /**
     * Adds a player to the game.
     * 
     * @param player the player to be added
     */
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    /**
     * Getter Methods
     * --------------
     */

    /**
     * Returns the list of players in the game.
     *
     * @return the list of players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
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

    public void setWinner(Player currentPlayer2) {
        winner = currentPlayer2;
    }

    public Cell[] getValidCells() {
        return validCells;
    }
}