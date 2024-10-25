package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Reset Position", group="Robot Function")
public class ResetPosition extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor armMotor = null;
    public TouchSensor armSafety = null;
    double motorSpeed = -0.8;
    int defaultPosition = 7000;


    @Override
    public void runOpMode() throws InterruptedException {


        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
        armMotor.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armSafety = hardwareMap.get(TouchSensor.class, "arm_safety");


        waitForStart();

        runtime.reset();




        while (!armSafety.isPressed()){
            armMotor.setPower(motorSpeed);
            telemetry.addData("Arm position", armMotor.getCurrentPosition());
            telemetry.update();
        }

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armMotor.setTargetPosition(defaultPosition);

        telemetry.addData("Arm position", armMotor.getCurrentPosition());
        telemetry.update();

    }


}