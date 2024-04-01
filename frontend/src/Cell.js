import React from 'react';
import './Cell.css';

const Cell = ({ cell, onClick }) => {
  const cellStyle = {
    backgroundColor: cell.color,
  };

  return (
    <div 
      className={`cell ${cell.isOccupied ? 'occupied' : ''}`} 
      onClick={onClick}
      style={cellStyle} 
    >
      {cell.isOccupied ? 'Occupied' : 'Empty'}
      {/* Display the height (level) of the cell */}
      <div className="level">Level: {cell.height}</div>
    </div>
  );
};

export default Cell;

