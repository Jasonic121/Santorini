package com.santorini;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.Map;

public class App extends NanoHTTPD {
    private int totalWorkersPlaced;
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
            // Start the game
            game.startGame();
            totalWorkersPlaced = 0;
            
        } else if (uri.equals("/setup")) {
            String cellCoords = params.get("cell1");
            if (cellCoords == null) {
                cellCoords = params.get("cell2");
            }
            if (cellCoords == null) {
                cellCoords = params.get("cell3");
            }
            if (cellCoords == null) {
                cellCoords = params.get("cell4");
            }
        
            String[] cellArray = cellCoords.split(",");
            int cellX = Integer.parseInt(cellArray[0]);
            int cellY = Integer.parseInt(cellArray[1]);
        
            int playerId = totalWorkersPlaced % 2;
            int workerIndex = totalWorkersPlaced / 2;
        
            game.setupInitialWorker(game.getBoard().getCell(cellX, cellY), playerId, workerIndex);
        
            totalWorkersPlaced++;
            game.nextPlayer();
        } else if (uri.equals("/play")) {
            int x = Integer.parseInt(params.getOrDefault("x", "0"));
            int y = Integer.parseInt(params.getOrDefault("y", "0"));
            System.out.println("User clicked on cell (" + x + ", " + y + ")");
            // Get the current player
            Player currentPlayer = game.getCurrentPlayer();

            // Find the worker at the specified position
            Worker worker = null;
            for (int i = 0; i < currentPlayer.getWorkerAmount(); i++) {
                Cell workerCell = currentPlayer.getWorkerCurrentCell(i);
                System.out.println("Worker " + i + " current cell: " + workerCell);
                if (workerCell != null && workerCell.getX() == x && workerCell.getY() == y) {
                    worker = currentPlayer.getWorker(i);
                    break;
                } else {
                    System.out.println("Click on a cell with your worker!");
                }
}

            if (worker != null) { // Worker found where the user clicked
                System.out.println("Worker " + worker.getWorkerId() + " found at cell (" + x + ", " + y + ")");
                // // Move the worker to the specified position
                // currentPlayer.moveWorker(worker.getWorkerId(), game.getBoard().getCell(x, y));

                // // Check if the game has ended
                // if (currentPlayer.checkWin()) {
                //     game.setWinner(currentPlayer);
                // } else if (currentPlayer.checkLose(game.getBoard())) {
                //     game.setWinner(game.getNextPlayer());
                // } else {
                //     // Switch to the next player
                //     game.nextPlayer();
                // }
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
