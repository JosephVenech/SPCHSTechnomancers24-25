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
    public static double motorSpeed = 1;
    public static int startingPosition = 0; // -224
    public static int travelPosition = 0;
    public static int hangSpecimen = 762;
    public static int lockSpecimen = -10;
    public static int sampleBasket = 6365;
    public static int highSample = 6270;
    public static int collectSample = 6270;
    public static int observationDeck = 1315;
    public static int collectSpecimen = 1315;
    public static int climbStageOne = 5; //1.399
    public static int climbStageTwo = 5;
    public static int climbStageThree = 7628;
    public static int climbStageFour = 7841;
    public static int climbStageFive = 4957;
    public static int climbStageSix = 1395;
    public static int climbStageSeven = 1330;
    public static int climbStageEight = 352;
}
