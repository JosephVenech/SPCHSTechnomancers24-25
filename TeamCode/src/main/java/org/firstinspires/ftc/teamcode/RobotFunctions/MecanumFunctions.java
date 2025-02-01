package org.firstinspires.ftc.teamcode.RobotFunctions;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.ObjectDeclarations.armPositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.liftSystemPositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.slidePositions;

public class MecanumFunctions {
        private static double[] oldDriveTrainMotorPowers = new double[driveTrainVariables.driveTrainMotorPower.length];

        public void init(HardwareMap hardwareMap) {

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

        public void driveTrainSafety(DcMotor slideMotor, DcMotor armMotor, DcMotor leftLiftSystem, DcMotor rightLiftSystem, TouchSensor slideSafety, TouchSensor leftSafety, TouchSensor rightSafety, Gamepad gamepad2) {
            /*if (abs(slideMotor.getCurrentPosition() - slideMotor.getTargetPosition()) < 2){
                    slideMotor.setPower(0);
                } else {
                    slideMotor.setPower(slidePositions.motorSpeed);
                }
                /if (abs(armMotor.getCurrentPosition() - armMotor.getTargetPosition()) < 2){
                    armMotor.setPower(0);
                } else {
                    armMotor.setPower(armPositions.motorSpeed);
                }

                if ((leftLiftSystem.getTargetPosition() == 0) && leftSafety.isPressed()){
                    leftLiftSystem.setPower(0);
                } else {
                    leftLiftSystem.setPower(liftSystemPositions.liftMotorPower);
                }

                if ((rightLiftSystem.getTargetPosition() == 0) && rightSafety.isPressed()){
                    rightLiftSystem.setPower(0);
                } else {
                    rightLiftSystem.setPower(liftSystemPositions.liftMotorPower);
            }*/
            slideMotor.setPower( ( abs(slideMotor.getCurrentPosition() - slideMotor.getTargetPosition()) < 2 ) ? 0 : slidePositions.motorSpeed);
            armMotor.setPower( ( abs(armMotor.getCurrentPosition() - armMotor.getTargetPosition()) < 2 ) ? 0 : armPositions.motorSpeed);
            leftLiftSystem.setPower( ( (leftLiftSystem.getTargetPosition() == 0) && leftSafety.isPressed() ) ? 0 : liftSystemPositions.liftMotorPower);
            rightLiftSystem.setPower( ( (rightLiftSystem.getTargetPosition() == 0) && rightSafety.isPressed() ) ? 0 : liftSystemPositions.liftMotorPower);

            if (slideMotor.getTargetPosition() < 20 && ((slideMotor.getCurrentPosition() > 20 && slideSafety.isPressed()) || gamepad2.guide)) {
                slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (slideMotor.getTargetPosition() < 20 && (abs(slideMotor.getTargetPosition() - slideMotor.getCurrentPosition()) > 2)) {
                gamepad2.rumble(gamepad2.left_trigger, gamepad2.right_trigger, Gamepad.RUMBLE_DURATION_CONTINUOUS);
            } else {
                gamepad2.stopRumble();
            }
        }
    }
