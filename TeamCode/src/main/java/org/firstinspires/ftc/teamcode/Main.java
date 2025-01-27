package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants.leftFrontMotorName;
import static org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants.leftRearMotorName;
import static org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants.rightFrontMotorName;
import static org.firstinspires.ftc.teamcode.pedroPathing.tuning.FollowerConstants.rightRearMotorName;
import static java.lang.Math.abs;

import android.text.method.Touch;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import java.util.Map;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotFunctions.IntakeFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.Robot;
import org.firstinspires.ftc.teamcode.RobotFunctions.MecanumFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.ColorSensorFunctions;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.OldMecanumFunctions;

import org.firstinspires.ftc.teamcode.ObjectDeclarations.*;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;

import com.sfdev.assembly.state.*;


@TeleOp(name="Main", group="Main")
public class Main extends LinearOpMode {
    public ElapsedTime runtime = new ElapsedTime();

    public DcMotor leftFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor slideMotor = null;
    public DcMotor armMotor = null;
    public DcMotor leftLiftSystem = null;
    public DcMotor rightLiftSystem = null;
    public Servo wristAngleServo = null;
    public Servo leftIntakeServo = null;
    public Servo rightIntakeServo = null;
    public Servo intakeAngleServo = null;
    public TouchSensor slideSafety = null;
    public TouchSensor leftSafety = null;
    public TouchSensor rightSafety = null;
    public NormalizedColorSensor intakeColorSensor = null;
    public String intakeSampleColor = "Null";
    public Boolean isBlueAlliance = false; // Default team is red alliance
    public Boolean xButtonCurrentlyPressed = false;
    public Boolean xButtonPreviouslyPressed = false;
    public double intakeSampleState = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap);
        Map<String, DcMotor> motors = robot.getDriveDictionary();
        Map<String, Servo> servos = robot.getServoDictionary();
        Map<String, String> misc = robot.getMiscDictionary();

        mapVariables(motors, servos, misc);

        leftLiftSystem = hardwareMap.get(DcMotor.class, "left_linear");
        rightLiftSystem = hardwareMap.get(DcMotor.class, "right_linear");

        leftSafety = hardwareMap.get(TouchSensor.class, "left_lift_reset");
        rightSafety = hardwareMap.get(TouchSensor.class, "right_lift_reset");

        leftLiftSystem.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightLiftSystem.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLiftSystem.setDirection(DcMotorSimple.Direction.FORWARD);
        rightLiftSystem.setDirection(DcMotorSimple.Direction.FORWARD);

        //leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        MecanumFunctions driveTrain = new MecanumFunctions();
        driveTrain.init(hardwareMap);
        IntakeFunctions intakeControl = new IntakeFunctions();
        StateMachineFunctions stateMachine = new StateMachineFunctions();
        ColorSensorFunctions colorSensorFunctions = new ColorSensorFunctions();
        OldMecanumFunctions oldMecanumFunctions = new OldMecanumFunctions();

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

        StateMachine machine = stateMachine.CreateStateDefinitions(gamepad1, gamepad2, armMotor, slideMotor, leftLiftSystem, rightLiftSystem, leftIntakeServo, rightIntakeServo, wristAngleServo, intakeAngleServo, colorSensorFunctions, intakeColorSensor, isBlueAlliance, slideSafety, telemetry);

        slideMotor.setTargetPosition(slidePositions.travelPosition);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setPower(slidePositions.motorSpeed);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armMotor.setTargetPosition(slidePositions.travelPosition);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(armPositions.motorSpeed);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftLiftSystem.setTargetPosition(liftSystemPositions.liftClosed);
        leftLiftSystem.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftLiftSystem.setPower(liftSystemPositions.liftMotorPower);
        leftLiftSystem.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightLiftSystem.setTargetPosition(liftSystemPositions.liftClosed);
        rightLiftSystem.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLiftSystem.setPower(liftSystemPositions.liftMotorPower);
        rightLiftSystem.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        machine.start();

        if (opModeIsActive()) {
            while(opModeIsActive()){
                // Functions - Comments can be found in individual files
                // oldMecanumFunctions.fullDriveTrainControl(gamepad1, gamepad2, leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive, telemetry);
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

                if ((leftLiftSystem.getTargetPosition() == 0) && leftSafety.isPressed()){
                    leftLiftSystem.setPower(0);
                }
                else {
                    leftLiftSystem.setPower(liftSystemPositions.liftMotorPower);
                }

                if ((rightLiftSystem.getTargetPosition() == 0) && rightSafety.isPressed()){
                    rightLiftSystem.setPower(0);
                }
                else {
                    rightLiftSystem.setPower(liftSystemPositions.liftMotorPower);
                }

                if (gamepad1.a) {
                    leftFrontDrive.setPower(1);
                }
                if (gamepad1.b) {
                    leftBackDrive.setPower(-1);
                }
                if (gamepad1.x) {
                    rightFrontDrive.setPower(1);
                }
                if (gamepad1.y) {
                    rightBackDrive.setPower(1);
                }

                machine.update();

                // Telemetry data
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.addData("intake color sample reading main", intakeSampleColor);
                telemetry.addData("Current State", machine.getState());
                telemetry.addData("Arm Current Position", armMotor.getCurrentPosition());
                telemetry.addData("Arm Target Position", armMotor.getTargetPosition());
                telemetry.addData("Slide Current Position", slideMotor.getCurrentPosition());
                telemetry.addData("Slide Target Position", slideMotor.getTargetPosition());
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





