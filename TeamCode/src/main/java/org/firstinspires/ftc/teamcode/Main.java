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
    public DcMotor armMotor = null;
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
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");

        // Sensors
        slideSafety = hardwareMap.get(TouchSensor.class, "slide_safety");

        // Set wheels to move forward
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);
        slideMotor.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        MecanumFunctions driveTrain = new MecanumFunctions();
        SlideFunctions slideControl = new SlideFunctions();

        double buttonPressed = 0;

        if (opModeIsActive()) {
            while(opModeIsActive()){

                                // Functions - Comments can be found in individual files //

                driveTrain.fullDriveTrainControl(gamepad1, gamepad2, leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive, telemetry, runtime);
                slideControl.SlidePosition(gamepad1, gamepad2, slideMotor, slideSafety, telemetry, runtime);
                slideControl.ArmPosition(gamepad1, gamepad2, armMotor);

                                // End of function calls //


                // Telemetry data
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.addData("Button press","%4.2f", buttonPressed);
                telemetry.update();



            }
        }
    }
}





