import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// GodCardFactoryTest.java
package com.santorini;


class GodCardFactoryTest {
    private static final String DEMETER_CARD_NAME = "Demeter";
    private static final String HEPHAESTUS_CARD_NAME = "Hephaestus";
    private static final String MINOTAUR_CARD_NAME = "Minotaur";
    private static final String PAN_CARD_NAME = "Pan";
    private static final String NORMAL_CARD_NAME = "Normal";
    private static final String INVALID_CARD_NAME = "InvalidCard";

    @Test
    void testCreateGodCard() {
        GodCard demeterCard = GodCardFactory.createGodCard(DEMETER_CARD_NAME);
        assertTrue(demeterCard instanceof DemeterGodCard);
        
        GodCard hephaestusCard = GodCardFactory.createGodCard(HEPHAESTUS_CARD_NAME);
        assertTrue(hephaestusCard instanceof HephaestusGodCard);
        
        GodCard minotaurCard = GodCardFactory.createGodCard(MINOTAUR_CARD_NAME);
        assertTrue(minotaurCard instanceof MinotaurGodCard);
        
        GodCard panCard = GodCardFactory.createGodCard(PAN_CARD_NAME);
        assertTrue(panCard instanceof PanGodCard);
        
        GodCard normalCard = GodCardFactory.createGodCard(NORMAL_CARD_NAME);
        assertNull(normalCard);
    }
    
    @Test
    void testCreateGodCard_InvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            GodCardFactory.createGodCard(INVALID_CARD_NAME);
        });
    }
}