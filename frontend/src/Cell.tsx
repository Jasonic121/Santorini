import React from 'react';
import { Cell } from './Game.tsx';
import player1 from './resources/img/workers/player_1/worker1.png';
import player2 from './resources/img/workers/player_2/worker1.png';
import selectedWorkerImage from './resources/img/blocks/move.png';
import validMoveImage from './resources/img/blocks/build.png'; 
import bottomImage from './resources/img/blocks/bottom.png';
interface Props {
  cell: Cell;
  selectedWorkerCell: Cell | null;
  validCells: Cell[];
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const { cell, selectedWorkerCell, validCells: validMoves } = this.props;
    const occupied = cell.occupied ? 'occupied' : '';
    const dome = cell.hasDome ? 'dome' : '';
    const height = `height-${cell.height}`;

    const isSelectedWorker = selectedWorkerCell?.x === cell.x && selectedWorkerCell?.y === cell.y;
    const isValidMove = validMoves?.some(move => move.x === cell.x && move.y === cell.y);

    console.log("Valid Moves: ", validMoves);

    let backgroundImage = 'none';
    if (isSelectedWorker) {
      backgroundImage = `url(${selectedWorkerImage})`;
    } else if (isValidMove) {
      backgroundImage = `url(${validMoveImage})`;
    } else if (cell.height === 1) {
      backgroundImage = `url(${bottomImage})`;
    }

    const cellStyle: React.CSSProperties = {
      backgroundImage,
      backgroundSize: 'cover',
      backgroundPosition: 'center',
    };


    if (cell.occupied) {
      if (cell.occupiedBy === 0) {
        return (
          <div className={`cell ${occupied} ${dome} ${height}`} style={cellStyle}>
            <div className="worker player1">
              <img src={player1} alt="Player 1" style={{ width: '50px', height: '50px' }} />
            </div>
          </div>
        );
      } else if (cell.occupiedBy === 1) {
        return (
          <div className={`cell ${occupied} ${dome} ${height}`} style={cellStyle}>
            <div className="worker player2">
              <img src={player2} alt="Player 2" style={{ width: '50px', height: '50px' }} />
            </div>
          </div>
        );
      }
    } else {
      return <div className={`cell ${occupied} ${dome} ${height}`} style={cellStyle}></div>;
    }
  }
}

export default BoardCell;