package com.santorini.java;

import java.util.ArrayList;

public class Game {
    private Board board;
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private Player currentPlayer;
    private boolean endGameFlag;

    /* Input parameters for test */
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

    /* 
    * Worker Initial setup logic 
    */
    public void setupInitialWorker(Cell initialCell, Cell initialCell2, Cell initialCell3, Cell initialCell4) {
        // Place the workers on the board.
        players.get(0).placeWorkerOnBoard(0, initialCell);
        players.get(0).placeWorkerOnBoard(1, initialCell2);
        players.get(1).placeWorkerOnBoard(0, initialCell3);
        players.get(1).placeWorkerOnBoard(1, initialCell4);
    }

    /*
    * Game start logic
    */ 
    public void startGame() {
        currentPlayer = players.get(currentPlayerIndex);
        
        while (!endGameFlag) {
            
            loseCondition();

            executeTurn(workerId, 1, 1, 1, 0); 

        }
    }

    /* 
     * Execute turn logic
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

    /*
     * Change player
     */
    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
    }

    /*
     * Detect Winning and Losing conditions
     */
    // Lose if the current player has lost by not being able to move
    private void loseCondition() {
        if (currentPlayer.checkLose(board)) {
            System.out.println("Player " + currentPlayer.getPlayerId() + " has lost!");
            endGame();
        }
    }
    // Win if the current player has won by reaching the third level
    private void winCondition() {
        if (currentPlayer.checkWin()) {
        System.out.println("Player " + currentPlayer.getPlayerId() + " has won!");
        endGame();
        }
    }

    /* 
    
    Game end logic 
    
    */
    private void endGame() {
        System.out.println("Game over.");
        endGameFlag = true;
    }

    /*
     * Setters 
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /*
     * Getters 
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