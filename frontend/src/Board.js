import React from 'react';
import Cell from './Cell';
import './Board.css';
import map from './resources/img/background/map.png';

const Board = ({ board, onCellClick }) => (
  <div className="board" style={{ backgroundImage: `url(${map})` }}>
    {board.map((row, i) => (
      <div key={i} className="row">
        {row.map((cell, j) => (
          <Cell key={j} cell={cell} onClick={() => onCellClick(i, j)} color={cell.color} />
        ))}
      </div>
    ))}
  </div>
);

export default Board;
