package com.rowan.utils;

import org.joml.Vector2f;

/**
 * Miscellaneous utility functions.
 */
public class Utils {
    /**
     * Creates a randomized Vector2f within the bounds of a maximum square but excluding the region of a minumum square.
     * @param minDist   Apothem of the minumum square (1/2 of the minimum side length)
     * @param maxDist   Apothem of the maximum square (1/2 of the maximum side length)
     * @return randomized Vector2f
     */
    public static Vector2f randomSquareVector2f(float minDist, float maxDist) {
        float x = randomRange(minDist, maxDist)*(new int[]{-1, 1}[randomInt(0, 2)]);
        float y = randomRange(minDist, maxDist)*(new int[]{-1, 1}[randomInt(0, 2)]);
        return new Vector2f(x, y);
    }

    /**
     * Creates a randomized Vector2f within the bounds of a square.
     * @param maxDist   Apothem of the maximum square (1/2 of the maximum side length)
     * @return randomized Vector2f
     */
    public static Vector2f randomSquareVector2f(float maxDist) {
        return randomSquareVector2f(0, maxDist);
    }

    /**
     * Creates a randomized Vector2f within the bounds of a maximum circle but excluding the region of a minimum circle.
     * @param minDist   Minimum length of the vector
     * @param maxDist   Maximum length of the vector
     * @return randomized Vector2f
     */
    public static Vector2f randomCircleVector2f(float minDist, float maxDist) {
        float angle = randomRange(0f, (float)(2*Math.PI));
        float magnitude = randomRange(minDist, maxDist);
        return new Vector2f((float)(magnitude*Math.cos(angle)), (float)(magnitude*Math.sin(angle)));
    }

     /**
     * Creates a randomized Vector2f within the bounds of a circle.
     * @param maxDist   Maximum length of the vector
     * @return randomized Vector2f
     */
    public static Vector2f randomCircleVector2f(float maxDist) {
        return randomCircleVector2f(0, maxDist);
    }

    /**
     * Creates a random float between two values.
     * @param min   minumum value
     * @param max   maximum value
     * @return random value
     */
    public static float randomRange(float min, float max) {
        return (float)(min + Math.random() * (max - min));
    }

    /**
     * Creates a random int between two values.
     * @param min   minumum value
     * @param max   maximum value
     * @return random value
     */
    public static int randomInt(int min, int max) {
        return (int)(min + Math.random() * (max - min));
    }
}
