package com.santorini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * The Game class represents a game of Santorini.
 * It manages the board, players, and game flow.
 */
public class Game {
    private static final int INITIAL_WORKER_COUNT = 4;
    private static final int TURN_PHASE = 3;

    private Board board;
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private Player currentPlayer;
    private boolean endGameFlag;
    private Player winner;
    private Cell[] validCells;
    private Map<String, GodCard> godCards;
    private int gamePhase;
    private int workerPhase;
    private int initialSetupCount = 0;
    private boolean isSecondBuild = false;
    private int cardCount = 0;

    // Constructor (initializes the board and players array list, and sets the first player to start the game)
    public Game() {
        endGameFlag = false;
        board = new Board();
        players = new ArrayList<>();

        // Create and add two players to the game
        players.add(new Player(0));
        players.add(new Player(1));

        // Set which player starts the game
        currentPlayerIndex = 0; // Player 1 starts.
        currentPlayer = players.get(currentPlayerIndex);
        
        validCells = new Cell[0];
        godCards = new HashMap<>();
        godCards.put("Demeter", new DemeterGodCard());
        godCards.put("Hephaestus", new HephaestusGodCard());
        godCards.put("Minotaur", new MinotaurGodCard());
        godCards.put("Pan", new PanGodCard());

        gamePhase = 0;
        workerPhase = 0;
        isSecondBuild = false;
    }

    /**
     * Selects a god card for a player.
     *
     * @param playerIndex      the index of the player
     * @param selectedCardName the name of the selected god card
     */
    public void selectGodCard(int playerIndex, String selectedCardName) {
        if (playerIndex >= 0 && playerIndex < players.size()) {
            Player player = players.get(playerIndex);
            if (selectedCardName.equals("Normal")) {
                System.out.println("Normal card selected: Player " + (playerIndex + 1));
            } else {
                GodCard selectedCard = GodCardFactory.createGodCard(selectedCardName);
                player.setGodCard(selectedCard);
                System.out.println("God card selected: Player " + (playerIndex + 1) + " - " + selectedCardName);
            }
            cardCount++;

            if (cardCount == 2) {
                gamePhase = 1;
                System.out.println("card count: " + cardCount);
                System.out.println("All players have selected their god cards.");
            } else {
                System.out.println("Awaiting other players to select god cards.");
            }
            nextPlayer();
        } else {
            System.out.println("Invalid player index: " + (playerIndex + 1));
        }
    }
    
    /**
     * Sets up the initial placement of a worker on the game board.
     * 
     * @param initialCell the initial cell where the worker will be placed
     * @param playerId the ID of the player placing the worker
     * @param workerIndex the index of the worker being placed
     */
    public void setupInitialWorker(Cell initialCell, int playerId, int workerIndex) {
        players.get(playerId).placeWorkerOnBoard(workerIndex, initialCell);
        System.out.println("Initial worker " + workerIndex + " placement has been set up for Player " + playerId + ".");
        initialSetupCount++;
        if (initialSetupCount == INITIAL_WORKER_COUNT) {
            gamePhase = 2;
        }
    }

    /**
     * Executes a turn for the current player in the game.
     * 
     * @param workerId the ID of the worker to be moved and built with
     * @param x the X-coordinate of the destination cell to move the worker to
     * @param y the Y-coordinate of the destination cell to move the worker to
     **/
    public void executeMoveTurn(int workerId, int x, int y) {
        System.out.println("Player " + currentPlayerIndex + "'s Move turn.");
        currentPlayer.resetActionPoints();
        validCells = this.board.validateCellsForMoving(currentPlayer.getWorker(workerId).getCurrentCell());
        if (currentPlayer.getGodCard() != null) {
            System.out.println("Moving god card: " + currentPlayer.getGodCard().getGodCardName());
            currentPlayer.getGodCard().onBeforeMove(currentPlayer, workerId, x, y, this);
            currentPlayer.getGodCard().onMove(currentPlayer, workerId, x, y, this);
            currentPlayer.getGodCard().onAfterMove(currentPlayer, workerId, x, y);
            System.out.println("Completed moving god card");
        } else {
            moveWorkerUntilPointsExhausted(workerId, x, y);
            System.out.println("Completed moving non-god card");
        }
        
        this.setValidCells(this.board.validateCellsForBuilding(currentPlayer.getWorker(workerId).getCurrentCell()));
        winCondition();
        loseCondition();
        gamePhase = TURN_PHASE;
        workerPhase = 1;
    }
    
