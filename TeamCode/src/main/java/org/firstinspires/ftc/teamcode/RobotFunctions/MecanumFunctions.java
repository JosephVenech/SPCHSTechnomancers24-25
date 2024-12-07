package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

// Call to update current motor power
public class MecanumFunctions {
        // boolean movingForward;
        private Follower follower;

        public void init(HardwareMap hardwareMap) {
            follower = new Follower(hardwareMap);
            follower.setMaxPower(driveTrainVariables.max_forward_power);
            follower.startTeleopDrive();
        }
        public void updateTeleOpMovement(Gamepad gamepad1) {
            follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            follower.update();

            // Telemetry data
            //telemetry.addData("Front left/Right", "%4.2f, %4.2f", driveTrainVariables.driveTrainMotorPower[0], driveTrainVariables.driveTrainMotorPower[1]);
            //telemetry.addData("Back  left/Right", "%4.2f, %4.2f", driveTrainVariables.driveTrainMotorPower[2], driveTrainVariables.driveTrainMotorPower[3]);
        }
    }
