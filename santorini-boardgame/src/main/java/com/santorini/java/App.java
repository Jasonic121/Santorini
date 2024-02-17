package com.santorini.java;

public class App 
{
    public static void main(String[] args)
    {
        Game game = new Game();

        // Setup the game (create the board and players, and place the workers on the board)
        game.setup();

        // Start the game
        game.startGame();

        // End the game
        game.endGame();
    }
}
