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
                System.out.println("Previous build cell: " + previousBuildCell.getX() + ", " + previousBuildCell.getY());
                game.setIsSecondBuild(true);
                keepOnlyValidCell(game, targetCell);

                System.out.println("isSecondBuild: " + game.getIsSecondBuild());
                if (targetCell.getHeight() >= 3) {
                    game.setIsSecondBuild(false);
                    System.out.println("higher than 3, isSecondBuild: " + game.getIsSecondBuild());
                    player.setBuildPoints(0);
                    hasUsedExtraBuild = true;
                    return;
                }
            } else {
                // Second build - cannot build domes
                game.setIsSecondBuild(false);
                System.out.println("targetCell: " + targetCell.getX() + ", " + targetCell.getY());

                if (previousBuildCell != null) { // Check if previousBuildCell is not null
                    System.out.println("previousBuildCell: " + previousBuildCell.getX() + ", " + previousBuildCell.getY());

                    if (targetCell.getHeight() < 3 && !isNotSameCell(targetCell, previousBuildCell)) {
                        player.build(workerId, targetCell);

                        System.out.println("Length of valid cells: " + game.getValidCells().length);
                        System.out.println("Target cell: " + targetCell.getX() + ", " + targetCell.getY());
                        keepOnlyValidCell(game, targetCell);

                        System.out.println("Length of valid cells after removal: " + game.getValidCells().length);
                    } else {
                        System.out.println("Hephaestus: Cannot build on a different cell or a dome on the additional build");
                        return;
                    }
                } else {
                    System.out.println("Hephaestus: Cannot perform the second build without a previous build");
                    return;
                }
            }
        }
    }

    @Override
    public void onAfterBuild(Player player, int workerId, int x, int y) {
        System.out.println("Hephaestus: After build, hasUsedExtraBuild: " + hasUsedExtraBuild);
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

    private boolean isNotSameCell(Cell cell1, Cell cell2) {
        return cell1.getX() != cell2.getX() || cell1.getY() != cell2.getY();
    }

    private void keepOnlyValidCell(Game game, Cell validCell) {
        List<Cell> cellsToRemove = new ArrayList<>();
        for (Cell cell : game.getValidCells()) {
            System.out.println("Now comparing with cell: " + cell.getX() + ", " + cell.getY());
            if (isNotSameCell(cell, validCell)) {
                cellsToRemove.add(cell);
            }
        }

        for (Cell cell : cellsToRemove) {
            System.out.println("Removing valid cell: " + cell.getX() + ", " + cell.getY());
            game.removeValidCells(cell);
        }
    }

    @Override
    public void resetState() {
        hasUsedExtraBuild = false; // Reset the flag
    }
}