package com.santorini;

import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.Map;

public class App extends NanoHTTPD {
    private int totalWorkersPlaced;
    private Game game;
    private Worker selectedWorker;
    private Cell[] validCells;

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
            // Create a new game
            this.game = new Game();
            System.out.println("Game has started.");

            totalWorkersPlaced = 0;
            selectedWorker = null;
            validCells = null;

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
        } else if (uri.equals("/selectedWorker")) {
            int x = Integer.parseInt(params.getOrDefault("x", "0"));
            int y = Integer.parseInt(params.getOrDefault("y", "0"));
        
            Worker worker = selectWorker(x, y);
            if (worker != null) {
                selectedWorker = worker;
                System.out.println("Selected worker: " + selectedWorker.getWorkerId());
                
                // Determine the valid target cells for the selected worker
                if (game.getCurrentPlayer().getMovePoints() > 0) {
                    validCells = game.getBoard().validateCellsForMoving(selectedWorker.getCurrentCell());
                } else if (game.getCurrentPlayer().getBuildPoints() > 0) {
                    validCells = game.getBoard().validateCellsForBuilding(selectedWorker.getCurrentCell());
                }
                
                for(Cell cell : validCells) {
                    System.out.println("Valid cell: (" + cell.getX() + ", " + cell.getY() + ")");
                }
            }
            
        } else if (uri.equals("/selectedTargetCell")) {
            int x = Integer.parseInt(params.getOrDefault("x", "0"));
            int y = Integer.parseInt(params.getOrDefault("y", "0"));
            int workerPhase = Integer.parseInt(params.getOrDefault("workerphase", "0"));

            // Find the target cell at the specified position
            Cell targetCell = game.getBoard().getCell(x, y);
            System.out.println("Selected target cell: (" + x + ", " + y + ")");

            // Perform the worker action
            if (selectedWorker != null && targetCell != null) {
                System.out.println("Valid target cell selected!");
                if (workerPhase == 0) {

                    // Move phase
                    game.executeMoveTurn(selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY());
                    System.out.println("Worker " + selectedWorker.getWorkerId() + " moved to cell (" + x + ", " + y + ")!!!");

                    // Get the valid cells for building after moving
                    validCells = game.getBoard().validateCellsForBuilding(selectedWorker.getCurrentCell());
                    
                } else {
                    // Build phase
                    System.out.println("Worker " + selectedWorker.getWorkerId() + " building at cell (" + x + ", " + y + ")!!!");
                    game.executeBuildTurn(selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY());
                    selectedWorker = null; // The worker has completed its turn
                    System.out.println("Completed Build!");
                }
                
                // Reset the selected worker and target cell
                targetCell = null;
            }
        }

        // Generate the current game state
        GameState gameState = GameState.getGameState(this.game);
        String json = gameState.toString();
        
        // add the valid cells to json
        json = json.substring(0, json.length() - 1) + ",\"validCells\":";
        if (validCells == null) {
            json += "[]}";
        } else {
            json += "[";
            for (int i = 0; i < validCells.length; i++) {
                json += validCells[i].toString();
                if (i < validCells.length - 1) {
                    json += ",";
                }
            }
            json += "]}";
        }

        Response response = newFixedLengthResponse(Response.Status.OK, "application/json", json);

        // Set CORS headers
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    
        return response;
    }

    // Helper Classes
    private Worker selectWorker(int x, int y) {
        System.out.println("User selected a worker (" + x + ", " + y + ")");
    
        // Find the worker at the specified position
        Worker worker = game.findWorkerAtPosition(x, y);
    
        if (worker == null) {
            System.out.println("No worker found at the selected position.");
            return null;
        }
    
        // Check if the worker belongs to the current player
        if (!isCurrentPlayerWorker(worker)) {
            System.out.println("You can only choose a worker that belongs to you!");
            return null;
        }
    
        System.out.println("Valid worker selected!");
        return worker;
    }
    
    private boolean isCurrentPlayerWorker(Worker worker) {
        return worker.getOwner().getPlayerId() == game.getCurrentPlayer().getPlayerId();
    }
}
