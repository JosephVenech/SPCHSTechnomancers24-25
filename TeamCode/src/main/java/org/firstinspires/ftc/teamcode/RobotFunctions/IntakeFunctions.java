package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeFunctions {
    public void intakeAngle (Gamepad gamepad1, Gamepad gamepad2, Servo wrist, Telemetry telemetry){
        if (gamepad1.a) {
            wrist.setPosition(0.82);
        }
        if (gamepad1.b) {
            wrist.setPosition(0.3);
        }
        if (gamepad1.y) {
            wrist.setPosition(0);
        }

        double wristPosition = wrist.getPosition();

        telemetry.addData("Wrist Position", wristPosition);
    }

    public void intakeSpin (Gamepad gamepad1, Gamepad gamepad2, Servo intake, Telemetry telemetry){
        if (gamepad1.dpad_up) {
            intake.setPosition(1);
        }
        if (gamepad1.dpad_down) {
            intake.setPosition(0);
        }

        double intakePosition = intake.getPosition();

        telemetry.addData("Wrist Position", intakePosition);
    }
}