    public void executeBuildTurn(int workerId, int x, int y) {
        System.out.println("Player " + currentPlayerIndex + "'s Build turn.");
        currentPlayer.resetActionPoints();
        validCells = this.board.validateCellsForBuilding(currentPlayer.getWorker(workerId).getCurrentCell());
        if (currentPlayer.getGodCard() != null) {
            System.out.println("Building god card: " + currentPlayer.getGodCard().getGodCardName());
            currentPlayer.getGodCard().onBeforeBuild(currentPlayer, workerId, x, y);
            currentPlayer.getGodCard().onBuild(currentPlayer, workerId, x, y, this);
            currentPlayer.getGodCard().onAfterBuild(currentPlayer, workerId, x, y);

            if (currentPlayer.checkBuildPointsAvailable()) {
                return;
            }
            System.out.println("Completed building god card");
        } else {
            buildUntilPointsExhausted(workerId, x, y);
            System.out.println("Completed building non-god card");

        }
    
        winCondition();
        loseCondition();
        nextPlayer();
        System.out.println("Moving to move turn\n");
        gamePhase = 2;
        workerPhase = 0;
        validCells = new Cell[0]; 
    }
    /**
     * Moves the specified worker until the current player's move points are exhausted.
     *
     * @param workerId the ID of the worker to move
     * @param moveX the X-coordinate of the destination cell
     * @param moveY the Y-coordinate of the destination cell
     */
    public void moveWorkerUntilPointsExhausted(int workerId, int moveX, int moveY) {
        while (currentPlayer.checkMovePointsAvailable()) {
            Cell targetCell = board.getCell(moveX, moveY);
            if (isValidCell(targetCell, validCells)) {
                currentPlayer.moveWorker(workerId, targetCell);
                validCells = this.board.validateCellsForBuilding(currentPlayer.getWorker(workerId).getCurrentCell());
            }
        }
    }

    /**
     * Builds on the specified cell until the current player's build points are exhausted.
     *
     * @param workerId the ID of the worker performing the build
     * @param buildX the x-coordinate of the cell to build on
     * @param buildY the y-coordinate of the cell to build on
     */
    public void buildUntilPointsExhausted(int workerId, int buildX, int buildY) {
        while (currentPlayer.checkBuildPointsAvailable()) {
            Cell targetCell = board.getCell(buildX, buildY);
            if (isValidCell(targetCell, validCells)) {
                currentPlayer.build(workerId, targetCell);
                validCells = this.board.validateCellsForBuilding(currentPlayer.getWorker(workerId).getCurrentCell());
            }
        }
    }

