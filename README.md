# Santorini Game
![intro](https://github.com/CMU-17-214/s24-hw3-santorini-Jasonic121/assets/54169592/16fe87e0-41db-4eef-aa4e-46c59fc978b7)

This is an implementation of the Santorini board game using Java for the backend and React for the frontend. The game allows two players to compete against each other, taking turns to move their workers and build towers on the game board.
## Table of Contents
- Features
- Prerequisites
- Getting Started
- How to Play
- Deployment
- Project Structure
- Contributing
- License
## Features
- Interactive game board with clickable cells for player actions
- Setup phase for initial worker placement
- Move phase for selecting and moving workers
- Build phase for constructing towers
- Win and lose conditions detection
- Current player and game phase display
- New game and test layout functionality
## Prerequisites
- Java Development Kit (JDK) installed
- Node.js and npm (Node Package Manager) installed
## Getting Started
1. Clone the repository:
`git clone https://github.com/CMU-17-214/s24-hw3-santorini-Jasonic121.git`

2. Navigate to the project directory:
`cd s24-hw3-santorini-Jasonic121`

3. Start the Java backend server:
`cd backend`
`mvn compile`
`mvn exec:java`

5. In a separate terminal, navigate to the frontend directory:
`cd frontend`

6. Install the frontend dependencies:
`npm install`

7. Start the React development server:
`npm start`

8. Open your web browser and visit `http://localhost:3000` to play the game.

## How to Play
~~To load a test layout, click the "Test Layout" button.~~ **The test layout function is under construction, DO NOT USE THIS TO TEST**
1. The game starts with the setup phase, where each player takes turns placing their workers on the game board by clicking on the desired cells.
2. After the setup phase, the game proceeds to the move phase. The current player selects one of their workers by clicking on it.
3. The valid cells for movement are highlighted. Click on a valid cell to move the selected worker.
4. After moving, the game enters the build phase. The valid cells for building are highlighted. Click on a valid cell to construct a tower level.
5. The game alternates between players until a win or lose condition is met.
6. To start a new game, click the "New Game" button.

## Deployment

The Santorini game can be deployed with:
- Frontend: GitHub Pages
- Backend: Render (or other hosting service)

For detailed deployment instructions, please refer to the [DEPLOYMENT.md](DEPLOYMENT.md) file.

## Project Structure
The project consists of two main directories:
- `backend`: Contains the Java source code for the game logic and server.
- `frontend`: Contains the React source code for the user interface.
## Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please create an issue or submit a pull request.
![icon](https://github.com/CMU-17-214/s24-hw3-santorini-Jasonic121/assets/54169592/f96a71de-a18d-43b5-9484-39bd94b67ef2)
