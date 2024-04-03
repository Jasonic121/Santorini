package com.santorini;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents the state of a game.
 * This class stores information about the cells, winner, current player, next move
 */
public class GameState {
    private Cell[] cells;
    private Player winner;
    private int currentPlayerId;
    private Game game;


    public GameState(Cell[] cells, Game game) {
        this.cells = cells;
        this.game = game;
        this.winner = null;
        this.currentPlayerId = game.getCurrentPlayer().getPlayerId();
    }

    /**
     * Creates a new GameState object for the given game.
     *
     * @param game the game for which to create the state
     * @return the new GameState object
     */
    public static GameState getGameState(Game game) {
        Cell[] cells = game.getBoard().getGrid();
        return new GameState(cells, game);
    }


    /** Getters and Setters **/
    /**
     * Returns the cells of the game state.
     *
     * @return an array of Cell objects representing the cells of the game state
     */
    public Cell[] getCells() {
        Cell[] gameCells = new Cell[25];
        Board board = game.getBoard();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Cell cell = board.getCell(i, j);
                int height = cell.getHeight();
                boolean hasDome = cell.hasDome();
                boolean isOccupied = cell.isOccupied();

                // Check which player occupies the cell
                Player player = null;
                for (Player p : game.getPlayers()) {
                    for (Cell c : p.getWorkerCells()) { // Fix: Use getWorkers() instead of getWorkerAmount()
                        if (c.equals(cell)) {
                            player = p;
                            break;
                        }
                    }
                }

                gameCells[5 * i + j] = new Cell(i, j, height, hasDome, isOccupied, player.getPlayerId());
            }
        }

        return gameCells;
    }

    /**
     * Returns the winner of the game state.
     *
     * @return the winner of the game state, or "null" if there is no winner
     */
    public String getWinner() {
        if (this.winner == null)
            return "null";
        else
            return this.winner.toString();
    }

    /**
     * Returns the current player in the game state.
     *
     * @return the current player in the game state
     */
    public int getCurrentPlayer() {
        return this.currentPlayerId;
    }

    /**
     * Returns the string representation of the GameState object in JSON format.
     *
     * @return the string representation of the GameState object
     */
    @Override
    public String toString() {
        return String.format(
            "{\"cells\":%s,\"winner\":\"%s\",\"currentPlayer\":\"%d\"}",
            Arrays.toString(this.cells), this.getWinner().toString(), this.getCurrentPlayer()
        );
    }
}