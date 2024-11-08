package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Math;


import org.firstinspires.ftc.teamcode.ObjectDeclarations.armPositions;

/*
This file runs arm motor backwards until it hits a touch sensor, then it zeroes out the encoder in
the motor. Then it runs to a predetermined starting position using the encoders, which will then be
zeroed out as encoder position 0.

This is crucial for the presets to work properly as it make the starting position of the arm an
exact position that can be reliably and easily reset to. This way the preset positions are exact and
accurately tuned based off consistent start position.
 */

@Autonomous(name="Reset Position", group="Robot Function")
public class ResetPosition extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor armMotor = null;
    public TouchSensor armSafety = null;
    public Servo wristServo = null;
    double motorSpeed = -0.8;
    int defaultPosition = 5225;


    @Override
    public void runOpMode() throws InterruptedException {


        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
        armMotor.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armSafety = hardwareMap.get(TouchSensor.class, "arm_safety");

        wristServo = hardwareMap.get(Servo.class, "wrist_servo");
        wristServo.setPosition(0.3);


        waitForStart();

        runtime.reset();




        while (!armSafety.isPressed()){
            armMotor.setPower(motorSpeed);
            telemetry.addData("Arm position", armMotor.getCurrentPosition());
            telemetry.update();
        }

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(-motorSpeed);

        armMotor.setTargetPosition(defaultPosition);

        while (Math.abs(armMotor.getCurrentPosition()-defaultPosition) > 10){

            telemetry.addData("Arm position", armMotor.getCurrentPosition());
            telemetry.update();
        }

    }


}