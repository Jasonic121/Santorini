package com.santorini;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.Map;

public class App extends NanoHTTPD {
    private static final int PORT_NUM = 8080;
    private static final int ROW_CELL = 5;
    private static final int BOARD_SIZE = 25;
    private int totalWorkersPlaced;
    private Game game;
    private Worker selectedWorker;
    
    public App() throws IOException {
        super(PORT_NUM); // Set the port number you want to use
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

        } else if (uri.equals("/godCardSelection")) {
            int playerIndex = Integer.parseInt(params.get("player"));
            String selectedCardName = params.get("card");
            game.selectGodCard(playerIndex, selectedCardName);
            System.out.println("gamePhase: " + game.getGamePhase());
            System.out.println("workerPhase: " + game.getWorkerPhase());
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
            }

        } else if (uri.equals("/selectedTargetCell")) {
            int x = Integer.parseInt(params.getOrDefault("x", "0"));
            int y = Integer.parseInt(params.getOrDefault("y", "0"));
        
            // Find the target cell at the specified position
            Cell targetCell = game.getBoard().getCell(x, y);
        
            // Perform the worker action
            if (selectedWorker != null && targetCell != null) {
                if (game.getWorkerPhase() == 0) {
                    // Move phase
                    System.out.println("\nExecuting move turn...");
        
                    // Check if the target cell is occupied by an opponent's worker
                    if (targetCell.isOccupied() && targetCell.getWorker().getOwner() != game.getCurrentPlayer()) {
                        // Check if the current player has the Minotaur god card
                        GodCard godCard = game.getCurrentPlayer().getGodCard();
                        if (godCard instanceof MinotaurGodCard) {
                            // Call the onMove method of the Minotaur god card to handle the forced move
                            godCard.onMove(game.getCurrentPlayer(), selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY(), game);
                        } else {
                            // If the player doesn't have the Minotaur god card, perform a regular move
                            game.executeMoveTurn(selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY());
                        }
                    } else {
                        // If the target cell is not occupied by an opponent's worker, perform a regular move
                        game.executeMoveTurn(selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY());
                    }
        
                    System.out.println("Completed Move!");
                    System.out.println("gamePhase: " + game.getGamePhase());
                    System.out.println("workerPhase: " + game.getWorkerPhase());
                } else {
                    // Build phase          
                    System.out.println("\nExecuting build turn...");
                    game.executeBuildTurn(selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY());
                    System.out.println("Completed Build!");
                }
        
                // Reset the selected worker and target cell
                targetCell = null;
            }
        } else if (uri.equals("/testLayout")) {
            String layout = params.get("layout");
            if (layout != null) {
                String[] testCells = layout.split(";");
                for (int i = 0; i < testCells.length; i += ROW_CELL) {
                    for (int j = i; j < i + ROW_CELL && j < testCells.length; j++) {
                        System.out.print(testCells[j] + ";");
                    }
                    System.out.println();
                }
                setupTestLayout(layout);
                System.out.println("Test layout dimensions: cell=" + testCells.length);
            }

        } else if (uri.equals("/pass")) {
            game.getCurrentPlayer().setBuildPoints(0);
            game.nextPlayer();
            game.setGamePhase(2);
            game.setWorkerPhase(0);
            game.setValidCells(new Cell[0]);
            System.out.println("Player passed the additional build");
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

    // Helper Classes
    private void setupTestLayout(String layout) {
        // Reset the game
        this.game = new Game();
        totalWorkersPlaced = ROW_CELL - 1;
        selectedWorker = null;
        int player1WorkerIDCounter = 0;
        int player2WorkerIDCounter = 0;
        // Parse the layout string and set up the board accordingly
        String[] testCells = layout.split(";");
        if (testCells.length != BOARD_SIZE + 1) {
            System.out.println("Invalid layout format: The board should have 26 cells");
            return;
        }
        for (int i = 0; i < ROW_CELL; i++) {
            for (int j = 0; j < ROW_CELL; j++) {
                String[] cellInfo = testCells[i * ROW_CELL + j].split(",");
                if (cellInfo.length != ROW_CELL - 2) {
                    System.out.println("Invalid layout format: Each cell should have 3 characters");
                    return;
                }
                int height = Integer.parseInt(cellInfo[0]);
                boolean hasDome = cellInfo[1].equals("1");
                int occupiedBy = Integer.parseInt(cellInfo[2]);
    
                Cell cell = game.getBoard().getCell(i, j);
                cell.setHeight(height);
                cell.setDome(hasDome);
    
                if (occupiedBy != -1) {
                    Player testPlayer = game.getPlayers().get(occupiedBy);
                    Worker testWorker;
                    if(occupiedBy == 0) {
                        testWorker = testPlayer.getWorkers()[player1WorkerIDCounter];
                        player1WorkerIDCounter++;
                    }
                    else {
                        testWorker = testPlayer.getWorkers()[player2WorkerIDCounter];
                        player2WorkerIDCounter++;
                    }
                    testWorker.setCurrentCell(cell);
                    cell.setWorker(testWorker);
                }
            }
        }
    
        // Set the current player based on the last character of the layout string
        if (layout.length() < testCells.length * ROW_CELL - 1) {
            System.out.println("Invalid layout format: Missing current player information");
            return;
        }
        int currentPlayerId = Character.getNumericValue(layout.charAt(layout.length() - 1));
        System.out.println("Current player: " + currentPlayerId);
        game.setCurrentPlayer(game.getPlayers().get(currentPlayerId));
    }

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
    
        // Update the valid cells based on the selected worker
        if (game.getCurrentPlayer().getMovePoints() > 0) {
            game.setValidCells(game.getBoard().validateCellsForMoving(worker.getCurrentCell()));
    
            // Check if the current player has the Minotaur god card
            GodCard godCard = game.getCurrentPlayer().getGodCard();
            if (godCard instanceof MinotaurGodCard) {
                // Call the onBeforeMove method of the Minotaur god card
                godCard.onBeforeMove(game.getCurrentPlayer(), worker.getWorkerId(), -1, -1, game);
            }
        } else if (game.getCurrentPlayer().getBuildPoints() > 0) {
            game.setValidCells(game.getBoard().validateCellsForBuilding(worker.getCurrentCell()));
        }
    
        game.setGamePhase(3);
        System.out.println("Valid worker selected!");
        return worker;
    }
    
    private boolean isCurrentPlayerWorker(Worker worker) {
        return worker.getOwner().getPlayerId() == game.getCurrentPlayer().getPlayerId();
    }
}