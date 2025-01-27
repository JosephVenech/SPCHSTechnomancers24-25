package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

// Call to update current motor power
public class MecanumFunctions {
        private Follower follower;
        private static double[] oldDriveTrainMotorPowers = new double[driveTrainVariables.driveTrainMotorPower.length];

        public void init(HardwareMap hardwareMap) {
            follower = new Follower(hardwareMap);
            follower.startTeleopDrive();
        }

        public void updateOldMotorPowers() {
            for (int i = 0; i < driveTrainVariables.driveTrainMotorPower.length; i++) {
                oldDriveTrainMotorPowers[i] = driveTrainVariables.driveTrainMotorPower[i].getPower();
            }
        }
        public void updateTeleOpMovement(Gamepad gamepad1) {
            // updateOldMotorPowers();

            follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            follower.update();

            /*for (int i = 0; i < driveTrainVariables.driveTrainMotorPower.length; i++) {
                if (driveTrainVariables.driveTrainMotorPower[i].getPower() > driveTrainVariables.driveTrainMaxPower) {
                    driveTrainVariables.driveTrainMotorPower[i].setPower(driveTrainVariables.driveTrainMaxPower);
                } else if (driveTrainVariables.driveTrainMotorPower[i].getPower() < -driveTrainVariables.driveTrainMaxPower) {
                    driveTrainVariables.driveTrainMotorPower[i].setPower(-driveTrainVariables.driveTrainMaxPower);
                }
            }

            for (int i = 0; i < driveTrainVariables.driveTrainMotorPower.length; i++) {
                double targetPower = driveTrainVariables.driveTrainMotorPower[i].getPower();
                double currentPower = oldDriveTrainMotorPowers[i];
                double powerDifference = targetPower - currentPower;

                if (powerDifference > driveTrainVariables.acceleration_rate) {
                    oldDriveTrainMotorPowers[i] += driveTrainVariables.acceleration_rate;
                } else if (powerDifference < -driveTrainVariables.acceleration_rate) {
                    oldDriveTrainMotorPowers[i] -= driveTrainVariables.acceleration_rate;
                } else {
                    oldDriveTrainMotorPowers[i] = targetPower;
                }

                double adjustedPower = Math.max( -driveTrainVariables.driveTrainMaxPower, Math.min( driveTrainVariables.driveTrainMaxPower, oldDriveTrainMotorPowers[i] ) );
                driveTrainVariables.driveTrainMotorPower[i].setPower(adjustedPower);
            }*/
        }
    }
