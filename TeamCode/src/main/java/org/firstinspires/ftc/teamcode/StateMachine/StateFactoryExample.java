package org.firstinspires.ftc.teamcode.StateMachine;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.*;
import org.firstinspires.ftc.teamcode.RobotFunctions.IntakeFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.MecanumFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.Robot;

import com.sfdev.assembly.state.*;

import java.util.Map;
import java.lang.Math;


@TeleOp(name="State Machine test", group="Code Structure")
public class StateFactoryExample extends LinearOpMode {

    // Name each of the preset states
    enum States {
        STARTING_POSITION,
        TRAVEL,
        HANG_SPECIMEN,
        TRANSITION_TO_BASKET,
        SAMPLE_BASKET,
        TRANSITION_FROM_BASKET,
        HIGH_SAMPLE,
        RELEASE_SAMPLE,
        COLLECT_SAMPLE,
        OBSERVATION_DECK,
        COLLECT_SPECIMEN,
        CLIMB_STAGE_ONE,
        STAGE_ONE_LIFT
    }

    // Create objects
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor slideMotor = null;
    public DcMotor armMotor = null;
    public Servo wristServo = null;
    public Servo intakeServo = null;
    public double driveTrainSpeed = 1;

        @Override
        public void runOpMode() throws InterruptedException {

            // Call function to handle hardware map
            Robot robot = new Robot(hardwareMap);
            Map<String, DcMotor> motors = robot.getDriveDictionary();
            Map<String, Servo> servos = robot.getServoDictionary();
            Map<String, TouchSensor> sensors = robot.getSensorDictionary();

            mapVariables(motors, servos, sensors);

            // Hardware map override
            slideMotor = hardwareMap.get(DcMotor.class, "slide_motor");
            slideMotor.setDirection(DcMotor.Direction.REVERSE);
            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
            armMotor.setDirection(DcMotor.Direction.REVERSE);
            armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);




            // Define details of each state
            StateMachine machine = new StateMachineBuilder()

