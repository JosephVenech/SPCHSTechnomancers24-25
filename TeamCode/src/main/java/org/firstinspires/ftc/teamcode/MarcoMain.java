package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

abstract class driveTrainFunctions {
    abstract double[] driveTrainMath(double left_stick_y, double left_stick_x, double right_stick_x);
}

class driveFunctions extends driveTrainFunctions {

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
}

@TeleOp()
public class MarcoMain extends LinearOpMode {
    // Declare OpMode members for each of the 4 motors.
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightBackDrive = null;

    driveTrainFunctions drivetrainFunctions = new driveFunctions();

    @Override
    public void runOpMode() {
        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        // Set wheels to move forward
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Call the abstract function and update it
            double[] motorPower = drivetrainFunctions.driveTrainMath(
                    gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x
            );
            // Send calculated power to wheels
            leftFrontDrive.setPower(motorPower[0]);
            rightFrontDrive.setPower(motorPower[1]);
            leftBackDrive.setPower(motorPower[2]);
            rightBackDrive.setPower(motorPower[3]);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", motorPower[0], motorPower[1]);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", motorPower[2], motorPower[3]);
            telemetry.update();
        }
    }
}
