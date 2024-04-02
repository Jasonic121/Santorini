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
    }

    /**
     * Sets up the initial worker placement on the board.
     * 
     * @param initialCell  the initial cell for the first player's first worker
     * @param initialCell2 the initial cell for the first player's second worker
     * @param initialCell3 the initial cell for the second player's first worker
     * @param initialCell4 the initial cell for the second player's second worker
     */
    public void setupInitialWorker(Cell initialCell, Cell initialCell2, Cell initialCell3, Cell initialCell4) {
        // Place the workers on the board.
        players.get(0).placeWorkerOnBoard(0, initialCell);
        players.get(0).placeWorkerOnBoard(1, initialCell2);
        players.get(1).placeWorkerOnBoard(0, initialCell3);
        players.get(1).placeWorkerOnBoard(1, initialCell4);
    }

    /**
     * Starts the game and executes turns until the game ends.
     */
    public void startGame() {
        currentPlayer = players.get(currentPlayerIndex);
        setupInitialWorker(board.getCell(0, 0), board.getCell(0, 1), board.getCell(1, 0), board.getCell(1, 1));
        System.out.println("Game has started.");

        // while (!endGameFlag) {
            // executeTurn(workerId, 2, 1, 1, 0); 
            // loseCondition();
        // }
    }

    /**
     * Executes a turn for the current player in the game.
     * 
     * @param workerId the ID of the worker to be moved and built with
     * @param moveX the X-coordinate of the cell to move the worker to
     * @param moveY the Y-coordinate of the cell to move the worker to
     * @param buildX the X-coordinate of the cell to build on
     * @param buildY the Y-coordinate of the cell to build on
     */
    public void executeTurn(int workerId, int moveX, int moveY, int buildX, int buildY) {
        System.out.println("Player " + currentPlayerIndex + "'s turn.");
        currentPlayer = players.get(currentPlayerIndex);

        // Reset action points at the start of the turn
        currentPlayer.resetActionPoints();

        // Move worker until move points are exhausted
        moveWorkerUntilPointsExhausted(workerId, moveX, moveY);

        // Check if the game has ended
        winCondition();

        // Build until build points are exhausted
        buildUntilPointsExhausted(workerId, buildX, buildY);

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
            currentPlayer.moveWorker(workerId, board.getCell(moveX, moveY));
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
}