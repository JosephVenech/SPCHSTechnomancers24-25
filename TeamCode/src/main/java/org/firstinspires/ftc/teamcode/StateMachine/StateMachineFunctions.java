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
import org.firstinspires.ftc.teamcode.ObjectDeclarations.wristAnglePositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.intakeAnglePositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.slidePositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.liftSystemPositions;

import org.firstinspires.ftc.teamcode.RobotFunctions.ColorSensorFunctions;

import java.util.Map;

public class StateMachineFunctions {
    public enum States {
        TRAVEL,
        TRANSITION_TO_BASKET,
        SAMPLE_BASKET,
        TRANSITION_FROM_BASKET_PHASE_ONE,
        TRANSITION_FROM_BASKET_PHASE_TWO,
        HIGH_SAMPLE,
        RELEASE_SAMPLE,
        COLLECT_SAMPLE,
        EJECT_SAMPLE_PHASE_ONE,
        EJECT_SAMPLE_PHASE_TWO,
        CLIMB_STAGE_ONE,
        CLIMB_STAGE_TWO,
        CLIMB_STAGE_THREE,
        CLIMB_STAGE_FOUR,
        CLIMB_STAGE_FIVE,
        CLIMB_STAGE_SIX,
        CLIMB_STAGE_SEVEN
    }

    public ColorSensorFunctions colorSensorFunctions = null;

    public StateMachine CreateStateDefinitions(Gamepad gamepad1, Gamepad gamepad2, DcMotor armMotor, DcMotor slideMotor, DcMotor leftLiftSystem, DcMotor rightLiftSystem, Servo leftIntakeServo, Servo rightIntakeServo, Servo wristAngleServo, Servo intakeAngleServo, ColorSensorFunctions cF, NormalizedColorSensor intakeColorSensor, Boolean isBlueAlliance, TouchSensor slideSafety, Telemetry telemetry) {
        colorSensorFunctions = cF;

        return new StateMachineBuilder() // returns the state machine states

                // Every position is relative to starting position, starting position is hard
                // defined using ResetPosition.java

                // Travel position that can easily access each other position
                // Easy and stable for travel, each other position has to return here
                .state(States.TRAVEL)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.travelPosition);
                    armMotor.setTargetPosition(armPositions.travelPosition);
                    leftIntakeServo.setPosition(intakePositions.leftIntakeOff);
                    rightIntakeServo.setPosition(intakePositions.rightIntakeOff);
                    wristAngleServo.setPosition(wristAnglePositions.travel);
                    intakeAngleServo.setPosition(intakeAnglePositions.flat);
                    leftLiftSystem.setTargetPosition(liftSystemPositions.liftClosed);
                    rightLiftSystem.setTargetPosition(liftSystemPositions.liftClosed);

