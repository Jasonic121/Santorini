import React from 'react';
import { Cell } from './Game.tsx';
import player1 from './resources/img/workers/player_1/worker1.png';
import player2 from './resources/img/workers/player_2/worker1.png';

interface Props {
  cell: Cell;
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const { cell } = this.props;
    const occupied = cell.occupied ? 'occupied' : '';
    const dome = cell.hasDome ? 'dome' : '';
    const height = `height-${cell.height}`;
    const occupiedBy = cell.occupiedBy;

    if (cell.occupied) {
      if (cell.occupiedBy === 0) {
        return (
          <div className={`cell ${occupied} ${dome} ${height}`}>
            <div className="worker player1">
              1
              <img src={player1} alt="Player 1" style={{ width: '50px', height: '50px' }} />
            </div>
          </div>
        );
      } else {
        return (
          <div className={`cell ${occupied} ${dome} ${height}`}>
            <div className="worker player2">
              2
              <img src={player2} alt="Player 2" style={{ width: '50px', height: '50px' }} />
            </div>
          </div>
        );
      }
    } else {
      return <div className={`cell ${occupied} ${dome} ${height}`}></div>;
    }
  }
}

export default BoardCell;