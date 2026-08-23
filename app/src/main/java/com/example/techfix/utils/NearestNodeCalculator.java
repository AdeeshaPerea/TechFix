package com.example.techfix.utils;

import android.location.Location;

public class NearestNodeCalculator {

    // Node A
    public static final double NODE_A_LAT = 6.033583173843611;
    public static final double NODE_A_LON = 80.21508210852444;

    // Node B
    public static final double NODE_B_LAT = 6.912341603690496;
    public static final double NODE_B_LON = 79.85120140667972;

    public static class Node {
        public String name;
        public double lat;
        public double lon;

        public Node(String name, double lat, double lon) {
            this.name = name;
            this.lat = lat;
            this.lon = lon;
        }
    }

    /**
     * Returns the closest node (A or B) to the user's current location.
     */
    public static Node getNearestNode(double userLat, double userLon) {
        float[] resultsA = new float[1];
        Location.distanceBetween(userLat, userLon, NODE_A_LAT, NODE_A_LON, resultsA);
        float distanceToA = resultsA[0];

        float[] resultsB = new float[1];
        Location.distanceBetween(userLat, userLon, NODE_B_LAT, NODE_B_LON, resultsB);
        float distanceToB = resultsB[0];

        if (distanceToA <= distanceToB) {
            return new Node("Node A", NODE_A_LAT, NODE_A_LON);
        } else {
            return new Node("Node B", NODE_B_LAT, NODE_B_LON);
        }
    }
}
