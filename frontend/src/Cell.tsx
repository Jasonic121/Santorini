import React from 'react';
import { Cell } from './Game.tsx';
import player1 from './resources/img/workers/player_1/worker1.png';
import player2 from './resources/img/workers/player_3/worker1.png';
import selectedWorkerImage from './resources/img/blocks/move.png';
import validMoveImage from './resources/img/blocks/build.png'; 
import bottomImage from './resources/img/blocks/bottom.png';
import middle from './resources/img/blocks/middle.png';
import top from './resources/img/blocks/top.png';
import domeImg from './resources/img/blocks/dome.png';
import './Cell.css';
interface Props {
  cell: Cell;
  selectedWorkerCell: Cell | null;
  validCells: Cell[];
  isCurrentPlayerWorker: boolean;
  currentPlayer: number;
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const { cell, selectedWorkerCell, validCells: validMoves } = this.props;
    const occupied = cell.occupied ? 'occupied' : '';
    const dome = cell.hasDome ? 'dome' : '';
    const height = `height-${cell.height}`;

    const isSelectedWorker = selectedWorkerCell?.x === cell.x && selectedWorkerCell?.y === cell.y;
    const isValidMove = validMoves?.some(move => move.x === cell.x && move.y === cell.y);

    // console.log("Valid Moves: ", validMoves);

    let backgroundImages: string[] = [];
    
    if (isSelectedWorker) {
      backgroundImages.push(`url(${selectedWorkerImage})`);
    }

    if (isValidMove) {
      backgroundImages.push(`url(${validMoveImage})`);
    }

    if (cell.height === 1) {
      backgroundImages.push(`url(${bottomImage})`);
    }

    if (cell.height === 2) {
      backgroundImages.push(`url(${middle})`);
    }

    if (cell.height === 3) {
      backgroundImages.push(`url(${top})`);
    }

    if (cell.hasDome && cell.height === 4) {
      backgroundImages.push(`url(${domeImg})`);
    }

    const backgroundImage = backgroundImages.length > 0 ? backgroundImages.join(', ') : 'none';

    const cellBackground: React.CSSProperties = {
      backgroundImage,
      backgroundSize: 'cover',
      backgroundPosition: 'center',
    };

    const playerAlt = cell.occupiedBy === 0 ? 'Player 1' : 'Player 2';
    const playerImage = cell.occupiedBy === 0 ? player1 : player2;
    const isCurrentPlayerWorker = this.props.isCurrentPlayerWorker && cell.occupied && Number(cell.occupiedBy) === this.props.currentPlayer;    return (
      <div className={`cell ${occupied} ${dome} ${height}`} style={cellBackground}>
          {cell.occupied && (
            <div className={`worker player${cell.occupiedBy} ${isCurrentPlayerWorker ? 'currentPlayerWorker' : ''}`}>
              <img src={playerImage} alt={playerAlt} style={{ width: '8vh', height: '8vh' }} />
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
