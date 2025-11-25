package com.rowan.physicsobject;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import org.joml.Vector2f;

import com.rowan.simulation.Simulation;

/**
 * An object which undergoes verlet physics integration.
 * Represented by a shape in the 2D simulation space.
 * Kept track of and iterated upon by an ArrayList in the Simulation class.
 */
public abstract class PhysicsObject {
    protected Simulation s;

    protected Shape shape;

    public int currentPartitionX;
    public int currentPartitionY;

    protected Vector2f prevPos;
    protected Vector2f pos;
    protected float collisionRadius;

    protected Color color;
    
    /**
     * Creates a new PhysicsObject at a given position with given collision radius and color.
     * @param s                 Simulation
     * @param pos               Position
     * @param collisionRadius   Collision Radius
     * @param color             Color
     */
    public PhysicsObject(Simulation s, Vector2f pos, float collisionRadius, Color color) {
        this.s = s;
        this.prevPos = new Vector2f(pos);
        this.pos = new Vector2f(pos);

        // currentPartitionX = getPartitionX();
        // currentPartitionY = getPartitionY();
        
        this.collisionRadius = collisionRadius;
        s.addPhysicsObject(this);
    }

    /**
     * Updates position by calculating a velocity vector using the current and previous positions in accordance with verlet integration.
     */
    public void verletStep() {
        Vector2f temp = new Vector2f(pos);
        Vector2f velocity = new Vector2f(pos).sub(prevPos).mul(s.FRICTION);
        pos.add(velocity);
        prevPos = temp;
    }

    /**
     * Constraints cells to the borders of the simulation space in accordance with verlet integration.
     */
    public void verletBorderConstraints() {
        float halfW = s.ENV_WIDTH / 2f;
        float halfH = s.ENV_HEIGHT / 2f;

        if (pos.x < -halfW) {
            pos.x = -halfW;
            prevPos.x = pos.x;
        } else if (pos.x > halfW) {
            pos.x = halfW;
            prevPos.x = pos.x;
        }

        if (pos.y < -halfH) {
            pos.y = -halfH;
            prevPos.y = pos.y;
        } else if (pos.y > halfH) {
            pos.y = halfH;
            prevPos.y = pos.y;
        }
    }

    /**
     * Adds self to grid according to location.
     */
    public void assignPartition() {
        s.insertObjectIntoGrid(this, getPartitionX(), getPartitionY());
    }

    /**
     * Recalculates the partition indicies for this object.
     */
    public void recalculatePartition() {
        int newX = getPartitionX();
        int newY = getPartitionY();
        if (!inExpectedPartition()) {
            s.removeObjectFromGrid(this, currentPartitionX, currentPartitionY);
            s.insertObjectIntoGrid(this, newX, newY);
            currentPartitionX = newX;
            currentPartitionY = newY;
        }
    }

    /**
     * Checks to see if this object is in its expected partition.
     * @return
     */
    public boolean inExpectedPartition() {
        return (getPartitionX() == currentPartitionX && getPartitionY() == currentPartitionY);
    }

    /**
     * Attempts to resolves collision between any two PhysicsObjects in accordance with verlet integration.
     * @param s  Simulation
     * @param p1 PhysicsObject 1
     * @param p2 PhysicsObject 2
     */
    public static void verletCollisions(Simulation s, PhysicsObject p1, PhysicsObject p2) {
        Vector2f delta = new Vector2f(p1.getPos()).sub(p2.getPos());
        float dist = delta.length();
        float minDist = p1.getCollisionRadius() + p2.getCollisionRadius();

        if (dist < minDist && dist > 0) {
            float overlap = 0.5f * (minDist - dist);

            delta.normalize();
            delta.mul(overlap*s.CELL_REPULSION);

            p1.getPos().add(delta);
            p2.getPos().sub(delta);
            
        }
    }

    /**
     * Updates the position of the object shape by setting the center of the given shape to the current position.
     * This method is required because different shapes have different methods of setting center coordinates.
     */
    public abstract void updateShapePosition();

    public void setColor(Color c) {
        color = c;
        shape.setFill(color);
    }

    /**
     * Returns the partition x index the cell is located in.
     * @return partition x index
     */
    public int getPartitionX() {
        float halfW = s.ENV_WIDTH / 2f;
        int x = (int)((pos.x + halfW) / s.PARTITION_WIDTH);
        return Math.max(0, Math.min(x, s.GRID_COLS - 1));
    }

    /**
     * Returns the partition y index the cell is located in.
     * @return partition y index
     */
    public int getPartitionY() {
        float halfH = s.ENV_HEIGHT / 2f;
        int y = (int)((pos.y + halfH) / s.PARTITION_HEIGHT);
        return Math.max(0, Math.min(y, s.GRID_ROWS - 1));
    }

    public Color getColor() {
        return color;
    }

    public Vector2f getPos() {
        return pos;
    }

    public Vector2f getPrevPos() {
        return prevPos;
    }

    public float getCollisionRadius() {
        return collisionRadius;
    }

    public Node getShape() {
        return shape;
    }
}
