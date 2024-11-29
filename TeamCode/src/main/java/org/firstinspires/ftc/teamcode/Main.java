package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.Map;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotFunctions.IntakeFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.Robot;
import org.firstinspires.ftc.teamcode.RobotFunctions.MecanumFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.SlideFunctions;
import org.firstinspires.ftc.teamcode.StateMachine.StateMachineFunctions;

import org.firstinspires.ftc.teamcode.ObjectDeclarations.*;

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
    public Servo wristServo = null;
    public Servo intakeServo = null;
    public TouchSensor slideSafety = null;
    public double driveTrainSpeed = 1;

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap);
        Map<String, DcMotor> motors = robot.getDriveDictionary();
        Map<String, Servo> servos = robot.getServoDictionary();
        Map<String, TouchSensor> sensors = robot.getSensorDictionary();

        mapVariables(motors, servos, sensors);

        MecanumFunctions driveTrain = new MecanumFunctions();
        IntakeFunctions intakeControl = new IntakeFunctions();
        StateMachineFunctions stateMachine = new StateMachineFunctions();

        StateMachine machine = stateMachine.CreateStateDefinitions(gamepad1, gamepad2, armMotor, slideMotor, intakeServo, slideSafety, telemetry);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

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
                driveTrain.fullDriveTrainControl(gamepad1, gamepad2, leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive, telemetry);
                intakeControl.intakeAngle(gamepad1, gamepad2, wristServo, telemetry);
                intakeControl.intakeSpin(gamepad1, gamepad2, intakeServo, telemetry);


                machine.update();


                // Telemetry data
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.update();
            }
        }
    }

    public void mapVariables(
            Map<java.lang.String, DcMotor> motors,
            Map<java.lang.String, Servo> servos,
            Map<java.lang.String, TouchSensor> sensors
    ) {
        // Mapping Motors
        leftFrontDrive = motors.get("leftFrontDrive");
        leftBackDrive = motors.get("leftBackDrive");
        rightFrontDrive = motors.get("rightFrontDrive");
        rightBackDrive = motors.get("rightBackDrive");
        slideMotor = motors.get("slideMotor");
        armMotor = motors.get("armMotor");

        // Mapping Servos
        wristServo = servos.get("wristServo");
        intakeServo = servos.get("intakeServo");

        // Mapping TouchSensors
        slideSafety = sensors.get("slideSafety");
    }
}





