package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.*;

import com.sfdev.assembly.state.*;


@TeleOp(name="State Machine test", group="Code Structure")
public class StateFactoryExample extends LinearOpMode {
    enum States {
        DEFAULT,
        HANG_SPECIMEN,
        SAMPLE_BASKET,
        COLLECT_SAMPLE,
        OBSERVATION_DECK,
        COLLECT_SPECIMEN,
        CLIMB_STAGE_ONE,
        STAGE_ONE_LIFT
    }


    public DcMotor slideMotor = null;
    public DcMotor armMotor = null;

        @Override
        public void runOpMode() throws InterruptedException {

            slideMotor = hardwareMap.get(DcMotor.class, "slide_motor");
            slideMotor.setDirection(DcMotor.Direction.REVERSE);
            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
            armMotor.setDirection(DcMotor.Direction.FORWARD);
            armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);





            StateMachine machine = new StateMachineBuilder()

                    .state(States.DEFAULT)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.defaultPosition);
                        armMotor.setTargetPosition(armPositions.defaultPosition);
                    })
                    .transition( () -> gamepad2.y, States.SAMPLE_BASKET)
                    .transition( () -> gamepad2.x, States.HANG_SPECIMEN)
                    .transition( () -> gamepad2.left_bumper, States.COLLECT_SAMPLE)
                    .transition( () -> gamepad2.right_bumper, States.OBSERVATION_DECK)
                    .transition( () -> gamepad2.b, States.COLLECT_SPECIMEN)
                    .transition( () -> gamepad2.dpad_left, States.CLIMB_STAGE_ONE)

                    .state(States.SAMPLE_BASKET)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.sampleBasket);
                        armMotor.setTargetPosition(armPositions.sampleBasket);
                    })
                    .transition( () ->  gamepad2.a, States.DEFAULT)


                    .state(States.HANG_SPECIMEN)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.hangSpecimen);
                        armMotor.setTargetPosition(armPositions.hangSpecimen);
                    })
                    .transition( () ->  gamepad2.a, States.DEFAULT)

                    .state(States.COLLECT_SAMPLE)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.collectSample);
                        armMotor.setTargetPosition(armPositions.collectSample);
                    })
                    .transition( () ->  gamepad2.a, States.DEFAULT)

                    .state(States.OBSERVATION_DECK)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.observationDeck);
                        armMotor.setTargetPosition(armPositions.observationDeck);
                    })
                    .transition( () ->  gamepad2.a, States.DEFAULT)

                    .state(States.COLLECT_SPECIMEN)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.collectSpecimen);
                        armMotor.setTargetPosition(armPositions.collectSpecimen);
                    })
                    .transition( () ->  gamepad2.a, States.DEFAULT)

                    .state(States.CLIMB_STAGE_ONE)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.climbStageOne);
                        armMotor.setTargetPosition(armPositions.climbStageOne);
                    })
                    .transition( () -> gamepad2.dpad_right, States.STAGE_ONE_LIFT)
                    .transition( () ->  gamepad2.a, States.DEFAULT)

                    .state(States.STAGE_ONE_LIFT)
                    .onEnter( () -> {
                        slideMotor.setTargetPosition(slidePositions.stageOneLift);
                        armMotor.setTargetPosition(armPositions.stageOneLift);
                    })
                    .transition( () ->  gamepad2.a, States.DEFAULT)

                    .build();

            slideMotor.setTargetPosition(slidePositions.defaultPosition);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slideMotor.setPower(slidePositions.motorSpeed);

            armMotor.setTargetPosition(slidePositions.defaultPosition);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(armPositions.motorSpeed);

            waitForStart();

            machine.start();

            while(opModeIsActive()) { // autonomous loop
                machine.update();

                telemetry.addData("Slide position", slideMotor.getCurrentPosition());
                telemetry.addData("Arm position", armMotor.getCurrentPosition());
                telemetry.addData("Slide target position", slideMotor.getTargetPosition());
                telemetry.addData("Arm target position", armMotor.getTargetPosition());
                telemetry.addData("Current State", machine.getState());
                telemetry.update();
            }
        }
    }