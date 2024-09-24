package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;


@TeleOp
public class Main extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor slideMotor = null;
    public TouchSensor slideSafety = null;

    @Override
    public void runOpMode() throws InterruptedException {
        //DriveTrain Motors
        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        // Motors
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        slideMotor = hardwareMap.get(DcMotor.class, "slide_motor");

        // Sensors
        slideSafety = hardwareMap.get(TouchSensor.class, "slide_safety");

        // Set wheels to move forward
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        MecanumFunctions driveTrain = new MecanumFunctions();
        SlideFunctions slideControl = new SlideFunctions();

        if (opModeIsActive()) {
            while(opModeIsActive()){

                                // Functions - Comments can be found in individual files //

                double[] motorPower = driveTrain.driveTrainMath(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);
                double slidePower = slideControl.SlidePosition(gamepad1.right_trigger, gamepad1.left_trigger, slideSafety);

                                // End of function calls //



                // Send calculated power to motors
                leftFrontDrive.setPower(motorPower[0]);
                rightFrontDrive.setPower(motorPower[1]);
                leftBackDrive.setPower(motorPower[2]);
                rightBackDrive.setPower(motorPower[3]);
                slideMotor.setPower(slidePower);

                // Telemetry data
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.addData("Front left/Right", "%4.2f, %4.2f", motorPower[0], motorPower[1]);
                telemetry.addData("Back  left/Right", "%4.2f, %4.2f", motorPower[2], motorPower[3]);
                telemetry.addData("Slide power","%4.2f", slidePower);
                telemetry.update();



            }
        }
    }
}





