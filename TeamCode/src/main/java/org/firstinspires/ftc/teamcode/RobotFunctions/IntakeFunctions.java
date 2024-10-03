package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeFunctions {
    public void intakeAngle (Gamepad gamepad1, Gamepad gamepad2, Servo wrist, Telemetry telemetry){
        if (gamepad1.a) {
            wrist.setPosition(1);
        }
        if (gamepad1.b) {
            wrist.setPosition(0.5);
        }
        if (gamepad1.y) {
            wrist.setPosition(0);
        }
    }

    public void intakeSpin (Gamepad gamepad1, Gamepad gamepad2, Servo intake, Telemetry telemetry){
        if (gamepad1.dpad_up) {
            intake.setPosition(1);
        }
        if (gamepad1.dpad_down) {
            intake.setPosition(0);
        }
    }
}
