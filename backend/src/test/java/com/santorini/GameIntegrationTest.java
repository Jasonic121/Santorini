import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// GameIntegrationTest.java
package com.santorini;


class GameIntegrationTest {
    private Game game;
    private static final int PLAYER_1 = 0;
    private static final int PLAYER_2 = 1;
    private static final int INITIAL_HEIGHT = 0;
    private static final int MOVE_ROW = 1;
    private static final int MOVE_COL = 0;
    private static final int BUILD_ROW = 0;
    private static final int BUILD_COL = 0;
    
    @BeforeEach
    void setUp() {
        game = new Game();
    }
    
    @Test
    void testGamePlayWithDemeterGodCard() {
        game.selectGodCard(PLAYER_1, "Demeter");
        game.selectGodCard(PLAYER_2, "Normal");
        
        game.setupInitialWorker(game.getBoard().getCell(0, 0), PLAYER_1, INITIAL_HEIGHT);
        game.setupInitialWorker(game.getBoard().getCell(1, 1), PLAYER_1, INITIAL_HEIGHT + 1);
        game.setupInitialWorker(game.getBoard().getCell(3, 3), PLAYER_2, INITIAL_HEIGHT);
        game.setupInitialWorker(game.getBoard().getCell(4, 4), PLAYER_2, INITIAL_HEIGHT + 1);
        
        game.executeMoveTurn(PLAYER_1, MOVE_ROW, MOVE_COL);
        game.executeBuildTurn(PLAYER_1, BUILD_ROW, BUILD_COL);
        game.executeBuildTurn(PLAYER_1, BUILD_ROW + 2, BUILD_COL);
        
        assertEquals(1, game.getBoard().getCell(BUILD_ROW, BUILD_COL).getHeight());
        assertEquals(1, game.getBoard().getCell(BUILD_ROW + 2, BUILD_COL).getHeight());
        
        game.executeMoveTurn(PLAYER_2, MOVE_ROW + 3, MOVE_COL + 3);
        game.executeBuildTurn(PLAYER_2, BUILD_ROW + 3, BUILD_COL + 4);
        
        assertEquals(1, game.getBoard().getCell(BUILD_ROW + 3, BUILD_COL + 4).getHeight());
    }
}