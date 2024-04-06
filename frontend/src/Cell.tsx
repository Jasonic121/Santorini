import React from 'react';
import { Cell } from './Game.tsx';
import player1 from './resources/img/workers/player_1/worker1.png';
import player2 from './resources/img/workers/player_3/worker1.png';
import './Cell.css';

interface Props {
  cell: Cell;
  selectedWorkerCell: Cell | null;
  validCells: Cell[];
  isCurrentPlayerWorker: boolean;
  currentPlayer: number;
  currentGamePhase: number;
  currentWorkerPhase: number;
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const { cell, selectedWorkerCell, validCells: validMoves, currentGamePhase, currentWorkerPhase} = this.props;
    const occupied = cell.occupied ? 'occupied' : '';
    const dome = cell.hasDome ? 'dome' : '';
    const height = `height-${cell.height}`;

    const isSelectedWorker = selectedWorkerCell?.x === cell.x && selectedWorkerCell?.y === cell.y;
    let isValidMove = validMoves?.some(move => move.x === cell.x && move.y === cell.y);
    const isValidBuild = isValidMove && currentWorkerPhase === 1;
    isValidMove = isValidMove && currentWorkerPhase === 0;
    const hasSelectedWorker = selectedWorkerCell !== null;
    const playerAlt = cell.occupiedBy === 0 ? 'Player 1' : 'Player 2';
    const playerImage = cell.occupiedBy === 0 ? player1 : player2;
    const isCurrentPlayerWorker = this.props.isCurrentPlayerWorker && cell.occupied && Number(cell.occupiedBy) === this.props.currentPlayer;

    return (
      <div className={`cell ${occupied} ${dome} ${height} ${isSelectedWorker ? 'selected-worker' : ''} ${isValidMove ? 'valid-move' : ''} ${isValidBuild ? 'valid-build' : ''}`}>
        {cell.occupied && (
        <div className={`worker player${cell.occupiedBy} ${ (currentGamePhase!=1) && (!currentWorkerPhase) && isCurrentPlayerWorker && (!hasSelectedWorker) ? 'currentPlayerWorker' : ''}`}>
            <img src={playerImage} alt={playerAlt} />
          </div>
        )}
        {cell.height !== 0 && (
          <span className="height-indicator">{cell.height}</span>
        )}
      </div>
    );
  }
}

export default BoardCell;