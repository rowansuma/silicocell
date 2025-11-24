package com.rowan.physicsobject;

import org.joml.Vector2f;

import com.rowan.simulation.Simulation;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A PhysicsObject with a circle shape.
 */
public class PhysicsCircle extends PhysicsObject {
    protected float radius;

    /**
     * Creates a new PhysicsCircle at a given position with given radius and color.
     * @param s         Simulation
     * @param pos       Position
     * @param radius    Radius
     * @param color     Color
     */
    public PhysicsCircle(Simulation s, Vector2f pos, float radius, Color color) {
        super(s, pos, radius, color);
        this.radius = radius;
        shape = new Circle(radius);
        updateShapePosition();
        s.addShape(shape);
        setColor(color);
    }

    public void updateShapePosition() {
        shape.setLayoutX(pos.x);
        shape.setLayoutY(pos.y);
    }

    public float getRadius() {
        return radius;
    }
}
