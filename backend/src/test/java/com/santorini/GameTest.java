// GameTest.java
package com.santorini;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;





public class GameTest {
    private static final int PLAYER_INDEX = 0;
    private static final int WORKER_INDEX = 0;
    private Game game;
    
    @BeforeEach
    void setUp() {
        game = new Game();
    }
    
    @Test
    void testSelectGodCard() {
        game.selectGodCard(PLAYER_INDEX, "Demeter");
        assertEquals("DemeterGodCard: Your Worker may build one additional time, but not on the same space.", game.getPlayers().get(PLAYER_INDEX).getGodCard().getGodCardName());
    }
    
    @Test
    void testSetupInitialWorker() {
        Cell initialCell = game.getBoard().getCell(0, 0);
        game.setupInitialWorker(initialCell, PLAYER_INDEX, WORKER_INDEX);
        assertEquals(initialCell, game.getPlayers().get(PLAYER_INDEX).getWorker(WORKER_INDEX).getCurrentCell());
    }
}
