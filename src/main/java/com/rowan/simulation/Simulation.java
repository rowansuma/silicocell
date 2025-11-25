package com.rowan.simulation;

import java.util.ArrayList;
import java.util.List;

import com.rowan.physicsobject.PhysicsObject;
import com.rowan.physicsobject.cell.Cell;
import com.rowan.utils.Utils;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

/**
 * Handles simulation steps, looping, and parameters.
 */
public class Simulation {
    private Scene scene;
    private Pane root;

    public final float SCREEN_WIDTH;
    public final float SCREEN_HEIGHT;

    public final float ENV_WIDTH = 4000;
    public final float ENV_HEIGHT = 4000;

    public final float ZOOM_FACTOR = 0.1f;

    public final float FRICTION = 0.96f;
    public final float CELL_RADIUS = 10f;
    public final float CELL_CYCLE_LENGTH = 200f;
    public final float CELL_REPULSION = 0.2f;
    public final float CELL_SPEED = 0.05f;
    public final float CELL_TURN_SPEED = 0.2f;

    public final float OFFSET_X = ENV_WIDTH / 2f;
    public final float OFFSET_Y = ENV_HEIGHT / 2f;

    public final int PARTITION_WIDTH = 20;
    public final int PARTITION_HEIGHT = 20;
    public final int GRID_COLS = (int)ENV_WIDTH/PARTITION_WIDTH;
    public final int GRID_ROWS = (int)ENV_HEIGHT/PARTITION_HEIGHT;

    private List<PhysicsObject>[][] grid = new ArrayList[GRID_COLS][GRID_ROWS];
    private ArrayList<PhysicsObject> addQueue = new ArrayList<>();

    /**
     * Creates a new Simulation with access to the scene and root group.
     * @param scene     Scene
     * @param root      Root Pane
     */
    public Simulation(Scene scene, Pane root) {
        this.scene = scene;
        this.root = root;

        this.SCREEN_WIDTH = (float) scene.getWidth();
        this.SCREEN_HEIGHT = (float) scene.getHeight();

        initalizeGrid();

        setInitialConditions();
        startLoop();
    }

    /**
     * Defines the initial setup (not parameters) for the simulation,
     * e.g. creation and position of initial cells.
     */
    private void setInitialConditions() {
        for (int i = 0; i < 1; i++) {
            new Cell(this, Utils.randomSquareVector2f(OFFSET_X));
        }
    }

    /**
     * Initializes the game loop.
     */
    public void startLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastTime = 0;
            private long fpsTimer = 0;
            private int frameCount = 0;
            private double fps = 0;

            @Override
            public void handle(long now) {
                if (lastTime > 0) {
                    long delta = now - lastTime;
                    fpsTimer += delta;
                    frameCount++;

                    // update FPS once per second (1,000,000,000 ns)
                    if (fpsTimer >= 1_000_000_000L) {
                        fps = frameCount * 1_000_000_000.0 / fpsTimer;
                        frameCount = 0;
                        fpsTimer = 0;

                        // optional: print or store FPS
                        System.out.println("FPS: " + (int)fps);
                    }

                    gameLoop();
                }
                lastTime = now;
            }
        };
        gameLoop.start();
    }

    /**
     * Handles game loop logic.
     * Iterates through cells to perform physics and behavior calculations.
     * Called every simulation step.
     */
    public void gameLoop() {
        for (int n = 0; n < 3; n++) { // Physics substeps
            for (int i = 0; i < grid.length; i++) { // Grid iteration
                for (int j = 0; j < grid[0].length; j++) {
                    for (PhysicsObject o : grid[i][j]) { // Object iteration

                        o.verletStep();

                        for (int k : new int[]{-1, 0, 1}) { // Neighbor partition iteration
                            for (int l : new int[]{-1, 0, 1}) {
                                int nx = i + k; // Neighbor partition
                                int ny = j + l; // Neighbor partition
                                if (nx < 0 || ny < 0 || nx >= GRID_COLS || ny >= GRID_ROWS) continue;
                                for (PhysicsObject other : grid[nx][ny]) {
                                    if (other.equals(o)) continue;
                                    PhysicsObject.verletCollisions(this, o, other);
                                }
                            }
                        }

                        o.verletBorderConstraints();

                    }
                }
            }
        }

        ArrayList<PhysicsObject> toReAdd = new ArrayList<>(); // Object to be re-added to grid

        for (int i = 0; i < grid.length; i++) { // Cell processes
            for (int j = 0; j < grid[0].length; j++) {
                for (PhysicsObject o : grid[i][j]) {
                    if (o instanceof Cell) {
                        Cell c = (Cell) o;
                        c.applyLocomotion();
                        c.handleCellCycle();
                    }
                    o.updateShapePosition();

                    if (!o.inExpectedPartition()) toReAdd.add(o);
                }
            }
        }


        toReAdd.addAll(addQueue);
        addQueue.clear();

        for (PhysicsObject o : toReAdd) { // Re-add objects to grid
            o.recalculatePartition();
        }

        
    }

    

    /**
     * Adds a shape to the scene.
     * @param shape shape
     */
    public void addShape(Shape shape) {
        root.getChildren().add(shape);
    }

    /**
     * Adds a PhysicsObject to the creation queue.
     * The object will be added in the next simulation step.
     * @param o object
     */
    public void addPhysicsObject(PhysicsObject o) {
        addQueue.add(o);
    }

    /**
     * Initializes the grid and adds empty partitions.
     */
    public void initalizeGrid() {
        for (int i = 0; i < GRID_COLS; i++)
            for (int j = 0; j < GRID_ROWS; j++)
                grid[i][j] = new ArrayList<>();
    }

    /**
     * Inserts PhysicsObject into grid.
     * @param o Object
     * @param x x index
     * @param y y index
     */
    public void insertObjectIntoGrid(PhysicsObject o, int x, int y) {
        grid[x][y].add(o);
    }

    /**
     * Removes PhysicsObject from grid.
     * @param o Object
     * @param x x index
     * @param y y index
     */
    public void removeObjectFromGrid(PhysicsObject o, int x, int y) {
        List<PhysicsObject> cell = grid[x][y];
        int idx = cell.indexOf(o);
        if (idx != -1) {
            int last = cell.size() - 1;
            cell.set(idx, cell.get(last));
            cell.remove(last);
        }
    }

    public List<PhysicsObject>[][] getGrid() {
        return grid;
    }

    public Scene getScene() {
        return scene;
    }

    public Pane getRoot() {
        return root;
    }
}
