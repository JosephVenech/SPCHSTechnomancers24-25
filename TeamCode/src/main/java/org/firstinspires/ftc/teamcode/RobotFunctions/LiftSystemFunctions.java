package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LiftSystemFunctions {
    public void LiftSystemControl(Gamepad gamepad1, Gamepad gamepad2, DcMotor leftLiftSystem, DcMotor rightLiftSystem, Telemetry telemetry) {
        if (gamepad1.left_bumper == true) {
            leftLiftSystem.setPower(-1);
        }
        else {
            leftLiftSystem.setPower(gamepad1.left_trigger);
        }
        if (gamepad1.right_bumper == true) {
            rightLiftSystem.setPower(-1);
        }
        else {
            rightLiftSystem.setPower(gamepad1.right_trigger);
        }

        if (gamepad1.dpad_up == true) {
            leftLiftSystem.setPower(1);
            rightLiftSystem.setPower(1);
        } else if (gamepad1.dpad_down == true) {
            leftLiftSystem.setPower(-1);
            rightLiftSystem.setPower(-1);
        }

        telemetry.addData("Left lift power:", leftLiftSystem.getPower());
        telemetry.addData("Right lift power:", rightLiftSystem.getPower());
    }
}
