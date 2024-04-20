package com.santorini;

public abstract class GodCard {
    public abstract void onBeforeMove(Player player, int workerId, int x, int y);
    public abstract void onAfterMove(Player player, int workerId, int x, int y);
    public abstract void onBeforeBuild(Player player, int workerId, int x, int y);
    public abstract void onAfterBuild(Player player, int workerId, int x, int y);
    public abstract boolean checkWinCondition(Player player);
    public void onMove(Player player, int workerId, int x, int y, Game game) {
        // Default implementation: Call the original moveWorkerUntilPointsExhausted method
        game.moveWorkerUntilPointsExhausted(workerId, x, y);
    }
    public void onBuild(Player player, int workerId, int x, int y, Game game) {
        // Default implementation: Call the original buildUntilPointsExhausted method
        game.buildUntilPointsExhausted(workerId, x, y);
    }

    /**
     * Get the god card name as string, and also add the description of the god card.
     *
     * @return The god card name as string with description
     */
    public String getGodCardName() {
        String className = this.getClass().getSimpleName();
        String description = "";

        switch (className) {
            case "DemeterGodCard":
                description = "Demeter: Your Worker may build one additional time, but not on the same space.";
                break;
            case "HephaestusGodCard":
                description = "Hephaestus: Your Worker may build one additional block (not dome) on top of your first block.";
                break;
            case "MinotaurGodCard":
                description = "Minotaur: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.";
                break;
            case "PanGodCard":
                description = "Pan: You also win if your Worker moves down two or more levels.";
                break;
            default:
                description = "No description available for this god card.";
                break;
        }

        return className + ": " + description;
    }
}