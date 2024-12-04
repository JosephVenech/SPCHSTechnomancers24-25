package org.firstinspires.ftc.teamcode.StateMachine;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.sfdev.assembly.state.StateMachine;
import com.sfdev.assembly.state.StateMachineBuilder;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.armPositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.intakePositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.slidePositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;

import org.firstinspires.ftc.teamcode.RobotFunctions.ColorSensorFunctions;

public class StateMachineFunctions {
    ColorSensorFunctions colorSensorFunctions = new ColorSensorFunctions();

    public enum States {
        TRAVEL,
        TRANSITION_TO_BASKET,
        SAMPLE_BASKET,
        TRANSITION_FROM_BASKET,
        HIGH_SAMPLE,
        RELEASE_SAMPLE,
        COLLECT_SAMPLE,
        EJECT_SAMPLE_PHASE_ONE,
        EJECT_SAMPLE_PHASE_TWO,
        CLIMB_STAGE_ONE,
        STAGE_ONE_LIFT
    }

    public StateMachine CreateStateDefinitions(Gamepad gamepad1, Gamepad gamepad2, DcMotor armMotor, DcMotor slideMotor, Servo intakeServo, NormalizedColorSensor intakeColorSensor, Boolean isBlueAlliance, TouchSensor slideSafety, Telemetry telemetry) {



        return new StateMachineBuilder() // returns the state machine states

                // Every position is relative to starting position, starting position is hard
                // defined using ResetPosition.java

                // Travel position that can easily access each other position
                // Easy and stable for travel, each other position has to return here
                .state(States.TRAVEL)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.travelPosition);
                    armMotor.setTargetPosition(armPositions.travelPosition);
                    intakeServo.setPosition(intakePositions.intakeOff);

                    driveTrainVariables.driveTrainSpeed = 0.8;
                })
                .transition( () -> gamepad2.y, States.TRANSITION_TO_BASKET)
                .transition( () -> gamepad2.left_bumper, States.HIGH_SAMPLE)
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

                    driveTrainVariables.driveTrainSpeed = 0.3;
                })
                .transition( () -> gamepad2.right_trigger > 0, States.RELEASE_SAMPLE)
                .transition( () ->  gamepad2.a, States.TRANSITION_FROM_BASKET)

                // Releases sample before transitioning back to travel
                // Timed transition, ADJUST FOR ACTUAL TIME REQUIRED TO RELEASE SAMPLE
                .state(States.RELEASE_SAMPLE)
                .onEnter( () -> {
                    intakeServo.setPosition(intakePositions.intakeReverse);
                })
                .transitionTimed(.75, States.TRANSITION_FROM_BASKET,
                        () -> armMotor.setTargetPosition(armPositions.safeReturnTransition))

                // Transition state: Closes slide before lowering arm to prevent bot from
                // tipping over
                .state(States.TRANSITION_FROM_BASKET)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.travelPosition);
                })
                .transition( () -> slideMotor.getCurrentPosition() < (slidePositions.travelPosition + 10), States.TRAVEL)

                // Pickup sample position, higher up to not get caught on samples
                // Use dpad to lower arm when you're above sample you want to pick up
                .state(States.HIGH_SAMPLE)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.highSample);
                    armMotor.setTargetPosition(armPositions.highSample);
                    intakeServo.setPosition(intakePositions.intakeOff);

                    driveTrainVariables.driveTrainSpeed = 0.3;
                })
                //.transition( () -> (armMotor.getCurrentPosition() >= armPositions.highSample - 10)  && intakeSampleColor.equals("EJECT_SAMPLE"),States.EJECT_SAMPLE)

                .transition( () -> gamepad2.dpad_down, States.COLLECT_SAMPLE)
                .transition( () -> gamepad2.a, States.TRAVEL)


                // Lower arm and spin intake to pick up sample
                .state(States.COLLECT_SAMPLE)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.collectSample);
                    armMotor.setTargetPosition(armPositions.collectSample);
                    intakeServo.setPosition(intakePositions.intakeOn);

                })
                // .loop( () -> telemetry.addData("intake color sample reading state machine", intakeSampleState))

                // Automatically transition to travel when intake has sample in it
                .transition( () -> (colorSensorFunctions.colorSensorGetColor(intakeColorSensor, isBlueAlliance, telemetry) == "EJECT_SAMPLE"), States.EJECT_SAMPLE_PHASE_ONE)
                .transition( () -> (colorSensorFunctions.colorSensorGetColor(intakeColorSensor, isBlueAlliance, telemetry) != "Null" && colorSensorFunctions.colorSensorGetColor(intakeColorSensor, isBlueAlliance, telemetry) != "EJECT_SAMPLE"), States.TRAVEL)
                .transition( () ->  gamepad2.dpad_up, States.HIGH_SAMPLE)

                .state(States.EJECT_SAMPLE_PHASE_ONE)
                .onEnter( () -> {
                    armMotor.setTargetPosition(armPositions.highSample);
                    intakeServo.setPosition(intakePositions.intakeOff);
                })
                .transition( () -> armMotor.getCurrentPosition() >= (armPositions.highSample - 3) , States.EJECT_SAMPLE_PHASE_TWO)

                // Eject Sample if it is the wrong color
                .state(States.EJECT_SAMPLE_PHASE_TWO)
                .onEnter( () -> {
                    intakeServo.setPosition(intakePositions.intakeReverse);
                })
                .transitionTimed(0.7, States.HIGH_SAMPLE)

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

    }
}
