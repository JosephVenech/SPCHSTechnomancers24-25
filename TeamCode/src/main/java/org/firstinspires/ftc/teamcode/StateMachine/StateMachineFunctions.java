package org.firstinspires.ftc.teamcode.StateMachine;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.sfdev.assembly.state.StateMachine;
import com.sfdev.assembly.state.StateMachineBuilder;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.armPositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.slidePositions;


public class StateMachineFunctions {

    public StateMachine CreateStateDefinitions(Gamepad gamepad1, Gamepad gamepad2, DcMotor armMotor, DcMotor slideMotor, TouchSensor slideSafety, Telemetry telemetry) {

        return new StateMachineBuilder() // returns the statemachine

                .state(StateFactoryExample.States.TRAVEL)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.travelPosition);
                    armMotor.setTargetPosition(armPositions.travelPosition);
                })
                .transition( () -> gamepad2.y, StateFactoryExample.States.SAMPLE_BASKET)
                .transition( () -> gamepad2.left_bumper, StateFactoryExample.States.COLLECT_SAMPLE)
                .transition( () -> gamepad2.dpad_left, StateFactoryExample.States.CLIMB_STAGE_ONE)

                .state(StateFactoryExample.States.SAMPLE_BASKET)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.sampleBasket);
                    armMotor.setTargetPosition(armPositions.sampleBasket);
                })
                .transition( () ->  gamepad2.a, StateFactoryExample.States.TRAVEL)

                .state(StateFactoryExample.States.COLLECT_SAMPLE)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.collectSample);
                    armMotor.setTargetPosition(armPositions.collectSample);
                })
                .transition( () ->  gamepad2.a, StateFactoryExample.States.TRAVEL)

                .state(StateFactoryExample.States.CLIMB_STAGE_ONE)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.climbStageOne);
                    armMotor.setTargetPosition(armPositions.climbStageOne);
                })
                .transition( () -> gamepad2.dpad_down, StateFactoryExample.States.STAGE_ONE_LIFT)
                .transition( () ->  gamepad2.a, StateFactoryExample.States.TRAVEL)

                .state(StateFactoryExample.States.STAGE_ONE_LIFT)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.stageOneLift);
                    armMotor.setTargetPosition(armPositions.stageOneLift);
                })
                .transition( () ->  gamepad2.dpad_up, StateFactoryExample.States.CLIMB_STAGE_ONE)

                .build();
    }
}