    /**
     * Checks if the specified cell is a valid cell to move or build on.
     *
     * @param targetCell the cell to check
     * @param validCells the array of valid cells
     * @return true if the cell is valid, false otherwise
     */
    private boolean isValidCell(Cell targetCell, Cell[] validCells) {
        for (Cell cell : validCells) {
            if (cell.getX() == targetCell.getX() && cell.getY() == targetCell.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Advances the game to the next player.
     * Updates the currentPlayerIndex and currentPlayer variables accordingly.
     */
    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
    }

    /**
     * Checks if the current player has won the game.
     * If the current player has won, it prints a message and ends the game.
     */
    private void winCondition() {
        if (currentPlayer.getGodCard() == null) {
            if (currentPlayer.checkWin()) {
                System.out.println("Player " + currentPlayer.getPlayerId() + " has won!");
                setWinner(currentPlayerIndex);
                endGame();
            }
            return;
        }
        if (currentPlayer.getGodCard().checkWinCondition(currentPlayer) || currentPlayer.checkWin()) {
            System.out.println("Player " + currentPlayer.getPlayerId() + " has won!");
            setWinner(currentPlayerIndex);
            endGame();
        }
    }

    /**
     * Checks if the current player has lost the game.
     * If the current player has lost, it prints a message and ends the game.
     */
    private void loseCondition() {
        if (currentPlayer.checkLose(board)) {
            System.out.println("Player " + currentPlayer.getPlayerId() + " has lost!");
            setWinner(currentPlayerIndex == 0 ? 1 : 0);
            endGame();
        }
    }


    /**
     * Ends the game and sets the endGameFlag to true.
     */
    private void endGame() {
        System.out.println("Game over.");
        endGameFlag = true;
    }

    /**
     * Finds the worker at the specified position on the game board.
     *
     * @param x the X-coordinate of the position
     * @param y the Y-coordinate of the position
     * @return the worker at the specified position, or null if no worker is found
     */
    public Worker findWorkerAtPosition(int x, int y) {
        for (Player player : players) {
            for (Worker worker : player.getWorkers()) {
                Cell workerCell = worker.getCurrentCell();
                if (workerCell != null && workerCell.getX() == x && workerCell.getY() == y) {
                    return worker;
                }
            }
        }
        return null;
    }

    /**
     * Getter Methods
     * --------------
     */
    /**
     * Get isSecondBuild
     * 
     * @return the isSecondBuild
     */
    public boolean getIsSecondBuild() {
        return isSecondBuild;
    }

    /** 
     * Set isSecondBuild
     * 
     * @param isSecondBuild the isSecondBuild to set
     */
    public void setIsSecondBuild(boolean isSecondBuild) {
        this.isSecondBuild = isSecondBuild;
    }


    /**
     * Set game phase
     * 
     * @param gamePhase the game phase to set
     */
    public void setGamePhase(int gamePhase) {
        this.gamePhase = gamePhase;
    }
    /**
     * Set worker phase
     * 
     * @param workerPhase the worker phase to set
     */
    public void setWorkerPhase(int workerPhase) {
        this.workerPhase = workerPhase;
    }
    /**
     * Get game phase
     * 
     * @return the game phase
     */
    public int getGamePhase() {
        return gamePhase;
    }

    /**
     * Get worker phase
     * 
     * @return the worker phase
     */
    public int getWorkerPhase() {
        return workerPhase;
    }

    /**
     * Sets the current player in the game.
     * @param player the player to set as the current player
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        this.currentPlayerIndex = players.indexOf(player);
    }
    /**
     * Returns the list of players in the game.
     * @return the list of players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the current player in the game.
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the next player in the game.
     * @return the next player
     */
    public Player getNextPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    /**
     * Returns the game board.
     * @return the game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the endGameFlag.
     * @return the endGameFlag
     */
    public boolean getEndGameFlag() {
        return endGameFlag;
    }
    
    /**
     * Sets the winner of the game.
     * @param playerId the ID of the player who won the game
     */
    public void setWinner(int playerId) {
        winner = players.get(playerId);
    }

    /**
     * Returns the valid cells for the current player.
     * @return the valid cells for the current player
     */
    public Cell[] getValidCells() {
        return validCells;
    }

    /**
     * Remove one of the valid cells from the list of valid cells 
     * 
     * @param targetCell the cell to remove from the list of valid cells
     */
    public void removeValidCells(Cell targetCell) {
        // Temporary collection to hold cells to be kept
        List<Cell> tempCells = new ArrayList<>();
        
        // Collect cells to keep
        for (Cell cell : validCells) {
            if (!cell.equals(targetCell)) {
                tempCells.add(cell);
            }
        }
    
        // Replace validCells with the new smaller array
        validCells = tempCells.toArray(new Cell[tempCells.size()]);
    }

    /**
     * Add one cell to the valid cells
     * 
     * @param cell the cell to add to the list of valid cells
     */
    public void addValidCells(Cell cell) {
        // Temporary collection to hold cells to be kept
        List<Cell> tempCells = new ArrayList<>();
        
        // Collect cells to keep
        for (Cell c : validCells) {
            tempCells.add(c);
        }
        tempCells.add(cell);
    
        // Replace validCells with the new smaller array
        validCells = tempCells.toArray(new Cell[tempCells.size()]);
    }

    /**
     * Returns the winner of the game.
     * @return the winner of the game
    */
    public Player getWinner() {
        return winner;
    }
    
    /**
     * Returns the ID of the winner of the game.
     * @return the ID of the winner of the game
     */
    public int getWinnerId() {
        return winner != null ? winner.getPlayerId() : -1;
    }

    /**
     * Sets the valid cells for the current player.
     * 
     * @param validCells the valid cells to set
     */
    public void setValidCells(Cell[] validCells) {
        this.validCells = validCells;
    }
}
