package com.santorini.java;

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

    // Constructor (initializes the board and players array list, and sets the first player to start the game)
    public Game() {
        endGameFlag = false;
        board = new Board();
        players = new ArrayList<>();

        Player player1 = new Player(0);
        Player player2 = new Player(1);

        this.addPlayer(player1);
        this.addPlayer(player2);
        
        // Set which player starts the game
        currentPlayerIndex = 0; // Player 1 starts.
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
        
        while (!endGameFlag) {
            
            loseCondition();

            executeTurn(workerId, 1, 1, 1, 0); 

        }
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

        // Assuming player has to finish move to build
        while (currentPlayer.checkMovePointsAvailable()) {
            currentPlayer.moveWorker(workerId, board.getCell(moveX, moveY));
        }

        // Check if the game has ended
        winCondition();

        while (currentPlayer.checkBuildPointsAvailable()) {
            currentPlayer.build(workerId, board.getCell(buildX, buildY));
        }

        // Change player
        nextPlayer();
    }

    /**
     * Advances the game to the next player.
     * Updates the currentPlayerIndex and currentPlayer variables accordingly.
     */
    private void nextPlayer() {
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
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    /**
     * Getter Methods
     * --------------
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Board getBoard() {
        return board;
    }
    public boolean getEndGameFlag() {
        return endGameFlag;
    }
}