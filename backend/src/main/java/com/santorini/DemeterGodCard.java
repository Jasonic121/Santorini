package com.santorini;

public class DemeterGodCard extends GodCard {
    private boolean hasUsedExtraBuild;
    private Cell previousBuildCell;

    @Override
    public void onBeforeMove(Player player, int workerId, int x, int y, Game game) {
        // No action required
    }

    @Override
    public void onMove(Player player, int workerId, int x, int y, Game game) {
        // Call the original moveWorkerUntilPointsExhausted method
        super.onMove(player, workerId, x, y, game);
    }

    @Override
    public void onAfterMove(Player player, int workerId, int x, int y) {
        // No action required
    }

    @Override
    public void onBeforeBuild(Player player, int workerId, int x, int y) {
        if (!hasUsedExtraBuild) {
            player.setBuildPoints(player.getBuildPoints() + 1);
            System.out.println("Demeter: Build points set to: " + player.getBuildPoints());
        }
    }

    @Override
    public void onBuild(Player player, int workerId, int buildX, int buildY, Game game) {
        if (!hasUsedExtraBuild) {
            onFirstBuild(player, workerId, buildX, buildY, game);
        } else {
            onSecondBuild(player, workerId, buildX, buildY, game);
        }
    }

    private void onFirstBuild(Player player, int workerId, int buildX, int buildY, Game game) {
        Board board = game.getBoard();
        Cell targetCell = board.getCell(buildX, buildY);

        if (player.checkBuildPointsAvailable() && isValidCell(targetCell, game.getValidCells())) {
            player.build(workerId, targetCell);
            System.out.println("First build completed");
            previousBuildCell = targetCell;
            game.setIsSecondBuild(true);
            game.removeValidCells(targetCell);
            System.out.println("isSecondBuild: " + game.getIsSecondBuild());
        }
    }

    private void onSecondBuild(Player player, int workerId, int buildX, int buildY, Game game) {
        Board board = game.getBoard();
        Cell targetCell = board.getCell(buildX, buildY);

        if (player.checkBuildPointsAvailable() && isValidCell(targetCell, game.getValidCells())) {
            if (!isSameCell(targetCell, previousBuildCell)) {
                player.build(workerId, targetCell);
                System.out.println("Second build completed");
                game.setIsSecondBuild(false);

                for (Cell cell : game.getValidCells()) {
                    System.out.println("Valid cell: " + cell.getX() + ", " + cell.getY());
                }
            } else {
                System.out.println("Demeter: Cannot build on the same cell twice");
            }
        }
    }

    @Override
    public void onAfterBuild(Player player, int workerId, int x, int y) {
        if (!hasUsedExtraBuild) {
            hasUsedExtraBuild = true;
        } else {
            System.out.println("Demeter: Extra build used");
            player.setBuildPoints(0);
            hasUsedExtraBuild = false;
            previousBuildCell = null; // Reset the previously built cell
        }
    }

    @Override
    public boolean checkWinCondition(Player player) {
        // No additional win condition for Demeter
        return false;
    }

    private boolean isValidCell(Cell targetCell, Cell[] validCells) {
        for (Cell cell : validCells) {
            if (cell.getX() == targetCell.getX() && cell.getY() == targetCell.getY()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameCell(Cell cell1, Cell cell2) {
        return cell1.getX() == cell2.getX() && cell1.getY() == cell2.getY();
    }
}