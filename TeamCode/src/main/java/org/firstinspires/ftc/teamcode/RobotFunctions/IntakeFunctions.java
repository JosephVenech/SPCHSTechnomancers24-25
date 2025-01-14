package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
This file controls all the functions for the intake, notably wrist and intake manual control.
Wrist manual control - When certain buttons are pressed go to set angles of the wrist, including
sample and specimen angles
Intake control - spin in to pick up game elements, stop moving to hold game element, spin in reverse
to release game element

    -River Perera
 */

public class IntakeFunctions {
    public void intakeAngle (Gamepad gamepad1, Gamepad gamepad2, Servo wristAngle, Servo intakeAngle, Telemetry telemetry){

        // When a is pressed go to vertical position to place specimen
        if (gamepad1.a) {
            wristAngle.setPosition(0.82);
            intakeAngle.setPosition(0.5);
        }

        // When b is pressed go to horizontal position: used for picking up game elements and
        // placing samples in basket
        if (gamepad1.b) {
            wristAngle.setPosition(0.3);
            intakeAngle.setPosition(0.3);
        }

        // Minimum position used for testing purposes
        if (gamepad1.y) {
            wristAngle.setPosition(0);
            intakeAngle.setPosition(0);
        }

        // Get position of the wrist and display on driver station for feedback
        double wristPosition = wristAngle.getPosition();
        double intakePosition = intakeAngle.getPosition();
        telemetry.addData("Wrist Position", wristPosition);
        telemetry.addData("Intake Angle", intakePosition);
    }

    public void intakeSpin (Gamepad gamepad1, Gamepad gamepad2, Servo intake, Telemetry telemetry){

        // Triggers are float with value 0-1 so when greater then 0 uses it to tell if pressed

        // when left trigger is pressed spin inwards to pick up game elements
        if (gamepad1.left_trigger > 0) {
            intake.setPosition(1);
        }

        // Right trigger pressed will release game elements
        else if (gamepad1.right_trigger > 0) {
            intake.setPosition(0);
        }

        // When neither trigger is pressed intake is in non-moving position
        else {
            intake.setPosition(0.5);
        }

        // Get intake position and display on driver station for feedback
        double intakePosition = intake.getPosition();
        telemetry.addData("Wrist Position", intakePosition);
    }
}
