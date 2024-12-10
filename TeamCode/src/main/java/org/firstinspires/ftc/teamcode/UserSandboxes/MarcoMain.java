/*

package org.firstinspires.ftc.teamcode.UserSandboxes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;

import org.firstinspires.ftc.robotcore.external.Telemetry;

// Call to update current motor power
public class MarcoMain {
    boolean movingForward;

    // Takes joystick inputs and calculates math for drivetrain motor power
    public void driveTrainMath(double left_stick_y, double left_stick_x, double right_stick_x, ElapsedTime runtime) {
        double max_speed;
        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        // Note: pushing stick forward gives negative value (-left_stick_y) = forward
        // Possible future change: replace game-pad inputs with inputs into function for
        // centralized adjustment of controls

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double axial   = -left_stick_y;  // Note: pushing stick forward gives negative value
        double lateral =  left_stick_x;
        double yaw     =  right_stick_x;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double leftFrontPower  = axial - lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower   = axial + lateral + yaw;
        double rightBackPower  = axial + lateral - yaw;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max_speed = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max_speed = Math.max(max_speed, Math.abs(leftBackPower));
        max_speed = Math.max(max_speed, Math.abs(rightBackPower));

        movingForward = (axial < 0);
        driveTrainVariables.current_time = runtime.milliseconds();

        // Update acceleration depending on the last update - current time
        if (driveTrainVariables.last_update - driveTrainVariables.current_time >= driveTrainVariables.update_period) {
            driveTrainVariables.last_update = driveTrainVariables.current_time;

            if (movingForward) {
                // Gradually increase acceleration up to the max forward power
                if (driveTrainVariables.driveTrainAcceleration < driveTrainVariables.max_forward_power) {
                    driveTrainVariables.driveTrainAcceleration += driveTrainVariables.speed_increment;
                    if (driveTrainVariables.driveTrainAcceleration > driveTrainVariables.max_forward_power) {
                        driveTrainVariables.driveTrainAcceleration = driveTrainVariables.max_forward_power;
                    }
                }
            } else {
                // Gradually decrease acceleration down to the max reverse power
                if (driveTrainVariables.driveTrainAcceleration > driveTrainVariables.max_reverse_power) {
                    driveTrainVariables.driveTrainAcceleration -= driveTrainVariables.speed_increment;
                    if (driveTrainVariables.driveTrainAcceleration < driveTrainVariables.max_reverse_power) {
                        driveTrainVariables.driveTrainAcceleration = driveTrainVariables.max_reverse_power;
                    }
                }
            }
        }

        // Sets all powers to max power if they are above the max power
        if (max_speed > driveTrainVariables.max_forward_power) {
            leftFrontPower /= max_speed;
            rightFrontPower /= max_speed;
            leftBackPower /= max_speed;
            rightBackPower /= max_speed;
        }

        // Set driveTrainMotorPower to the new powers
        //driveTrainVariables.driveTrainMotorPower[0] = (leftFrontPower * driveTrainVariables.driveTrainAcceleration) * driveTrainVariables.driveTrainSpeedMultiplier;
        //driveTrainVariables.driveTrainMotorPower[1] = (rightFrontPower * driveTrainVariables.driveTrainAcceleration) * driveTrainVariables.driveTrainSpeedMultiplier;
        //driveTrainVariables.driveTrainMotorPower[2] = (leftBackPower * driveTrainVariables.driveTrainAcceleration) * driveTrainVariables.driveTrainSpeedMultiplier;
        //driveTrainVariables.driveTrainMotorPower[3] = (rightBackPower * driveTrainVariables.driveTrainAcceleration) * driveTrainVariables.driveTrainSpeedMultiplier;
    }

    // Assigns power to the motors
    public void fullDriveTrainControl(Gamepad gamepad1, Gamepad gamepad2, DcMotor leftFrontDrive, DcMotor leftBackDrive, DcMotor rightFrontDrive, DcMotor rightBackDrive, ElapsedTime runtime, Telemetry telemetry) {

        driveTrainMath(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, runtime);

        // Send calculated power to motors
        //leftFrontDrive.setPower(driveTrainVariables.driveTrainMotorPower[0]);
        //rightFrontDrive.setPower(driveTrainVariables.driveTrainMotorPower[1]);
        //leftBackDrive.setPower(driveTrainVariables.driveTrainMotorPower[2]);
        //rightBackDrive.setPower(driveTrainVariables.driveTrainMotorPower[3]);

        // Telemetry data
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", driveTrainVariables.driveTrainMotorPower[0], driveTrainVariables.driveTrainMotorPower[1]);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", driveTrainVariables.driveTrainMotorPower[2], driveTrainVariables.driveTrainMotorPower[3]);
    }
}

 */