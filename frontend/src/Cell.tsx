import React from 'react';
import { Cell } from './Game.tsx';

interface Props {
  cell: Cell;
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const {cell} = this.props;
    const occupied = cell.occupied ? 'occupied' : '';
    const dome = cell.hasDome ? 'dome' : '';
    const height = `height-${cell.height}`;

    return (
      <div className={`cell ${occupied} ${dome} ${height}`} >
        {cell.occupied ? 'X' : ''}
      </div>
    );
  }
}

export default BoardCell;