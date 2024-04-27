// DemeterGodCardTest.java
package com.santorini;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;




public class DemeterGodCardTest {
    private static final int INITIAL_BUILD_POINTS = 0;
    private static final int X_COORDINATE = 0;
    private static final int Y_COORDINATE = 0;
    private static final int Z_COORDINATE = 0;
    
    private Game game;
    private Player player;
    private DemeterGodCard demeterCard;
    
    @BeforeEach
    void setUp() {
        game = new Game();
        player = game.getPlayers().get(0);
        demeterCard = new DemeterGodCard();
        player.setGodCard(demeterCard);
    }
    
    @Test
    void testOnBeforeBuild() {
        demeterCard.onBeforeBuild(player, X_COORDINATE, Y_COORDINATE, Z_COORDINATE);
        assertEquals(2, player.getBuildPoints());
    }
    
    @Test
    void testOnAfterBuild() {
        demeterCard.onBeforeBuild(player, X_COORDINATE, Y_COORDINATE, Z_COORDINATE);
        demeterCard.onAfterBuild(player, X_COORDINATE, Y_COORDINATE, Z_COORDINATE);
        assertTrue(demeterCard.hasUsedExtraBuild());
        
        demeterCard.onAfterBuild(player, X_COORDINATE, Y_COORDINATE, Z_COORDINATE);
        assertFalse(demeterCard.hasUsedExtraBuild());
        assertEquals(INITIAL_BUILD_POINTS, player.getBuildPoints());
    }
}