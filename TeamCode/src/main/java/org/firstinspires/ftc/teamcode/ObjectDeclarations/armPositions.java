package org.firstinspires.ftc.teamcode.ObjectDeclarations;

/*
This file declares from the starting position how many encoder ticks the arm of our bot has to
move to get to the angle for each preset position.
Positions are measured from starting position using a manual control tele-op then recorded in this
file.
Starting position is set in ResetPosition.java
Motor Speed variable determines max speed for the motor to run at.

Having this file separate is important as it allows us easily change the numbers if needed.

Eventually I plan to convert all these numbers to actual angle measurements to help improve
readability.
    -River Perera
 */
public class armPositions {
    public static double motorSpeed = 0.95;
    public static int startingPosition = 0;
    public static int hangSpecimen = 4108;
    public static int sampleBasket = 5001;
    public static int safeReturnTransition = 5200;
    public static int travelPosition = 1982;
    public static int highSample = 1222;
    public static int collectSample = 843;
    public static int observationDeck = 1684;
    public static int collectSpecimen = 2184;
    public static int climbStageOne = 4026;
    public static int stageOneLift = -1201;
}
