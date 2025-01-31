package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
    public DcMotor slideMotor = null;
    public DcMotor leftLiftMotor = null;
    public DcMotor rightLiftMotor = null;
    public TouchSensor armSafety = null;
    public TouchSensor slideSafety = null;
    public TouchSensor leftLiftReset = null;
    public TouchSensor rightLiftReset = null;
    public Servo wristAngleServo = null;
    double motorSpeed = 0.8;
    double slideSpeed = 0.5;
    int defaultPosition = -2700;


    @Override
    public void runOpMode() throws InterruptedException {
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
        armMotor.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        slideMotor = hardwareMap.get(DcMotor.class, "slide_motor");
        slideMotor.setDirection(DcMotor.Direction.REVERSE);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftLiftMotor = hardwareMap.get(DcMotor.class, "left_linear");
        rightLiftMotor = hardwareMap.get(DcMotor.class, "right_linear");
        leftLiftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightLiftMotor.setDirection(DcMotor.Direction.FORWARD);

        armSafety = hardwareMap.get(TouchSensor.class, "arm_safety");
        slideSafety = hardwareMap.get(TouchSensor.class, "slide_safety");
        leftLiftReset = hardwareMap.get(TouchSensor.class, "left_lift_reset");
        rightLiftReset = hardwareMap.get(TouchSensor.class, "right_lift_reset");

        wristAngleServo = hardwareMap.get(Servo.class, "wrist_angle_servo");
        // wristAngleServo.setPosition(0.3);

        waitForStart();
        runtime.reset();

        while (!slideSafety.isPressed()){
            slideMotor.setPower(slideSpeed);
            telemetry.addData("Slide position", slideMotor.getCurrentPosition());
            telemetry.update();
        }

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        while (!armSafety.isPressed()){
            armMotor.setPower(motorSpeed);
            telemetry.addData("Arm position", armMotor.getCurrentPosition());
            telemetry.update();
        }

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        while (!leftLiftReset.isPressed()){
            leftLiftMotor.setPower(-motorSpeed);
        }
        leftLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        while (!rightLiftReset.isPressed()){
            rightLiftMotor.setPower(-motorSpeed);
        }
        rightLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        armMotor.setTargetPosition(defaultPosition);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(motorSpeed);

        while (Math.abs(armMotor.getCurrentPosition()-defaultPosition) > 10) {
            telemetry.addData("Arm position", armMotor.getCurrentPosition());
            telemetry.update();
        }

    }

}