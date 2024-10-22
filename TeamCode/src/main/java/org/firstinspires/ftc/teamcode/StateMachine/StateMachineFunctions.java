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

    public void CreateStateDefinitions(Gamepad gamepad1, Gamepad gamepad2, DcMotor armMotor, DcMotor slideMotor, TouchSensor slideSafety, Telemetry telemetry) {
        StateMachine machine = new StateMachineBuilder()

                .state(StateFactoryExample.States.DEFAULT)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.defaultPosition);
                    armMotor.setTargetPosition(armPositions.defaultPosition);
                })
                .transition( () -> gamepad2.y, StateFactoryExample.States.SAMPLE_BASKET)
                .transition( () -> gamepad2.x, StateFactoryExample.States.HANG_SPECIMEN)
                .transition( () -> gamepad2.left_bumper, StateFactoryExample.States.COLLECT_SAMPLE)
                .transition( () -> gamepad2.right_bumper, StateFactoryExample.States.OBSERVATION_DECK)
                .transition( () -> gamepad2.b, StateFactoryExample.States.COLLECT_SPECIMEN)
                .transition( () -> gamepad2.dpad_left, StateFactoryExample.States.CLIMB_STAGE_ONE)

                .state(StateFactoryExample.States.SAMPLE_BASKET)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.sampleBasket);
                    armMotor.setTargetPosition(armPositions.sampleBasket);
                })
                .transition( () ->  gamepad2.a, StateFactoryExample.States.DEFAULT)


                .state(StateFactoryExample.States.HANG_SPECIMEN)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.hangSpecimen);
                    armMotor.setTargetPosition(armPositions.hangSpecimen);
                })
                .transition( () ->  gamepad2.a, StateFactoryExample.States.DEFAULT)

                .state(StateFactoryExample.States.COLLECT_SAMPLE)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.collectSample);
                    armMotor.setTargetPosition(armPositions.collectSample);
                })
                .transition( () ->  gamepad2.a, StateFactoryExample.States.DEFAULT)

                .state(StateFactoryExample.States.OBSERVATION_DECK)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.observationDeck);
                    armMotor.setTargetPosition(armPositions.observationDeck);
                })
                .transition( () ->  gamepad2.a, StateFactoryExample.States.DEFAULT)

                .state(StateFactoryExample.States.COLLECT_SPECIMEN)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.collectSpecimen);
                    armMotor.setTargetPosition(armPositions.collectSpecimen);
                })
                .transition( () ->  gamepad2.a, StateFactoryExample.States.DEFAULT)

                .state(StateFactoryExample.States.CLIMB_STAGE_ONE)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.climbStageOne);
                    armMotor.setTargetPosition(armPositions.climbStageOne);
                })
                .transition( () -> gamepad2.dpad_right, StateFactoryExample.States.STAGE_ONE_LIFT)
                .transition( () ->  gamepad2.a, StateFactoryExample.States.DEFAULT)

                .state(StateFactoryExample.States.STAGE_ONE_LIFT)
                .onEnter( () -> {
                    slideMotor.setTargetPosition(slidePositions.stageOneLift);
                    armMotor.setTargetPosition(armPositions.stageOneLift);
                })
                .transition( () ->  gamepad2.a, StateFactoryExample.States.DEFAULT)

                .build();
    }
}
