package com.rowan.display;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * Controls view of simulation space.
 */
public class Camera {
    protected Scene scene;
    protected Pane root;

    /**
     * Creates a new Camera object with a provided scene and root pane.
     * @param scene Scene
     * @param root  Root Pane
     */
    public Camera(Scene scene, Pane root) {
        this.scene = scene;
        this.root = root;
    }

    /**
     * Sets the zoom scale of the simulation space.
     * @param zoomFactor Zoom Factor (0.5 = zoom out, 2 = zoom in)
     */
    public void setZoom(double zoomFactor) {
        root.setScaleX(zoomFactor);
        root.setScaleY(zoomFactor);
    }
}
