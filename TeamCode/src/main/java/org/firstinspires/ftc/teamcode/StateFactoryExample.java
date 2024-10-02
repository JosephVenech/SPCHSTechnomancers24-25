package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;

import com.sfdev.assembly.state.*;

@TeleOp(name="State Machine test", group="Code Structure")
public class StateFactoryExample extends LinearOpMode {
    enum States {
        FIRST,
        SECOND,
        THIRD
    }

    public DcMotor slideMotor = null;

        @Override
        public void runOpMode() throws InterruptedException {

            slideMotor = hardwareMap.get(DcMotor.class, "slide_motor");
            slideMotor.setDirection(DcMotor.Direction.REVERSE);
            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            int slidePosition1 = 100;
            int slidePosition2 = 400;
            int slidePosition3 = 1200;
            double slideSpeed = 0.95;

            slideMotor.setTargetPosition(slidePosition1);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            StateMachine machine = new StateMachineBuilder()
                    .state(States.FIRST)
                    .onEnter( () -> {
                        System.out.println( "Entering the first state" );
                        slideMotor.setTargetPosition(slidePosition2);
                    })
                    .transition( () ->  gamepad1.x ) // transition when gamepad1.x is clicked
                    .onExit( () -> System.out.println("Exiting!") ) // setting check2 to false

                    .state(States.SECOND)
                    .onEnter( () -> {
                            System.out.println("Entering the second state");
                            slideMotor.setTargetPosition(slidePosition3);
                    }
                    )
                    .transition( () -> gamepad1.b) // if check2 is false transition

                    .state(States.THIRD)
                    .onEnter( () -> {
                        System.out.println("In the third state ");
                        slideMotor.setTargetPosition(slidePosition1);
                    })
                    .build();

            waitForStart();

            machine.start();

            while(opModeIsActive()) { // autonomous loop
                machine.update();
                slideMotor.setPower(slideSpeed);

                telemetry.addData("Slide position", slideMotor.getTargetPosition());
                telemetry.addData("Slide position 3", slidePosition3);
                telemetry.update();
            }
        }
    }