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
    private ArrayList<ArrayList<Cell>> cells;
    private Board board;
    private Player winner;
    private Player currentPlayer;
    private Game game;


    public GameState(Board board, Game game) {
        this.board = board;
        this.game = game;
        this.winner = null;
        this.currentPlayer = game.getCurrentPlayer();
        this.cells = board.getGrid();
    }

    /**
     * Creates a new GameState object for the given game.
     *
     * @param game the game for which to create the state
     * @return the new GameState object
     */
    public static GameState getGameState(Game game) {
        Board board = game.getBoard();
        return new GameState(board, game);
    }


    /** Getters and Setters **/

    /**
     * Returns the board of the game state.
     *
     * @return an array of Cell objects representing the cells of the game state
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the cells of the game state.
     *
     * @return an array of Cell objects representing the cells of the game state
     */
    public ArrayList<ArrayList<Cell>> getCells() {
        return this.cells;
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
     * @return the current player in the game state, or "null" if there is no current player
     */
    public String getCurrentPlayer() {
        if (this.currentPlayer == null)
            return "null";
        else
            return this.currentPlayer.toString();
    }

    /**
     * Returns the string representation of the GameState object in JSON format.
     *
     * @return the string representation of the GameState object
     */
    @Override
    public String toString() {
        return String.format(
            "{ \"cells\": %s, \"winner\": \"%s\", \"currentPlayer\": \"%s\" }",
            this.cells.toString(), this.getWinner().toString(), this.getCurrentPlayer().toString()
        );
    }
}