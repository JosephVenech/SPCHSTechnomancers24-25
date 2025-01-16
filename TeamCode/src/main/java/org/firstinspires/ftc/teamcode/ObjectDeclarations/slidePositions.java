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
    public static int hangSpecimen = 2720;
    public static int sampleBasket = 7831;
    public static int highSample = 3300;
    public static int collectSample = 3300;
    public static int observationDeck = 2230;
    public static int collectSpecimen = 2230;
    public static int climbStageOne = 5; //1.399
    public static int climbStageTwo = 5;
    public static int climbStageThree = 4015;
    public static int climbStageFour = 4127;
    public static int climbStageFive = 2609;
    public static int climbStageSix = 734;
    public static int climbStageSeven = 700;
    public static int climbStageEight = 185;
}
