package org.firstinspires.ftc.teamcode.ObjectDeclarations;

/*
This file declares from the starting position how many encoder ticks the slide of our bot has to
move to get to the angle for each preset position.
Positions are measured from starting position using a manual control tele-op then recorded in this
file.
Motor Speed variable determines max speed for the motor to run at.

Having this file separate is important as it allows us easily change the numbers if needed.

Eventually I plan to convert all these numbers to length measurements (inch/cm) to help improve
readability.
    -River Perera
 */

public class slidePositions {
    public static double motorSpeed = 0.7;
    public static int startingPosition = 0;
    public static int hangSpecimen = 2720;
    public static int sampleBasket = 5424;
    public static int travelPosition = 0;
    public static int highSample = 2258;
    public static int collectSample = 2252;
    public static int observationDeck = 2230;
    public static int collectSpecimen = 2230;
    public static int climbStageOne = -228;
    public static int stageOneLift = -211;
}
