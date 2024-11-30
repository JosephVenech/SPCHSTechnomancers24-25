package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.Map;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotFunctions.IntakeFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.Robot;
import org.firstinspires.ftc.teamcode.RobotFunctions.MecanumFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.ColorSensorFunctions;
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
    public NormalizedColorSensor intakeColorSensor = null;
    public String intakeSampleColor = "Null";
    public Boolean isBlueAlliance = false; // Default team is red alliance

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap);
        Map<String, DcMotor> motors = robot.getDriveDictionary();
        Map<String, Servo> servos = robot.getServoDictionary();
        Map<String, TouchSensor> sensors = robot.getSensorDictionary();

        mapVariables(motors, servos, sensors);

        // TEMPORARY TODO: set up in Robot hardware map
        intakeColorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");
        intakeColorSensor.setGain(colorSensorVariables.gain);

        MecanumFunctions driveTrain = new MecanumFunctions();
        IntakeFunctions intakeControl = new IntakeFunctions();
        StateMachineFunctions stateMachine = new StateMachineFunctions();
        ColorSensorFunctions colorSensorFunctions = new ColorSensorFunctions();

        StateMachine machine = stateMachine.CreateStateDefinitions(gamepad1, gamepad2, armMotor, slideMotor, intakeServo, slideSafety, intakeSampleColor, telemetry);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        while (!opModeIsActive()) {
            telemetry.addData("Press game pad 2 X to toggle team alliance", "Current: " + (isBlueAlliance ? "Red" : "Blue"));

            if (gamepad2.x) {
                isBlueAlliance = !isBlueAlliance;
            }
        }
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





