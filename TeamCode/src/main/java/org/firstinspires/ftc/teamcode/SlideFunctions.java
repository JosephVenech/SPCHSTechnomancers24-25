package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SlideFunctions {

    public void SlidePosition(Gamepad gamepad1, Gamepad gamepad2, DcMotor slideMotor, TouchSensor slideSafety, Telemetry telemetry){

        double slidePowerConst = 0.7;
        double slidePower = -gamepad2.left_stick_y;

        if (!slideSafety.isPressed() || slidePower > 0){
            slideMotor.setPower(slidePower * slidePowerConst);
        }


        telemetry.addData("Slide power","%4.2f", slidePower);
    }

    public void ArmPosition(Gamepad gamepad1, Gamepad gamepad2, DcMotor armMotor, Telemetry telemetry) {
        double armPower = gamepad2.right_stick_y;
        armMotor.setPower(armPower);

        telemetry.addData("Arm power","%4.2f", armPower);
    }


}
