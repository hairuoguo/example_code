/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.ArrayList;

public class TurtleSoup {
    static final double DEGREES_IN_CIRCLE = 360.0;
    static final double DEGREES_IN_RIGHT_ANGLE = 90.0;
    static final double DEGREES_IN_HALF_CIRCLE = DEGREES_IN_CIRCLE/2;

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        final int numSidesSquare = 4;
        for (int i = 0; i < numSidesSquare; i++){
            turtle.forward(sideLength);
            turtle.turn(DEGREES_IN_RIGHT_ANGLE);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        return DEGREES_IN_HALF_CIRCLE*(sides - 2)/sides; // formula for interior angles that is a double due to the 180
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        long sides = Math.round(DEGREES_IN_CIRCLE/(DEGREES_IN_HALF_CIRCLE - angle)); //round to long first b/c we aren't given a specified upper bound for number of sides
        if (sides < Integer.MAX_VALUE){
            return (int) sides;
        }else{//if sides is too big to be an int, throw an unchecked exception
            throw new RuntimeException("Number of sides exceeds 32 bit integer!");
        }
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double interiorAngle = calculateRegularPolygonAngle(sides);
        for (int i = 0; i < sides; i++){
            turtle.forward(sideLength); 
            turtle.turn(DEGREES_IN_HALF_CIRCLE - interiorAngle); 
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {
        double deltaY = (double) (targetY - currentY); // distance between Y coordinates
        double deltaX = (double) (targetX - currentX); // distance between X coordinates
        double newHeading = DEGREES_IN_RIGHT_ANGLE - Math.toDegrees(Math.atan(deltaY/deltaX));// converting from regular degrees to cardinal
        return (DEGREES_IN_CIRCLE + newHeading - currentHeading) % DEGREES_IN_CIRCLE; //calculating distance in heading, making sure within range of 0-360
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> deltaHeadings = new ArrayList<Double>(); //List for difference between headings
        double deltaHeading; // difference between headings
        double currentHeading = 0; //to keep track of current heading
        for (int i = 0; i < xCoords.size() - 1; i++){
            deltaHeading = calculateHeadingToPoint(currentHeading, xCoords.get(i), yCoords.get(i), xCoords.get(i+1), yCoords.get(i+1)); 
            deltaHeadings.add(deltaHeading); // adding calculated heading to list
            currentHeading = (deltaHeading + currentHeading) % DEGREES_IN_CIRCLE; // adding the change to get our new currentHeading, making sure within range of 0-360
        }
        return deltaHeadings;
    }

    /**
     * This method draws the anarchist symbol.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        //NOTE: All magic number arguments in calls to the drawThickEvenWeightLine method are line lengths determined by aesthetics (hold no real significance)
        final int sidesInTriangle = 3;
        final double regTriangleAngle = calculateRegularPolygonAngle(sidesInTriangle);
        final int numLegsOfSymble = 2;
        final int thickLineHalfWeight = 5;
        final int approxCircleCircum = 750;
        
        turtle.turn(DEGREES_IN_RIGHT_ANGLE);
        drawRegularPolygon(turtle, approxCircleCircum, 1); 
        turtle.turn(regTriangleAngle);
        turtle.color(PenColor.RED);
        for (int i = 0; i < numLegsOfSymble; i++ ){ // drawing legs
            drawThickEvenWeightLine(turtle, thickLineHalfWeight, 250);
            turtle.turn(DEGREES_IN_HALF_CIRCLE);
            drawThickEvenWeightLine(turtle, thickLineHalfWeight, 30);
            turtle.turn(DEGREES_IN_HALF_CIRCLE + regTriangleAngle);
        }
        turtle.turn(DEGREES_IN_HALF_CIRCLE + regTriangleAngle); // drawing bar
        turtle.forward(115);
        turtle.turn(DEGREES_IN_HALF_CIRCLE - regTriangleAngle);
        drawThickEvenWeightLine(turtle, thickLineHalfWeight, 210);
        turtle.turn(DEGREES_IN_HALF_CIRCLE);
        drawThickEvenWeightLine(turtle, thickLineHalfWeight, 105);
    }
    
    /**
     * Custom method that creates a line of thickness <code>int half_thickness*2</code> and length  <code>int length</code>.
     * 
     * @param turtle: instance of class Turtle that is doing the drawing
     * @param half_thickness: half the thickness of the line measured in same units as distance
     * @param length: length of the line
     */
    
    public static void drawThickEvenWeightLine(Turtle turtle, int half_thickness, int length){
        turtle.turn(DEGREES_IN_RIGHT_ANGLE); //we first move to the right so that we center the line's thickness
        turtle.forward(half_thickness);
        turtle.turn(DEGREES_IN_CIRCLE - DEGREES_IN_RIGHT_ANGLE);
        for (int i = 0; i < half_thickness*2; i ++){ // draw thicker line by drawing multiple lines and then shifting to the left
            drawRegularPolygon(turtle, 2, length);
            turtle.turn(DEGREES_IN_CIRCLE - DEGREES_IN_RIGHT_ANGLE);
            turtle.forward(1);
            turtle.turn(DEGREES_IN_RIGHT_ANGLE);
        }
        turtle.turn(DEGREES_IN_RIGHT_ANGLE); //return the turtle to the same location and orientation that we started from
        turtle.forward(half_thickness);
        turtle.turn(DEGREES_IN_CIRCLE - DEGREES_IN_RIGHT_ANGLE);
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
   
    
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();
        drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
    }

}
