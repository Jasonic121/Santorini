package com.santorini;
import java.util.Scanner;
import org.json.JSONObject;
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
        game.setupInitialWorker(null, null, null, null);

        // Start the game
        game.startGame();

        // Convert and print the game state to JSON
        String gameStateJson = sendGameState(game);
        System.out.println(gameStateJson); // This will print the state to stdout, where Node.js can read it
    }

    // Send the game state to the frontend
    public static String sendGameState(Game game)
    {
        // Send the game state to the frontend
        GameState gameState = GameState.getGameState(game);
        return gameState.toString();
    }

}
