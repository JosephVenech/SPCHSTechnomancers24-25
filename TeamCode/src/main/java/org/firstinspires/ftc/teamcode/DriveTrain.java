package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;
@TeleOp(name="Mecanum Drive Train", group="Subsytems")
public class DriveTrain extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightBackDrive = null;

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

        org.firstinspires.ftc.teamcode.RobotFunctions.MecanumFunctions driveTrain = new org.firstinspires.ftc.teamcode.RobotFunctions.MecanumFunctions();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // driveTrain.driveTrainMath(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x, runtime);

            // Send calculated power to wheels
            leftFrontDrive.setPower(driveTrainVariables.driveTrainMotorPower[0]);
            rightFrontDrive.setPower(driveTrainVariables.driveTrainMotorPower[1]);
            leftBackDrive.setPower(driveTrainVariables.driveTrainMotorPower[2]);
            rightBackDrive.setPower(driveTrainVariables.driveTrainMotorPower[3]);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", driveTrainVariables.driveTrainMotorPower[0], driveTrainVariables.driveTrainMotorPower[1]);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", driveTrainVariables.driveTrainMotorPower[2], driveTrainVariables.driveTrainMotorPower[3]);
            telemetry.update();
        }
    }
}