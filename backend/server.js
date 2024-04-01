const express = require('express');
const { exec } = require('child_process');
const app = express();
const port = 8080;
const cors = require('cors');
app.use(cors());
app.use(express.json());

let game;

// GET request to the homepage
app.get('/', (req, res) => {
  if (game == null) {
    res.json({ message: 'Welcome to the Santorini game API! No game is currently running.'});
  }
  res.json({ message: 'Welcome to the Santorini game API!' });
});

// GET the game variable
app.get('/start', (req, res) => {
  if (game == null) {
    return res.status(400).json({ message: 'Game is not started.' });
  }
  res.json({ message: 'Game is started.' });
});

// POST request to start the game
app.post('/start', (req, res) => {
  if (game) {
    return res.status(400).json({ message: 'Game is already started.' });
  }
  
  // Execute your Java program to start the game
  exec('java -cp "target/classes" com.santorini.App', (error, stdout, stderr) => {
    if (error) {
      console.error(`exec error: ${error}`);
      return res.status(500).json({ message: 'Internal server error' });
    }
    if (stderr) {
      console.error(`stderr: ${stderr}`);
      return res.status(500).json({ message: stderr });
    }
    
    // Assuming your Java program outputs the game state in JSON format
    try {
      const initialState = JSON.parse(stdout);
      
      // Initialize the game object using the state from the Java program
      game = new Game(initialState);
      
      // Send the initial game state as a response
      res.json(initialState);
    } catch (parseError) {
      console.error('Failed to parse game state:', parseError);
      res.status(500).json({ message: 'Failed to start game due to state parsing error' });
    }
  });
});

// POST request for the board state
app.post('/board', (req, res) => {
  if (!game) {
    // If the game is not initialized, inform the user.
    return res.status(400).json({ error: "Game not initialized" });
  }

  try {
    game.setBoard(req.body.board);
    res.json({ message: 'Board updated' });
  } catch (error) {
    console.error('Error:', error);
    res.status(500).json({ error: 'Failed to update board' });
  }
});

// GET the current board state
app.get('/board', (req, res) => {
  if (!game) {
    // If the game is not initialized, inform the user.
    return res.status(400).json({ error: "Game not initialized" });
  }
  // Send the current board state as a response.
  res.json({ board: game.getBoard() });
});



// GET the current game state from GameState
app.get('/state', (req, res) => {
  if (!game) {
    return res.status(500).json({ error: "Game not initialized" });
  }
  res.json(game.getGameState());
});



app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});
