// package com.santorini;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

// import fi.iki.elonen.NanoHTTPD;
// import java.util.Map;
// import java.util.HashMap;
// import java.lang.reflect.Field;
// import java.io.InputStream;

// class AppTest {
//     private App app;

//     @BeforeEach
//     void setUp() throws Exception {
//         app = new App();
//     }

//     @Test
//     void testNewGameCreation() throws Exception {
//         app.serve(new TestSession("/newgame"));
//         Field gameField = App.class.getDeclaredField("game");
//         gameField.setAccessible(true);
//         assertNotNull(gameField.get(app));

//         Field totalWorkersPlacedField = App.class.getDeclaredField("totalWorkersPlaced");
//         totalWorkersPlacedField.setAccessible(true);
//         assertEquals(0, totalWorkersPlacedField.getInt(app));

//         Field selectedWorkerField = App.class.getDeclaredField("selectedWorker");
//         selectedWorkerField.setAccessible(true);
//         assertNull(selectedWorkerField.get(app));

//         Field validCellsField = App.class.getDeclaredField("validCells");
//         validCellsField.setAccessible(true);
//         assertNull(validCellsField.get(app));

//         Field workerPhaseField = App.class.getDeclaredField("workerPhase");
//         workerPhaseField.setAccessible(true);
//         assertEquals(0, workerPhaseField.getInt(app));
//     }

//     @Test
//     void testWorkerSetup() throws Exception {
//         app.serve(new TestSession("/setup").addParam("cell1", "0,0"));

//         Field totalWorkersPlacedField = App.class.getDeclaredField("totalWorkersPlaced");
//         totalWorkersPlacedField.setAccessible(true);
//         assertEquals(1, totalWorkersPlacedField.getInt(app));

//         Field gameField = App.class.getDeclaredField("game");
//         gameField.setAccessible(true);
//         Game game = (Game) gameField.get(app);
//         assertTrue(game.getBoard().getCell(0, 0).isOccupied());
//     }

//     @Test
//     void testWorkerSelection() throws Exception {
//         app.serve(new TestSession("/setup").addParam("cell1", "0,0"));
//         app.serve(new TestSession("/selectedWorker").addParam("x", "0").addParam("y", "0"));

//         Field selectedWorkerField = App.class.getDeclaredField("selectedWorker");
//         selectedWorkerField.setAccessible(true);
//         assertNotNull(selectedWorkerField.get(app));

//         Field validCellsField = App.class.getDeclaredField("validCells");
//         validCellsField.setAccessible(true);
//         assertNotNull(validCellsField.get(app));
//     }

//     @Test
//     void testInvalidWorkerSelection() throws Exception {
//         app.serve(new TestSession("/setup").addParam("cell1", "0,0"));
//         app.serve(new TestSession("/selectedWorker").addParam("x", "1").addParam("y", "1"));

//         Field selectedWorkerField = App.class.getDeclaredField("selectedWorker");
//         selectedWorkerField.setAccessible(true);
//         assertNull(selectedWorkerField.get(app));

//         Field validCellsField = App.class.getDeclaredField("validCells");
//         validCellsField.setAccessible(true);
//         assertNull(validCellsField.get(app));
//     }

//     @Test
//     void testWorkerMove() throws Exception {
//         app.serve(new TestSession("/setup").addParam("cell1", "0,0"));
//         app.serve(new TestSession("/selectedWorker").addParam("x", "0").addParam("y", "0"));
//         app.serve(new TestSession("/selectedTargetCell").addParam("x", "0").addParam("y", "1"));

//         Field workerPhaseField = App.class.getDeclaredField("workerPhase");
//         workerPhaseField.setAccessible(true);
//         assertEquals(1, workerPhaseField.getInt(app));

//         Field validCellsField = App.class.getDeclaredField("validCells");
//         validCellsField.setAccessible(true);
//         assertNotNull(validCellsField.get(app));
//     }

//     @Test
//     void testWorkerBuild() throws Exception {
//         app.serve(new TestSession("/setup").addParam("cell1", "0,0"));
//         app.serve(new TestSession("/selectedWorker").addParam("x", "0").addParam("y", "0"));
//         app.serve(new TestSession("/selectedTargetCell").addParam("x", "0").addParam("y", "1"));
//         app.serve(new TestSession("/selectedTargetCell").addParam("x", "1").addParam("y", "1"));

//         Field workerPhaseField = App.class.getDeclaredField("workerPhase");
//         workerPhaseField.setAccessible(true);
//         assertEquals(0, workerPhaseField.getInt(app));

//         Field validCellsField = App.class.getDeclaredField("validCells");
//         validCellsField.setAccessible(true);
//         assertNull(validCellsField.get(app));

//         Field gameField = App.class.getDeclaredField("game");
//         gameField.setAccessible(true);
//         Game game = (Game) gameField.get(app);
//         assertEquals(1, game.getBoard().getCell(1, 1).getHeight());
//     }

//     // TestSession class to simulate HTTP requests
//     private class TestSession implements NanoHTTPD.IHTTPSession {
//         private String uri;
//         private Map<String, String> params;

//         public TestSession(String uri) {
//             this.uri = uri;
//             this.params = new HashMap<>();
//         }

//         public TestSession addParam(String key, String value) {
//             this.params.put(key, value);
//             return this;
//         }

//         @Override
//         public Method getMethod() {
//             return Method.GET;
//         }

//         @Override
//         public String getUri() {
//             return uri;
//         }

//         @Override
//         public Map<String, String> getParms() {
//             return params;
//         }

//         @Override
//         public Map<String, String> getHeaders() {
//             return new HashMap<>();
//         }
//     }
// }