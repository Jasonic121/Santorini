
# Justification for Handling State

Below, describe where you stored each of the following states and justify your answers with design principles/goals/heuristics/patterns. Discuss the alternatives and trade-offs you considered during your design process.

## Players

**Storage**: The `Game` class stores the `players` state within an `ArrayList<Player>`.

**Justification**: This aligns with the Information Expert principle, where the `Game` class is responsible for managing the players in the game. It holds the collection of `Player` objects since it's the `Game`'s role to orchestrate the turns and keep track of all participants.

## Current Player\

**Storage**: The `currentPlayer` state is stored in the `Game` class as a `Player` reference.

**Justification**: By managing the flow of the game, including tracking the current player, the `Game` class effectively serves as a Controller, centralizing control actions to manage system operations. This approach is consistent with the Controller pattern, ensuring that game flow control is encapsulated within a single class responsible for overseeing game progression.

## Worker Locations

**Storage**: Each `Worker` object holds a reference to its `currentCell`, and the `Board` class contains a grid of `Cell` objects.

**Justification**: The `Worker` knows its position, adhering to the Information Expert principle. It's the most direct and encapsulated way to access and modify a worker's location without unnecessary traversal or exposure of the `Board` structure.

## Towers

**Storage**: The state of towers is stored within the `Cell` objects in the `grid` of the `Board` class.

**Justification**: Each `Cell` is an Information Expert on its own state, including whether it has a block or dome (part of a tower). This design respects the High Cohesion principle, keeping related data and behaviors close together.

## Winner

**Storage**: The `winner` state is stored in the `Game` class, for the moment a message will pop out in the terminal printing "Player n has won!", where n is the winner's `playerID`.

**Justification**: The `Game` is responsible for game flow and determining the endgame conditions, so it would be the logical place to track the winner. This follows the Controller pattern, where `Game` acts as a controller for the game state.

## Design Goals/Principles/Heuristics Considered

1. **Information Expert**: Assign responsibilities to the class that has the necessary information.
2. **High Cohesion**: Keep related data and behaviors in the same class to promote organized and manageable code.
3. **Low Coupling**: Avoid unnecessary interdependencies between classes to make the system more maintainable.
4. **Controller**: Centralize control actions in a controller class to manage system operations.

## Alternatives Considered and Analysis of Trade-offs

- Alternative: Storing player and worker data directly in `Board` or in a separate `GameState` class.
  
  Trade-off: This could reduce the cohesion of the `Board` class and increase coupling, making the system more complex and harder to maintain. By keeping player and worker information within their respective classes and using `Game` as a controller, the design is cleaner and adheres to SOLID principles.

- Alternative: Placing the `winner` state within a `Player` class.
  
  Trade-off: This could potentially make sense if a player could be active in multiple games. However, in a single game context, it complicates the retrieval of the winner and may lead to redundancy if more than one `Player` instance has to track the winning state.
