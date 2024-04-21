package com.santorini;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.util.Duration;

public class App extends NanoHTTPD {
    private static final int PORT_NUM = 8080;
    private static final int ROW_CELL = 5;
    private static final int BOARD_SIZE = 25;
    private int totalWorkersPlaced;
    private Game game;
    private Worker selectedWorker;
    private MediaPlayer mediaPlayer;
    private MediaPlayer selectWorkerSound;
    private MediaPlayer selectCellSound;
    private MediaPlayer selectCardSound;
    private MediaPlayer buildSound;

    private boolean isMusicPlaying = true;

    public App() throws IOException {
        super(PORT_NUM); 
        game = new Game();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Server is running! Access it at http://localhost:8080/");
        playBackgroundMusic();
        initializeSoundEffects();
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

        switch (uri) {
            case "/newgame":
                return handleNewGame();
            case "/godCardSelection":
                return handleGodCardSelection(params);
            case "/setup":
                return handleSetup(params);
            case "/selectedWorker":
                return handleSelectedWorker(params);
            case "/selectedTargetCell":
                return handleSelectedTargetCell(params);
            case "/testLayout":
                return handleTestLayout(params);
            case "/pass":
                return handlePass();
            case "/toggleMusic":
                return handleToggleMusic();
            default:
                return handleDefaultResponse();
        }
    }

    private Response handleNewGame() {
        System.out.println("Starting a new game...");
        this.game = new Game();
        totalWorkersPlaced = 0;
        selectedWorker = null;
        return handleDefaultResponse();
    }

    private Response handleGodCardSelection(Map<String, String> params) {
        int playerIndex = Integer.parseInt(params.get("player"));
        String selectedCardName = params.get("card");
        game.selectGodCard(playerIndex, selectedCardName);
        selectCardSound.seek(Duration.ZERO);
        selectCardSound.play();
        return handleDefaultResponse();
    }

    private Response handleSetup(Map<String, String> params) {
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
        selectCellSound.seek(Duration.ZERO);
        selectCellSound.play();
        totalWorkersPlaced++;
        game.nextPlayer();
        return handleDefaultResponse();
    }

    private Response handleSelectedWorker(Map<String, String> params) {
        int x = Integer.parseInt(params.getOrDefault("x", "0"));
        int y = Integer.parseInt(params.getOrDefault("y", "0"));

        Worker worker = selectWorker(x, y);
        if (worker != null) {
            selectedWorker = worker;
        }
        return handleDefaultResponse();
    }

    private Response handleSelectedTargetCell(Map<String, String> params) {
        int x = Integer.parseInt(params.getOrDefault("x", "0"));
        int y = Integer.parseInt(params.getOrDefault("y", "0"));

        Cell targetCell = game.getBoard().getCell(x, y);


        if (selectedWorker != null && targetCell != null) {
            if (game.getWorkerPhase() == 0) {
                System.out.println("\nExecuting move turn...");
                handleMoveTurn(targetCell);
                System.out.println("Completed Move!");
                System.out.println("gamePhase: " + game.getGamePhase());
                System.out.println("workerPhase: " + game.getWorkerPhase());
                selectCellSound.seek(Duration.ZERO);
                selectCellSound.play();
            } else {
                System.out.println("\nExecuting build turn...");
                game.executeBuildTurn(selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY());
                System.out.println("Completed Build!");
                buildSound.seek(Duration.ZERO);
                buildSound.play();
            }
            targetCell = null;
        }
        return handleDefaultResponse();
    }

    private void handleMoveTurn(Cell targetCell) {
        if (targetCell.isOccupied() && targetCell.getWorker().getOwner() != game.getCurrentPlayer()) {
            GodCard godCard = game.getCurrentPlayer().getGodCard();
            if (godCard instanceof MinotaurGodCard) {
                godCard.onMove(game.getCurrentPlayer(), selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY(), game);
            } else {
                game.executeMoveTurn(selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY());
            }
        } else {
            game.executeMoveTurn(selectedWorker.getWorkerId(), targetCell.getX(), targetCell.getY());
        }
    }

    private Response handleTestLayout(Map<String, String> params) {
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
        return handleDefaultResponse();
    }

    private Response handlePass() {
        GodCard currentGodCard = game.getCurrentPlayer().getGodCard();
        if (currentGodCard != null) {
            currentGodCard.resetState();
        }
        game.nextPlayer();
        game.setGamePhase(2);
        game.setWorkerPhase(0);
        game.setValidCells(new Cell[0]);
        game.setIsSecondBuild(false);
        System.out.println("Player passed the additional build");
        return handleDefaultResponse();
    }

    private Response handleToggleMusic() {
        isMusicPlaying = !isMusicPlaying;
        if (isMusicPlaying) {
            mediaPlayer.play();
        } else {
            mediaPlayer.pause();
        }

        String json = "{\"isMusicPlaying\":" + isMusicPlaying + "}";
        Response response = newFixedLengthResponse(Response.Status.OK, "application/json", json);
        addCORSHeaders(response);
        return response;
    }

    private Response handleDefaultResponse() {
        GameState gameState = GameState.getGameState(this.game);
        String json = gameState.toString();
        Response response = newFixedLengthResponse(Response.Status.OK, "application/json", json);
        addCORSHeaders(response);
        return response;
    }

    private void addCORSHeaders(Response response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    }

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
        } else {
            selectWorkerSound.seek(Duration.ZERO);
            selectWorkerSound.play();
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

    private void playBackgroundMusic() {
        // Initialize the JavaFX toolkit
        new JFXPanel();
        Platform.runLater(() -> {
            String musicFile = "/sound/background.mp3";
            Media sound = new Media(getClass().getResource(musicFile).toExternalForm());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
            if (isMusicPlaying) {
                mediaPlayer.play();
            }
        });
    }

    private void initializeSoundEffects() {
        String selectWorkerSoundFile = "/sound/select_worker.mp3";
        String selectCellSoundFile = "/sound/select_cell.mp3";
        String selectCardSoundFile = "/sound/select_card.mp3";
        String buildSoundFile = "/sound/build.mp3";
        
        Media selectWorkerMedia = new Media(getClass().getResource(selectWorkerSoundFile).toExternalForm());
        Media selectCellMedia = new Media(getClass().getResource(selectCellSoundFile).toExternalForm());
        Media selectCardMedia = new Media(getClass().getResource(selectCardSoundFile).toExternalForm());
        Media buildMedia = new Media(getClass().getResource(buildSoundFile).toExternalForm());
        
        selectWorkerSound = new MediaPlayer(selectWorkerMedia);
        selectCellSound = new MediaPlayer(selectCellMedia);
        selectCardSound = new MediaPlayer(selectCardMedia);
        buildSound = new MediaPlayer(buildMedia);
    }
}