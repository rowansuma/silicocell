package com.rowan;

import com.rowan.display.Camera;
import com.rowan.simulation.Simulation;
import com.rowan.simulation.SimulationBorders;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

/**
 * Main file. Sets up simulation window.
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();

        Scene scene = new Scene(root, 600, 600, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);

        Pane simulationPane = new Pane();   // Holds all cells and borders
        Simulation s = new Simulation(scene, simulationPane);

        // Draw borders
        new SimulationBorders(s.ZOOM_FACTOR, s.ENV_WIDTH, s.ENV_HEIGHT, simulationPane);

        // Wrap in camera Pane
        Pane cameraPane = new Pane();
        cameraPane.getChildren().add(simulationPane);

        // **Translate simulationPane so that (0,0) is at the center**
        simulationPane.setTranslateX(scene.getWidth()/2.0);
        simulationPane.setTranslateY(scene.getHeight()/2.0);

        scene.setRoot(cameraPane);

        Camera c = new Camera(scene, cameraPane);
        c.setZoom(s.ZOOM_FACTOR);

        stage.setTitle("3D Molecule Simulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
