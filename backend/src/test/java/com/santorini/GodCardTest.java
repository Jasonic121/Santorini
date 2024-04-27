
// GodCardTest.java
package com.santorini;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class GodCardTest {
    @Test
    void testGetGodCardName() {
        String demeterExpected = "DemeterGodCard: Your Worker may build one additional time, but not on the same space.";
        GodCard demeterCard = new DemeterGodCard();
        assertEquals(demeterExpected, demeterCard.getGodCardName());
        
        String hephaestusExpected = "HephaestusGodCard: Your Worker may move into an opponent Worker's space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.";
        GodCard hephaestusCard = new HephaestusGodCard();
        assertEquals(hephaestusExpected, hephaestusCard.getGodCardName());
        
        String minotaurExpected = "MinotaurGodCard: Your Worker may move into an opponent Worker's space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.";
        GodCard minotaurCard = new MinotaurGodCard();
        assertEquals(minotaurExpected, minotaurCard.getGodCardName());
        
        String panExpected = "PanGodCard: You also win if your Worker moves down two or more levels.";
        GodCard panCard = new PanGodCard();
        assertEquals(panExpected, panCard.getGodCardName());
    }
}