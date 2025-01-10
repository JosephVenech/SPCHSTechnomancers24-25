package org.firstinspires.ftc.teamcode.ObjectDeclarations;

/*
This file declares 3 position for a contious servo to run at to control the intake.
intakeOn is used to pick up game elements, off to hold a game element, and reverse to release

These are used with standard servo instead of continous servo to reduce variables needed however,
this reduces max speed but that is not a problem for us

    -River Perera
 */

public class intakePositions {

    // Left servo, one with cut gecko wheel
    // Continuous mode
    public static double leftIntakeOn = 0;
    public static double leftIntakeOff = 0.5;
    public static double leftIntakeReverse = 1;


    // Right servo, uncut gecko wheel and intake wheel
    // Continuous mode
    public static double rightIntakeOn = 0;
    public static double rightIntakeOff = 0.5;
    public static double rightIntakeReverse = 1;

}
