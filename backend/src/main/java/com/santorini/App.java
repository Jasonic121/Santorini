package com.santorini;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.Map;

public class App extends NanoHTTPD {

    private Game game;

    public App() throws IOException {
        super(8080); // Set the port number you want to use
        game = new Game();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Server is running! Access it at http://localhost:8080/");
    }

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException e) {
            System.err.println("Server could not start:");
            e.printStackTrace();
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();

        // Check what type of action is being requested
        if (uri.equals("/newgame")) {
            System.out.println("Game has been reset");
            this.game = new Game();
            // // Setup initial worker positions
            // game.setupInitialWorker(game.getBoard().getCell(2, 0), game.getBoard().getCell(0, 1),
            //         game.getBoard().getCell(1, 0), game.getBoard().getCell(1, 1));
            
            // Start the game
            game.startGame();
            
        } else if (uri.equals("/play")) {
            int x = Integer.parseInt(params.getOrDefault("x", "0"));
            int y = Integer.parseInt(params.getOrDefault("y", "0"));

            // Get the current player
            Player currentPlayer = game.getCurrentPlayer();

            // Find the worker at the specified position
            Worker worker = null;
            for (int i = 0; i < currentPlayer.getWorkerAmount(); i++) {
                if (currentPlayer.getWorkerCurrentCell(i).getX() == x && currentPlayer.getWorkerCurrentCell(i).getY() == y) {
                    worker = currentPlayer.getWorker(i);
                    break;
                }
            }

            if (worker != null) {
                // Move the worker to the specified position
                currentPlayer.moveWorker(worker.getWorkerId(), game.getBoard().getCell(x, y));

                // Check if the game has ended
                if (currentPlayer.checkWin()) {
                    game.setWinner(currentPlayer);
                } else if (currentPlayer.checkLose(game.getBoard())) {
                    game.setWinner(game.getNextPlayer());
                } else {
                    // Switch to the next player
                    game.nextPlayer();
                }
            }
        }
        // Generate the current game state
        GameState gameState = GameState.getGameState(this.game);
        String json = gameState.toString();
        Response response = newFixedLengthResponse(Response.Status.OK, "application/json", json);

        // Set CORS headers
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    
        return response;
    }
}
