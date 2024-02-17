package com.santorini.java;

import java.util.ArrayList;

public class Game {
    private int currentPlayerIndex;
    private Board board;
    private ArrayList<Player> players;

    /* Game setup logic */
    public void setup() {
        // Initialize the board.
        this.board = new Board();

        // Initialize the players.
        this.players = new ArrayList<>();
        this.players.add(new Player(1));
        this.players.add(new Player(2));

        // Place the workers on the board.
        for (Player player : players) {
            Cell initialCell = new Cell(0, 0);
            Cell initialCell2 = new Cell(0, 1);
            player.placeWorkerOnBoard(0, initialCell);
            player.placeWorkerOnBoard(1, initialCell2);
        }

        // Initialize the current player index.
        this.currentPlayerIndex = 0; // Player A starts.

    }

    /* 
    
    Game start logic 
    
    */
    public void startGame() {

        // Game will end if endGame() returns true
        while (true) {
            
            // Get the current player
            Player currentPlayer = players.get(currentPlayerIndex);
            
            // Tests if the current player has lost
            if (currentPlayer.checkLose()) {
                System.out.println("Player " + currentPlayer.getPlayerId() + " has lost!");
                break;
            }

            // Player takes their turn 
            takeTurn(currentPlayer);

            // Toggle to the next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    /* 
    
    Take turn 
    
    */
    private void takeTurn(Player currentPlayer) {

        // Next Player's turn
        currentPlayer.resetActionPoints();

        // Assuming player has to finish move to build
        while (true) {
            while (currentPlayer.checkMovePointsAvailable()) {
                currentPlayer.moveWorker(0, new Cell(1, 0));
            } 
            while (currentPlayer.checkBuildPointsAvailable()) {
                currentPlayer.build(0, new Cell(1, 1));
            }
        }
    }

    /* 
    
    Game end logic 
    
    */
    public void endGame() {
        System.out.println("Game over.");
    }

}