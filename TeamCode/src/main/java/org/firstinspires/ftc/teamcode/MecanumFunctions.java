package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

// Call to update current motor power
    public class MecanumFunctions extends driveTrainFunctions {

        // Calculates the math for the joystick
        double[] driveTrainMath(double left_stick_y, double left_stick_x, double right_stick_x) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            // Note: pushing stick forward gives negative value (-left_stick_y) = forward
            // Possible future change: replace game-pad inputs with inputs into function for
            // centralized adjustment of controls

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = -left_stick_y + left_stick_x + right_stick_x;
            double rightFrontPower = -left_stick_y - left_stick_x - right_stick_x;
            double leftBackPower   = -left_stick_y - left_stick_x + right_stick_x;
            double rightBackPower  = -left_stick_y + left_stick_x - right_stick_x;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // Send calculated power to wheels
            // leftFrontDrive.setPower(leftFrontPower);
            // rightFrontDrive.setPower(rightFrontPower);
            // leftBackDrive.setPower(leftBackPower);
            // rightBackDrive.setPower(rightBackPower);

            double[] motorPower;
            motorPower = new double[4];
            motorPower[0] = leftFrontPower;
            motorPower[1] = rightFrontPower;
            motorPower[2] = leftBackPower;
            motorPower[3] = rightBackPower;

            return motorPower;
        }

        // Assigns power to the motors


        public void fullDriveTrainControl(Gamepad gamepad1, Gamepad gamepad2, DcMotor leftFrontDrive, DcMotor leftBackDrive, DcMotor rightFrontDrive, DcMotor rightBackDrive, Telemetry telemetry, ElapsedTime runtime) {
            double[] motorPower = driveTrainMath(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);

            // End of function calls //



            // Send calculated power to motors
            leftFrontDrive.setPower(motorPower[0]);
            rightFrontDrive.setPower(motorPower[1]);
            leftBackDrive.setPower(motorPower[2]);
            rightBackDrive.setPower(motorPower[3]);

            // Telemetry data
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", motorPower[0], motorPower[1]);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", motorPower[2], motorPower[3]);
            telemetry.update();
        }
    }
