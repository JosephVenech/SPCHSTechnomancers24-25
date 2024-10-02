package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SlideFunctions {

    public void SlidePosition(Gamepad gamepad1, Gamepad gamepad2, DcMotor slideMotor, TouchSensor slideSafety, Telemetry telemetry){

        double slidePowerConst = 0.95; // Set max speed of slide
        double slidePower = -gamepad2.left_stick_y;

        // If slide safety is activated prevent slide from retracting
        if (slideSafety.isPressed() && slidePower < 0 ){
            slidePower = 0;
        }

        slideMotor.setPower(slidePower * slidePowerConst);

        int slidePosition = slideMotor.getCurrentPosition();

        telemetry.addData("Slide power","%4.2f", slidePower);
        telemetry.addData("Slide Position", slidePosition);
    }

    public void ArmPosition(Gamepad gamepad1, Gamepad gamepad2, DcMotor armMotor, Telemetry telemetry) {
        double armPower = gamepad2.right_stick_y;
        armMotor.setPower(armPower);

        int armPosition = armMotor.getCurrentPosition();

        telemetry.addData("Arm power","%4.2f", armPower);
        telemetry.addData("Arm Position", armPosition);
    }


}
