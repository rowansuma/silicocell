package com.rowan.simulation;

import java.util.ArrayList;

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

    public final float ENV_WIDTH = 600;
    public final float ENV_HEIGHT = 600;

    public final float ZOOM_FACTOR = 0.5f;

    public final float FRICTION = 0.96f;
    public final float CELL_RADIUS = 10f;
    public final float CELL_CYCLE_LENGTH = 200f;
    public final float CELL_REPULSION = 0.2f;
    public final float CELL_SPEED = 0.05f;
    public final float CELL_TURN_SPEED = 0.2f;
    public final float OFFSET_X = ENV_WIDTH / 2f;
    public final float OFFSET_Y = ENV_HEIGHT / 2f;

    private ArrayList<PhysicsObject> physicsObjects = new ArrayList<>();
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

        setInitialConditions();
        startLoop();
    }

    /**
     * Defines the initial setup (not parameters) for the simulation,
     * e.g. creation and position of initial cells.
     */
    private void setInitialConditions() {
        for (int i = 0; i < 100; i++) {
            new Cell(this, Utils.randomSquareVector2f(OFFSET_X));
        }
    }

    /**
     * Initializes the game loop.
     */
    public void startLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    gameLoop();
                }
                lastUpdate = now;
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
        System.out.println(physicsObjects.size());
        for (int i = 0; i < 3; i++) { // Physics substeps
            for (PhysicsObject o : physicsObjects) {
                o.verletStep();
                for (PhysicsObject other : physicsObjects) {
                    if (other.equals(o)) continue;
                    PhysicsObject.verletCollisions(this, o, other);
                }
                o.verletBorderConstraints();
            }
        }
        for (PhysicsObject o : physicsObjects) { // Cell processes
            if (o instanceof Cell) {
                Cell c = (Cell) o;
                c.applyLocomotion();
                c.handleCellCycle();
            }
            o.updateShapePosition();
        }
        physicsObjects.addAll(addQueue);
        addQueue.clear();
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

    public ArrayList<PhysicsObject> getPhysicsObjects() {
        return physicsObjects;
    }

    public Scene getScene() {
        return scene;
    }

    public Pane getRoot() {
        return root;
    }
}
