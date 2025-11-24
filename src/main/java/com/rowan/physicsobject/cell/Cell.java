package com.rowan.physicsobject.cell;

import org.joml.Vector2f;

import com.rowan.physicsobject.PhysicsCircle;
import com.rowan.simulation.Simulation;
import com.rowan.utils.Utils;

import javafx.scene.paint.Color;

/**
 * A generic Cell that moves and goes through the cell cycle.
 */
public class Cell extends PhysicsCircle {
    protected float speed;
    protected float turnSpeed;
    protected float angle;
    protected float turnState = 0;

    protected float timer = 0;

    /**
     * Creates a new Cell at a given position.
     * @param s     Simulation
     * @param pos   Position
     */
    public Cell(Simulation s, Vector2f pos) {
        super(s, pos, s.CELL_RADIUS, Color.SKYBLUE);
        this.speed = s.CELL_SPEED;
        this.turnSpeed = s.CELL_TURN_SPEED;
        this.angle = (float)(Math.random()*2*Math.PI);
    }

    /**
     * Calculates and applies random cell movement of its own accord.
     * Distinct from cell movement relating to external factors.
     */
    public void applyLocomotion() {
        if (Math.random() < 0.3) { // Turn State Calculation
            double r = Math.random();
            if (r < 1f/3) turnState = -1;
            else if (r < 2f/3) turnState = 0;
            else turnState = 1;
        }
        
        angle += turnState * turnSpeed; // Update Angle
        Vector2f movementVector = new Vector2f((float)Math.cos(angle), (float)Math.sin(angle)).mul(speed);
        pos.add(movementVector);
    }

    /**
     * Handles all properties and functionality of the cell cycle, such as mitosis.
     */
    public void handleCellCycle() {
        timer += 1;
        if (timer == s.CELL_CYCLE_LENGTH) { // Mitosis Clock
            timer = 0;
            new Cell(s, new Vector2f(pos).add(Utils.randomCircleVector2f(15, 20)));
        }
    }
}
