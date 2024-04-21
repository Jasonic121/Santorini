package com.santorini;

public class GodCardFactory {
    public static GodCard createGodCard(String godCardName) {
        switch (godCardName) {
            case "Demeter":
                return new DemeterGodCard();
            case "Hephaestus":
                return new HephaestusGodCard();
            case "Minotaur":
                return new MinotaurGodCard();
            case "Pan":
                return new PanGodCard();
            case "Normal":
                return null;
            default:
                throw new IllegalArgumentException("Unknown god card: " + godCardName);
        }
    }
}