                    // Starting position, not used during tele-op but to fit in 18-inch cube
                    // Every position is relative to starting position, starting position is hard
                    // defined using ResetPosition.java
                    .state(States.STARTING_POSITION)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.startingPosition);
                        armMotor.setTargetPosition(armPositions.startingPosition);
                        wristServo.setPosition(wristPositions.sample);
                    })
                    .transition( () -> gamepad2.a, States.TRAVEL)

                    // Travel position that can easily access each other position
                    // Easy and stable for travel, each other position has to return here
                    .state(States.TRAVEL)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.travelPosition);
                        armMotor.setTargetPosition(armPositions.travelPosition);
                        wristServo.setPosition(wristPositions.sample);
                        intakeServo.setPosition(intakePositions.intakeOff);

                        driveTrainSpeed = 0.8;
                    })
                    .transition( () -> gamepad2.y, States.TRANSITION_TO_BASKET)
                    .transition( () -> gamepad2.x, States.HANG_SPECIMEN)
                    .transition( () -> gamepad2.left_bumper, States.HIGH_SAMPLE)
                    .transition( () -> gamepad2.right_bumper, States.OBSERVATION_DECK)
                    .transition( () -> gamepad2.b, States.COLLECT_SPECIMEN)
                    .transition( () -> gamepad2.dpad_up, States.CLIMB_STAGE_ONE)

                    // Intermediate stage, raises arm first then once arm is in position moves to
                    // next state where slide extends to necessary position.
                    // This is done to not have the bot tip over, keeping mass above bot instead of
                    // in front.
                    .state(States.TRANSITION_TO_BASKET)
                    .onEnter( () -> {
                        armMotor.setTargetPosition(armPositions.sampleBasket);
                    })
                    .transition( () -> armMotor.getCurrentPosition() < (armPositions.sampleBasket + 10), States.SAMPLE_BASKET)
                    // When encoder is within 10 ticks of target position it moves to next state
                    // Note: 10 is arbitrary but fairly close while not needing exact

                    // State to place samples in the high basket
                    // This state can transfer to low basket (NOT YET IMPLEMENTED) or back to travel
                    .state(States.SAMPLE_BASKET)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.sampleBasket);
                        armMotor.setTargetPosition(armPositions.sampleBasket);

                        driveTrainSpeed = 0.3;
                    })
                    .transition( () -> gamepad2.right_trigger > 0, States.RELEASE_SAMPLE)
                    .transition( () ->  gamepad2.a, States.TRANSITION_FROM_BASKET)

                    // Releases sample before transitioning back to travel
                    // Timed transition, ADJUST FOR ACTUAL TIME REQUIRED TO RELEASE SAMPLE
                    .state(States.RELEASE_SAMPLE)
                    .onEnter( () -> {
                        intakeServo.setPosition(intakePositions.intakeReverse);
                    })
                    .transitionTimed(.75, States.TRANSITION_FROM_BASKET)

                    // Transition state: Closes slide before lowering arm to prevent bot from
                    // tipping over
                    .state(States.TRANSITION_FROM_BASKET)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.travelPosition);
                    })
                    .transition( () -> slideMotor.getCurrentPosition() < (slidePositions.travelPosition + 10), States.TRAVEL)

                    // Angle wrist and go to preset position to place a specimen
                    .state(States.HANG_SPECIMEN)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.hangSpecimen);
                        armMotor.setTargetPosition(armPositions.hangSpecimen);
                        wristServo.setPosition(wristPositions.specimen);
                    })
                    .transition( () ->  gamepad2.a, States.TRAVEL)

                    // Pickup sample position, higher up to not get caught on samples
                    // Use dpad to lower arm when you're above sample you want to pick up
                    .state(States.HIGH_SAMPLE)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.highSample);
                        armMotor.setTargetPosition(armPositions.highSample);

                        driveTrainSpeed = 0.3;
                    })
                    .transition( () -> gamepad2.dpad_down, States.COLLECT_SAMPLE)
                    .transition( () -> gamepad2.a, States.TRAVEL)


                    // Lower arm and spin intake to pick up sample
                    .state(States.COLLECT_SAMPLE)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.collectSample);
                        armMotor.setTargetPosition(armPositions.collectSample);
                        intakeServo.setPosition(intakePositions.intakeOn);

                        driveTrainSpeed = 0.5;
                    })
                    .transition( () ->  gamepad2.dpad_up, States.HIGH_SAMPLE)

                    // Preset position to put samples in observation deck to give to human player
                    .state(States.OBSERVATION_DECK)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.observationDeck);
                        armMotor.setTargetPosition(armPositions.observationDeck);
                    })
                    .transition( () ->  gamepad2.a, States.TRAVEL)

                    // Preset position to pick up a specimen
                    .state(States.COLLECT_SPECIMEN)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.collectSpecimen);
                        armMotor.setTargetPosition(armPositions.collectSpecimen);
                    })
                    .transition( () ->  gamepad2.a, States.TRAVEL)

                    // Set arm and slide in position allowing driver to drive to submersible to set
                    // up stage one climb
                    .state(States.CLIMB_STAGE_ONE)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.climbStageOne);
                        armMotor.setTargetPosition(armPositions.climbStageOne);
                    })
                    .transition( () -> gamepad2.dpad_down, States.STAGE_ONE_LIFT)
                    .transition( () ->  gamepad2.a, States.TRAVEL)

                    // Lowers arm to life bot off ground for stage one climb
                    .state(States.STAGE_ONE_LIFT)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.stageOneLift);
                        armMotor.setTargetPosition(armPositions.stageOneLift);
                    })
                    .transition( () ->  gamepad2.dpad_up, States.CLIMB_STAGE_ONE)

                    .build();


            // Sets starting position and mode to run to position for arm and slide
            // Needed to make motors run to preset positions
            // Note: brake does not hard stop, only offers slight resistance

            slideMotor.setTargetPosition(slidePositions.startingPosition);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideMotor.setPower(slidePositions.motorSpeed);
            slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            armMotor.setTargetPosition(slidePositions.startingPosition);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(armPositions.motorSpeed);
            armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



            waitForStart();

            machine.start();
            runtime.reset();
            IntakeFunctions intakeControl = new IntakeFunctions();
            MecanumFunctions driveTrain = new MecanumFunctions();

            while(opModeIsActive()) { // autonomous loop
                machine.update(); // Checks for inputs and handles state machine presets

                // Call functions and pass inputs to handle intake and drivetrain
                // NOTE: intake should be part of state machine
                intakeControl.intakeSpin(gamepad1, gamepad2, intakeServo, telemetry);
                driveTrain.fullDriveTrainControl(gamepad1, gamepad2, leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive, driveTrainSpeed, telemetry);

                // Telemetry data is basically print functions, these give drivers feedback, mostly
                // useful for debugging and testing purposes, making sure everything works as intended
                telemetry.addData("Slide position", slideMotor.getCurrentPosition());
                telemetry.addData("Arm position", armMotor.getCurrentPosition());
                telemetry.addData("Slide target position", slideMotor.getTargetPosition());
                telemetry.addData("Arm target position", armMotor.getTargetPosition());
                telemetry.addData("Wrist Position", wristServo.getPosition());
                telemetry.addData("Intake Position", intakeServo.getPosition());
                telemetry.addData("Current State", machine.getState());
                telemetry.update();
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

    }
    }