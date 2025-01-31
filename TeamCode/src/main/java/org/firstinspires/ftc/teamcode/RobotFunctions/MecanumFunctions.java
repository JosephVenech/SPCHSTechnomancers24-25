package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;
public class MecanumFunctions {
        public DcMotor leftFrontDrive = null;
        public DcMotor leftBackDrive = null;
        public DcMotor rightFrontDrive = null;
        public DcMotor rightBackDrive = null;
        // private Follower follower;
        private static double[] oldDriveTrainMotorPowers = new double[driveTrainVariables.driveTrainMotorPower.length];

        public void init(HardwareMap hardwareMap) {
            // follower = new Follower(hardwareMap);
            // follower.startTeleopDrive();

            leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
            leftBackDrive = hardwareMap.get(DcMotor.class, "left_rear_drive");
            rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
            rightBackDrive = hardwareMap.get(DcMotor.class, "right_rear_drive");
        }
        public double[] driveTrainMath(double left_stick_y, double left_stick_x, double right_stick_x) {
            double max;

            double axial   = -left_stick_y;
            double lateral =  left_stick_x;
            double yaw     =  right_stick_x;

            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            double[] motorPower;
            motorPower = new double[4];
            motorPower[0] = leftFrontPower;
            motorPower[1] = rightFrontPower;
            motorPower[2] = leftBackPower;
            motorPower[3] = rightBackPower;

            return motorPower;
        }
        public void updateOldMotorPowers() {
            for (int i = 0; i < driveTrainVariables.driveTrainMotorPower.length; i++) {
                oldDriveTrainMotorPowers[i] = driveTrainVariables.driveTrainMotorPower[i].getPower();
            }
        }
        public void updateTeleOpMovement(Gamepad gamepad1) {
            updateOldMotorPowers();

            /*for (int i = 0; i < driveTrainVariables.driveTrainMotorPower.length; i++) {
                if (driveTrainVariables.driveTrainMotorPower[i].getPower() > driveTrainVariables.driveTrainMaxPower) {
                    driveTrainVariables.driveTrainMotorPower[i].setPower(driveTrainVariables.driveTrainMaxPower);
                } else if (driveTrainVariables.driveTrainMotorPower[i].getPower() < -driveTrainVariables.driveTrainMaxPower) {
                    driveTrainVariables.driveTrainMotorPower[i].setPower(-driveTrainVariables.driveTrainMaxPower);
                }
            }*/

            double[] motorPower = driveTrainMath(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            for (int i = 0; i < driveTrainVariables.driveTrainMotorPower.length; i++) {
                double targetPower = motorPower[i];
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
            }
        }
    }
