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
    public static int travelPosition = 1429;
    public static int startingPosition = 3750;
    public static int hangSpecimen = 4031;
    public static int lockSpecimen = 8050;
    public static int sampleBasket = 4084; //4084
    public static int safeReturnTransition = 3900;
    public static int highSample = 400;
    public static int collectSample = -50;
    public static int observationDeck = 266;
    public static int collectSpecimen = 735;
    public static int collectSpecimenTwo = 900;
    public static int climbStageOne = 3307; // 1.394
    public static int climbStageTwo = 2691;
    public static int climbStageThree = 2660;
    public static int climbStageFour = 1952;
    public static int climbStageFive = 3329;
    public static int climbStageSix = 6829;
    public static int climbStageSeven = 7140;
    public static int climbStageEight = 2091;

}
