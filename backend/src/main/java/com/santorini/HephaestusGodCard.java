package com.santorini;

import java.util.ArrayList;
import java.util.List;

public class HephaestusGodCard extends GodCard {
    private boolean hasUsedExtraBuild = false;
    private Cell previousBuildCell;

    @Override
    public void onBeforeMove(Player player, int workerId, int x, int y, Game game) {
        // No action required
    }

    @Override
    public void onAfterMove(Player player, int workerId, int x, int y) {
        // No action required
    }

    @Override
    public void onBeforeBuild(Player player, int workerId, int x, int y) {
        if (!hasUsedExtraBuild) {
            player.setBuildPoints(player.getBuildPoints() + 1);
            System.out.println("Hephaestus: Build points set to: " + player.getBuildPoints());
        }
    }

    @Override
    public void onBuild(Player player, int workerId, int buildX, int buildY, Game game) {
        Board board = game.getBoard();
        Cell targetCell = board.getCell(buildX, buildY);

        if (player.checkBuildPointsAvailable() && isValidCell(targetCell, game.getValidCells())) {
            if (!hasUsedExtraBuild) {
                // First build - normal build (can build domes)
                player.build(workerId, targetCell);
                previousBuildCell = targetCell;
                game.setIsSecondBuild(true);
                System.out.println("isSecondBuild: " + game.getIsSecondBuild());
            } else {
                // Second build - cannot build domes
                game.setIsSecondBuild(false);

                if (targetCell.getHeight() < 4 && isSameCell(targetCell, previousBuildCell)) {
                    player.build(workerId, targetCell);

                    System.out.println("Length of valid cells: " + game.getValidCells().length);
                    System.out.println("Target cell: " + targetCell.getX() + ", " + targetCell.getY());

                    // List to collect cells to remove
                    List<Cell> cellsToRemove = new ArrayList<>();
                    for (Cell cell : game.getValidCells()) {
                        System.out.println("Now comparing with cell: " + cell.getX() + ", " + cell.getY());
                        if (cell.getX() != targetCell.getX() || cell.getY() != targetCell.getY()) {
                            cellsToRemove.add(cell);
                        }
                    }

                    // Removing cells after iterating to avoid concurrent modification
                    for (Cell cell : cellsToRemove) {
                        System.out.println("Removing valid cell: " + cell.getX() + ", " + cell.getY());
                        game.removeValidCells(cell);
                    }

                    System.out.println("Length of valid cells after removal: " + game.getValidCells().length);
                } else {
                    System.out.println("Hephaestus: Cannot build a dome on the additional build");
                }
            }
        }

    }

    @Override
    public void onAfterBuild(Player player, int workerId, int x, int y) {
        if (!hasUsedExtraBuild) {
            hasUsedExtraBuild = true;
        } else {
            System.out.println("Hephaestus: Extra build used");
            player.setBuildPoints(0);
            hasUsedExtraBuild = false;
            previousBuildCell = null; // Reset the previously built cell
        }
    }

    @Override
    public boolean checkWinCondition(Player player) {
        // No additional win condition for Hephaestus
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