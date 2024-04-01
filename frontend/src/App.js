import React, { useEffect, useState } from 'react';
import Board from './Board';
import { GameState, Cell } from './Game';



const App = () => {
  const [board, setBoard] = useState(Array(5).fill().map(() => Array(5).fill({ isOccupied: false, height: 0 })));
  const [initialPositions, setInitialPositions] = useState([]);
  const [isSelectingInitialPositions, setIsSelectingInitialPositions] = useState(false); // New state to track if user is selecting initial positions
  const [gameStarted, setGameStarted] = useState(false);
  const [currentPhase, setCurrentPhase] = useState('Placing'); // Placing, Moving, Building
  const [selectedWorker, setSelectedWorker] = useState(null); // Track selected worker for moving and building
  
  const handleCellClick = (x, y) => {
    if(gameStarted) {
      console.log('currentPhase:', currentPhase, '\nselectedWorker:', selectedWorker, 'x:', x, 'y:', y)
      switch (currentPhase) {
        case 'Placing':
          placeInitialWorker(x, y);
          break;
        case 'Moving':
          moveWorker(x, y);
          break;
        case 'Building':
          buildLevel(x, y);
          break;
        default:
          console.log('Unknown game phase');
      }
    } else {
      console.log('Game not started yet');
    }
  };

  const placeInitialWorker = (x, y) => {
    // Logic to place initial workers on the board
    // Once all initial positions are filled, transition to the 'Moving' phase
    if (initialPositions.length < 4) {
      const newPositions = [...initialPositions, { x, y }];
      setInitialPositions(newPositions);
      // Assign color and place worker
      const color = newPositions.length <= 2 ? 'blue' : 'red';
      updateBoardCell(x, y, { color, isOccupied: true });

      if (newPositions.length === 4) {
        setCurrentPhase('Moving');
      }
    }
  };
  const moveWorker = (x, y) => {
    // Logic to move a selected worker
    // This requires keeping track of which worker is selected and then moving it to the new position
    // After moving, transition to the 'Building' phase
    if (selectedWorker) {
      const { x: prevX, y: prevY } = selectedWorker;
      updateBoardCell(prevX, prevY, { isOccupied: false }); // Clear previous position
      updateBoardCell(x, y, { isOccupied: true, color: board[prevX][prevY].color }); // Move to new position
      setSelectedWorker(null); // Reset selected worker
      setCurrentPhase('Building'); // Transition to building phase
    } else {
      // Select the worker at the clicked cell
      const cell = board[x][y];
      if (cell.isOccupied) {
        setSelectedWorker({ x, y });
      }
    }

  };
  
  const buildLevel = (x, y) => {
    // Logic to build a level on the board
    // Increment the height of the cell where the level is built
    updateBoardCell(x, y, cell => ({ height: cell.height + 1 }));
    setCurrentPhase('Moving'); // Transition back to moving for the next turn
  };
  
  const updateBoardCell = (x, y, update) => {
    setBoard(prevBoard => prevBoard.map((row, rowIndex) => row.map((cell, cellIndex) => {
      if (rowIndex === x && cellIndex === y) {
        return typeof update === 'function' ? update(cell) : { ...cell, ...update };
      }
      return cell;
    })));
  };

  const startGame = async () => {
    setGameStarted(true); // Mark the game as started
    console.log('Game started:', gameStarted);

    if (initialPositions.length === 4 && !gameStarted) {
      try {
        const response = await fetch('http://localhost:8080/start', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ positions: initialPositions }),
        });
        const data = await response.json();
        setBoard(data.board);
      } catch (error) {
        console.error('Error:', error);
      }
    } else if (gameStarted) {
      alert('Game is already in progress.');
    } else {
      alert('Please select 4 initial positions for the workers.');
    }
  };

  // Reset the game
  const resetGame = () => {
    setBoard(Array(5).fill().map(() => Array(5).fill({ isOccupied: false, height: 0 })));
    setInitialPositions([]);
    setGameStarted(false); // Reset the game status
  };

  // Every time the board changes, POST the new board state to the server
  useEffect(() => {
    const updateBoard = async () => {
      try {
        const response = await fetch('http://localhost:8080/board', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ board }),
        });
        const data = await response.json();
        console.log('Updated board:', data);
      } catch (error) {
        console.error('Error:', error);
      }
    };

    updateBoard();
  }, [board]);

  return (
    <div className="app">
      <Board
        board={board}
        onCellClick={handleCellClick}
        cellColors={initialPositions.reduce((acc, pos, index) => {
          acc[`${pos.x},${pos.y}`] = index < 2 ? 'blue' : 'red'; // Assign color based on index
          return acc;
        }, {})}
      />
      <ul>
        {initialPositions.map((pos, index) => (
          <li key={index} style={{ color: index < 2 ? 'blue' : 'red' }}>
            Worker {index + 1}: ({pos.x}, {pos.y})
          </li>
        ))}
      </ul>
      <button onClick={startGame}>Start Game</button>
      <button onClick={resetGame}>Reset Game</button>
    </div>
  );
};

export default App;
