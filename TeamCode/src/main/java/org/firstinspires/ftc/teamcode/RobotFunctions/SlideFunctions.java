package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
This file handles manual control fo the slide and arm
 */

public class SlideFunctions {

    public void SlidePosition(Gamepad gamepad1, Gamepad gamepad2, DcMotor slideMotor, String slideSafety, Telemetry telemetry){

        double slidePowerConst = 0.95; // Set max speed of slide
        double slidePower = -gamepad2.left_stick_y; // Input for slide control

        // If slide safety is activated prevent slide from retracting
        if (slideSafety.isPressed() && slidePower < 0 ){
            slidePower = 0;
        }

        slideMotor.setPower(slidePower * slidePowerConst); // Send power to motor

        // Get position and display power of motor and current position on driver station
        int slidePosition = slideMotor.getCurrentPosition();
        telemetry.addData("Slide power","%4.2f", slidePower);
        telemetry.addData("Slide Position", slidePosition);
    }

    public void ArmPosition(Gamepad gamepad1, Gamepad gamepad2, DcMotor armMotor, Telemetry telemetry) {

        double armPower = -gamepad2.right_stick_y; // Input for arm control
        armMotor.setPower(armPower); // Set power to the arm

        // Get position and display power of motor and current position on driver station
        int armPosition = armMotor.getCurrentPosition();

        telemetry.addData("Arm power","%4.2f", armPower);
        telemetry.addData("Arm Position", armPosition);
    }


}
