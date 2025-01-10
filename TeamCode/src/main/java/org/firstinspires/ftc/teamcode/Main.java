package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sfdev.assembly.state.StateMachine;

import org.firstinspires.ftc.teamcode.ObjectDeclarations.armPositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.slidePositions;
import org.firstinspires.ftc.teamcode.RobotFunctions.ColorSensorFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.IntakeFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.MecanumFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.Robot;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineFunctions;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;

import java.util.Map;


@TeleOp(name="Main", group="Main")
public class Main extends LinearOpMode {
    public ElapsedTime runtime = new ElapsedTime();

    public DcMotor leftFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor slideMotor = null;
    public DcMotor armMotor = null;
    public Servo wristAngleServo = null;
    public Servo leftIntakeServo = null;
    public Servo rightIntakeServo = null;
    public Servo intakeAngleServo = null;
    public TouchSensor slideSafety = null;
    public NormalizedColorSensor intakeColorSensor = null;
    public String intakeSampleColor = "Null";
    public Boolean isBlueAlliance = false; // Default team is red alliance
    public Boolean xButtonCurrentlyPressed = false;
    public Boolean xButtonPreviouslyPressed = false;
    public double intakeSampleState = 0;

    private Follower follower;
    private final Pose startPose = new Pose(0,0,0);

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap);
        Map<String, DcMotor> motors = robot.getDriveDictionary();
        Map<String, Servo> servos = robot.getServoDictionary();
        Map<String, String> misc = robot.getMiscDictionary();

        mapVariables(motors, servos, misc);

        MecanumFunctions driveTrain = new MecanumFunctions();
        driveTrain.init(hardwareMap);
        IntakeFunctions intakeControl = new IntakeFunctions();
        StateMachineFunctions stateMachine = new StateMachineFunctions();
        ColorSensorFunctions colorSensorFunctions = new ColorSensorFunctions();

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();



        while (!opModeIsActive()) {

            telemetry.addData("Press game pad 2 X to toggle team alliance", "Current: " + (isBlueAlliance ? "Red" : "Blue"));

            xButtonCurrentlyPressed = gamepad2.x;

            // If the button state is different than what it was, then act
            if (xButtonCurrentlyPressed != xButtonPreviouslyPressed) {
                // If the button is (now) down, then toggle alliance color
                if (xButtonCurrentlyPressed) {
                    isBlueAlliance = !isBlueAlliance;
                }
            }
            xButtonPreviouslyPressed = xButtonCurrentlyPressed;
            telemetry.update();

        }
        waitForStart();
        runtime.reset();

        StateMachine machine = stateMachine.CreateStateDefinitions(gamepad1, gamepad2, armMotor, slideMotor, leftIntakeServo, rightIntakeServo, wristAngleServo, intakeAngleServo, colorSensorFunctions, intakeColorSensor, isBlueAlliance, slideSafety, telemetry);

        slideMotor.setTargetPosition(slidePositions.travelPosition);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(slidePositions.motorSpeed);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armMotor.setTargetPosition(slidePositions.travelPosition);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(armPositions.motorSpeed);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        machine.start();


        if (opModeIsActive()) {
            while(opModeIsActive()){
                // Functions - Comments can be found in individual files
                //driveTrain.fullDriveTrainControl(gamepad1, gamepad2, leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive, runtime, telemetry);
                driveTrain.updateTeleOpMovement(gamepad1);
                // intakeControl.intakeSpin(gamepad1, gamepad2, intakeServo, telemetry);

                intakeSampleColor = colorSensorFunctions.colorSensorGetColor(intakeColorSensor, isBlueAlliance, telemetry);

                if (abs(slideMotor.getCurrentPosition() - slideMotor.getTargetPosition()) < 2){
                    slideMotor.setPower(0);
                }
                else {
                    slideMotor.setPower(slidePositions.motorSpeed);
                }
                if (abs(armMotor.getCurrentPosition() - armMotor.getTargetPosition()) < 2){
                    armMotor.setPower(0);
                }
                else {
                    armMotor.setPower(armPositions.motorSpeed);
                }

                if (intakeSampleColor.equals("EJECT_SAMPLE")) {
                    telemetry.addData("Sample should be ejected", intakeSampleColor);
                }
                else if (!intakeSampleColor.equals("Null")) {
                    telemetry.addData("Should return to travel", intakeSampleColor);
                }
                else {
                    // intakeSampleState = 0;
                }

                machine.update();

                // Telemetry data
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.addData("intake color sample reading main", intakeSampleColor);
                telemetry.addData("Current State", machine.getState());
                telemetry.addData("Max Speed", driveTrainVariables.driveTrainMaxPower);
                telemetry.addData("Current Speed", driveTrainVariables.driveTrainMotorPower[0].getPower());
                telemetry.update();
            }
        }
    }

    public void mapVariables(
            Map<java.lang.String, DcMotor> motors,
            Map<java.lang.String, Servo> servos,
            Map<java.lang.String, String> misc
    ) {
        // Mapping Motors
        leftFrontDrive = motors.get("leftFrontDrive");
        leftBackDrive = motors.get("leftBackDrive");
        rightFrontDrive = motors.get("rightFrontDrive");
        rightBackDrive = motors.get("rightBackDrive");
        slideMotor = motors.get("slideMotor");
        armMotor = motors.get("armMotor");

        // Mapping Servos
        wristAngleServo = servos.get("wristAngleServo");
        leftIntakeServo = servos.get("leftIntakeServo");
        rightIntakeServo = servos.get("rightIntakeServo");
        intakeAngleServo = servos.get("intakeAngleServo");

        /*
        Mapping Miscellaneous
        (since miscellaneous maps have multiple types of objects, we need to send them as strings and map them here
        */
        slideSafety = hardwareMap.get(TouchSensor.class, misc.get("slideSafety"));
        intakeColorSensor = hardwareMap.get(NormalizedColorSensor.class, misc.get("intakeColorSensor"));
    }
}





