package com.rowan.simulation;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Defines and creates borders around the simulation space.
 */
public class SimulationBorders {
    public final Color color = Color.WHITE;
    public final float thickness;

    /**
     * Creates new SimulationBorders given a width, height, zoom factor, and a root group.
     * @param zoomFactor    Zoom Factor
     * @param width         Width
     * @param height        Height
     * @param root          Root Pane
     */
    public SimulationBorders(float zoomFactor, float width, float height, Pane root) {
        thickness = 0.4f/zoomFactor;
        float halfW = width / 2f;
        float halfH = height / 2f;

        Line top = new Line(-halfW, -halfH, halfW, -halfH);
        Line bottom = new Line(-halfW, halfH, halfW, halfH);
        Line left = new Line(-halfW, -halfH, -halfW, halfH);
        Line right = new Line(halfW, -halfH, halfW, halfH);

        top.setStroke(color);
        bottom.setStroke(color);
        left.setStroke(color);
        right.setStroke(color);

        top.setStrokeWidth(thickness);
        bottom.setStrokeWidth(thickness);
        left.setStrokeWidth(thickness);
        right.setStrokeWidth(thickness);

        root.getChildren().addAll(top, bottom, left, right);
    }

}
