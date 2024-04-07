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
    private Cell[] validCells;


    public GameState(Cell[] cells, Game game, Cell[] validCells) {
        this.cells = cells;
        this.game = game;
        this.winner = null;
        this.currentPlayerId = game.getCurrentPlayer().getPlayerId();
        this.validCells = validCells;
    }

    /**
     * Creates a new GameState object for the given game.
     *
     * @param game the game for which to create the state
     * @return the new GameState object
     */
    public static GameState getGameState(Game game) {
        Cell[] cells = game.getBoard().getGrid();
        Cell[] validCells = game.getValidCells();
        return new GameState(cells, game, validCells);
    }


    // /** Getters and Setters **/
    /**
     * Returns the winner of the game state.
     *
     * @return the winner of the game state, or "-1" if there is no winner
     */
    public int getWinner() {
        if (this.game.getWinner() == null)
            return -1;
        else
            return this.game.getWinner().getPlayerId();
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
            "{\"cells\":%s,\"winner\":%d,\"currentPlayer\":%d}",
            Arrays.toString(this.cells), this.getWinner(), this.getCurrentPlayer()
        );
    }
}