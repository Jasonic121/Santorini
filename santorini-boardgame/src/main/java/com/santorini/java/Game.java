package com.santorini.java;

import java.util.ArrayList;

public class Game {
    private int currentPlayerIndex;
@SuppressWarnings("unused")
    private Board board;
    private ArrayList<Player> players;
    private boolean isRunning;
    private Player currentPlayer;

    // Constructor (initializes the board and players array list, and sets the first player to start the game)
    public Game() {
        board = new Board();
        players = new ArrayList<>();

        // Set which player starts the game
        currentPlayerIndex = 0; // Player 1 starts.
    }

    /* 
    * Game setup logic 
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
        isRunning = true;
        currentPlayer = players.get(currentPlayerIndex);

        // isRunning determine how many turns the game will have
        while (isRunning) {
            
            // Tests if the current player has lost
            if (currentPlayer.checkLose()) {
                System.out.println("Player " + currentPlayer.getPlayerId() + " has lost!");
                break;
            }

            // Player takes their turn 
            takeTurn(currentPlayer);

            // Tests if the current player has won
            if (currentPlayer.checkWin()) {
                System.out.println("Player " + currentPlayer.getPlayerId() + " has won!");
                break;
            }

            // Toggles to the next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentPlayer = players.get(currentPlayerIndex);

            isRunning = false;
        }
    }

    /* 
    
    Take turn 
    
    */
    public void takeTurn(Player currentPlayer) {
        System.out.println("Player " + currentPlayer.getPlayerId() + "'s turn.");

        // Reset action points at the start of the turn
        currentPlayer.resetActionPoints();

        // Assuming player has to finish move to build
        while (currentPlayer.checkMovePointsAvailable()) {
            currentPlayer.moveWorker(0, new Cell(1, 0));
        } 
        while (currentPlayer.checkBuildPointsAvailable()) {
            currentPlayer.build(0, new Cell(1, 1));
        }
    }

    /* 
    
    Game end logic 
    
    */
    public void endGame() {
        System.out.println("Game over.");
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
}