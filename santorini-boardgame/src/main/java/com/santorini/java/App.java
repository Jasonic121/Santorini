package com.santorini.java;

/**
 * The main class that represents the entry point of the Santorini board game application.
 */
public class App 
{
    public static void main(String[] args)
    {
        // Create a new game (add players and board to the game, sets the first player to start the game, and initializes the scanner for user input)
        Game game = new Game();

        // // Setup the game (place the initial workers on the board)
        // game.setupInitialWorker();

        // Start the game
        game.startGame();
    }
}