                    driveTrainVariables.driveTrainMaxPower = driveTrainVariables.driveTrainDefaultMaxPower;
                })
                .transition( () -> (GetSampleColor(intakeColorSensor, isBlueAlliance, telemetry).equals("EJECT_SAMPLE")), States.EJECT_SAMPLE_PHASE_ONE)
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
                    wristAngleServo.setPosition(wristAnglePositions.placeSample);
                    intakeAngleServo.setPosition(intakeAnglePositions.vertical);

                    driveTrainVariables.driveTrainMaxPower = 0.2;
                })
                .transition( () -> gamepad2.right_trigger > 0, States.RELEASE_SAMPLE)
                .transition( () ->  gamepad2.a, States.TRANSITION_FROM_BASKET_PHASE_TWO)

                // Releases sample before transitioning back to travel
                // Timed transition, ADJUST FOR ACTUAL TIME REQUIRED TO RELEASE SAMPLE
                .state(States.RELEASE_SAMPLE)
                .onEnter( () -> {
                    leftIntakeServo.setPosition(intakePositions.leftIntakeReverse);
                    rightIntakeServo.setPosition(intakePositions.rightIntakeReverse);
                })
                .transitionTimed(.75, States.TRANSITION_FROM_BASKET_PHASE_TWO)

                // TODO: Should be safe remove, make sure then remove it if so
                .state(States.TRANSITION_FROM_BASKET_PHASE_ONE)
                .onEnter( () -> {
                    armMotor.setTargetPosition(armPositions.safeReturnTransition);
                })
                .transition( () -> (armMotor.getCurrentPosition() >= armPositions.safeReturnTransition), States.TRANSITION_FROM_BASKET_PHASE_TWO)

                // Transition state: Closes slide before lowering arm to prevent bot from
                // tipping over
                .state(States.TRANSITION_FROM_BASKET_PHASE_TWO)
                .onEnter( () -> {
                    wristAngleServo.setPosition(wristAnglePositions.travel);
                    slideMotor.setTargetPosition(slidePositions.travelPosition);
                })
                .transition( () -> (Math.abs(slideMotor.getCurrentPosition() - slidePositions.travelPosition) <= 20), States.TRAVEL)
                // .transition( () ->  gamepad2.a, States.TRAVEL)


                // Pickup sample position, higher up to not get caught on samples
                // Use dpad to lower arm when you're above sample you want to pick up
                .state(States.HIGH_SAMPLE)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.highSample);
                    armMotor.setTargetPosition(armPositions.highSample);
                    wristAngleServo.setPosition(wristAnglePositions.collectSample);
                    intakeAngleServo.setPosition(intakeAnglePositions.flat);
                    leftIntakeServo.setPosition(intakePositions.leftIntakeOff);
                    rightIntakeServo.setPosition(intakePositions.rightIntakeOff);

                    driveTrainVariables.driveTrainMaxPower = 0.3;
                })
                .transition( () -> gamepad2.dpad_down, States.COLLECT_SAMPLE)
                .transition( () -> gamepad2.a, States.TRAVEL)

                // Lower arm and spin intake to pick up sample
                .state(States.COLLECT_SAMPLE)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.collectSample);
                    armMotor.setTargetPosition(armPositions.collectSample);
                    leftIntakeServo.setPosition(intakePositions.leftIntakeOn);
                    rightIntakeServo.setPosition(intakePositions.rightIntakeOn);

                })
                // .loop( () -> telemetry.addData("intake color sample reading state machine", intakeSampleState))

                // Automatically transition to travel when intake has sample in it
                .transition( () -> (GetSampleColor(intakeColorSensor, isBlueAlliance, telemetry).equals("EJECT_SAMPLE")), States.EJECT_SAMPLE_PHASE_ONE)
                .transition( () -> (!GetSampleColor(intakeColorSensor, isBlueAlliance, telemetry).equals("Null") && !GetSampleColor(intakeColorSensor, isBlueAlliance, telemetry).equals("EJECT_SAMPLE")), States.TRAVEL)
                .transition( () ->  gamepad2.dpad_up, States.HIGH_SAMPLE)

                .state(States.EJECT_SAMPLE_PHASE_ONE)
                .onEnter( () -> {
                    armMotor.setTargetPosition(armPositions.highSample);
                    leftIntakeServo.setPosition(intakePositions.leftIntakeOff);
                    rightIntakeServo.setPosition(intakePositions.rightIntakeOff);
                })
                // .transition( () -> armMotor.getCurrentPosition() >= (armPositions.highSample - 10), States.EJECT_SAMPLE_PHASE_TWO)
                .transitionTimed(0.3, States.EJECT_SAMPLE_PHASE_TWO)

                // Eject Sample if it is the wrong color
                .state(States.EJECT_SAMPLE_PHASE_TWO)
                .onEnter( () -> {
                    leftIntakeServo.setPosition(intakePositions.leftIntakeReverse);
                    rightIntakeServo.setPosition(intakePositions.rightIntakeReverse);
                })
                .transitionTimed(0.6, States.HIGH_SAMPLE)

                // TODO: Change to lift system instead of arms doing climb
                .state(States.CLIMB_STAGE_ONE)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.climbStageOne);
                    armMotor.setTargetPosition(armPositions.climbStageOne);
                    leftLiftSystem.setTargetPosition(liftSystemPositions.liftOpen);
                    rightLiftSystem.setTargetPosition(liftSystemPositions.liftOpen);
                })
                .transition( () -> gamepad2.dpad_down, States.CLIMB_STAGE_TWO)
                .transition( () ->  gamepad2.a, States.TRAVEL)

                // Lowers arm to life bot off ground for stage one climb
                .state(States.CLIMB_STAGE_TWO)
                .onEnter( () -> {
                    leftLiftSystem.setTargetPosition(liftSystemPositions.liftClosed);
                    rightLiftSystem.setTargetPosition(liftSystemPositions.liftClosed);

                    slideMotor.setTargetPosition(slidePositions.climbStageTwo);
                    armMotor.setTargetPosition(armPositions.climbStageTwo);
                })
                .transition( () ->  gamepad2.dpad_up, States.CLIMB_STAGE_ONE)
                .transition( () ->  (
                        (Math.abs(leftLiftSystem.getCurrentPosition() - leftLiftSystem.getTargetPosition()) <= liftSystemPositions.threshold) &&
                                (Math.abs(slideMotor.getCurrentPosition() - slideMotor.getTargetPosition()) <= liftSystemPositions.threshold) &&
                                (Math.abs(armMotor.getCurrentPosition() - armMotor.getTargetPosition()) <= liftSystemPositions.threshold)), States.CLIMB_STAGE_THREE)


                .state(States.CLIMB_STAGE_THREE)
                .onEnter( () -> {
                    armMotor.setTargetPosition(armPositions.climbStageThree);
                    slideMotor.setTargetPosition(slidePositions.climbStageThree);
                })
                .transition( () ->  (
                        (Math.abs(slideMotor.getCurrentPosition() - slideMotor.getTargetPosition()) <= liftSystemPositions.threshold) &&
                        (Math.abs(armMotor.getCurrentPosition() - armMotor.getTargetPosition()) <= liftSystemPositions.threshold)), States.CLIMB_STAGE_FOUR)

                .state(States.CLIMB_STAGE_FOUR)
                .onEnter( () -> {
                    armMotor.setTargetPosition(armPositions.climbStageFour);
                    slideMotor.setTargetPosition(slidePositions.climbStageFour);
                })
                .transition( () ->  (
                        (Math.abs(slideMotor.getCurrentPosition() - slideMotor.getTargetPosition()) <= liftSystemPositions.threshold) &&
                        (Math.abs(armMotor.getCurrentPosition() - armMotor.getTargetPosition()) <= liftSystemPositions.threshold)), States.CLIMB_STAGE_FIVE)

                .state(States.CLIMB_STAGE_FIVE)
                .onEnter( () -> {
                    armMotor.setTargetPosition(armPositions.climbStageFive);
                    slideMotor.setTargetPosition(slidePositions.climbStageFive);
                })
                .transition( () ->  (
                        (Math.abs(slideMotor.getCurrentPosition() - slideMotor.getTargetPosition()) <= liftSystemPositions.threshold) &&
                        (Math.abs(armMotor.getCurrentPosition() - armMotor.getTargetPosition()) <= liftSystemPositions.threshold)), States.CLIMB_STAGE_SIX)

                .state(States.CLIMB_STAGE_SIX)
                .onEnter( () -> {
                    armMotor.setTargetPosition(armPositions.climbStageSix);
                    slideMotor.setTargetPosition(slidePositions.climbStageSix);
                })
                .transition( () ->  (
                        (Math.abs(slideMotor.getCurrentPosition() - slideMotor.getTargetPosition()) <= liftSystemPositions.threshold) &&
                        (Math.abs(armMotor.getCurrentPosition() - armMotor.getTargetPosition()) <= liftSystemPositions.threshold)), States.CLIMB_STAGE_SEVEN)

                .state(States.CLIMB_STAGE_SEVEN)
                .onEnter( () -> {
                    armMotor.setTargetPosition(armPositions.climbStageSeven);
                    slideMotor.setTargetPosition(slidePositions.climbStageSeven);
                })


                .build();

    }
    public String GetSampleColor(NormalizedColorSensor intakeColorSensor, Boolean isBlueAlliance, Telemetry telemetry) { return colorSensorFunctions.colorSensorGetColor(intakeColorSensor, isBlueAlliance, telemetry); }
}
