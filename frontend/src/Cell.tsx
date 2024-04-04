import React from 'react';
import { Cell } from './Game.tsx';
import player1 from './resources/img/workers/player_1/worker1.png';
import player2 from './resources/img/workers/player_2/worker1.png';
import selectedWorkerImage from './resources/img/blocks/move.png';

interface Props {
  cell: Cell;
  selectedWorkerCell: Cell | null;
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const { cell, selectedWorkerCell } = this.props;
    const occupied = cell.occupied ? 'occupied' : '';
    const dome = cell.hasDome ? 'dome' : '';
    const height = `height-${cell.height}`;

    const isSelectedWorker = selectedWorkerCell && selectedWorkerCell.x === cell.x && selectedWorkerCell.y === cell.y;

    const cellStyle: React.CSSProperties = {
      backgroundImage: isSelectedWorker ? `url(${selectedWorkerImage})` : 'none',
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