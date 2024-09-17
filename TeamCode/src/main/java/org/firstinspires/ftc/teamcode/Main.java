package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class Main extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {
        //DriveTrain Motors
      Robot robot = new Robot(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        if (opModeIsActive()) {
            while(opModeIsActive()){

                //Drivetrain - Comments can be found in DriveTrainFunctions.java

                double max;

                double axial = -gamepad1.left_stick_y;
                double lateral = gamepad1.left_stick_x;
                double yaw = gamepad1.right_stick_x;

                double leftFrontPower = axial + lateral + yaw;
                double rightFrontPower = axial - lateral - yaw;
                double leftBackPower = axial - lateral + yaw;
                double rightBackPower = axial + lateral - yaw;

                max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
                max = Math.max(max, Math.abs(leftBackPower));
                max = Math.max(max, Math.abs(rightBackPower));

                if (max > 1.0) {
                    leftFrontPower /= max;
                    rightFrontPower /= max;
                    leftBackPower /= max;
                    rightBackPower /= max;
                }

                robot.leftFrontDrive.setPower(leftFrontPower);
                robot.rightFrontDrive.setPower(rightFrontPower);
                robot.leftBackDrive.setPower(leftBackPower);
                robot.rightBackDrive.setPower(rightBackPower);

                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
                telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
                telemetry.update();

                //End of Drivetrain

            }
        }
    }
}






/*
    Robot robot = new Robot(hardwareMap);

    //DriveTrainFunctions driveTrainFunctions = new DriveTrainFunctions() {


        @Override
        public void runOpMode() throws InterruptedException {
            Robot robot = new Robot(hardwareMap);


            waitForStart();

            if (opModeIsActive()) {
                //driveTrain.initDriveTrain();
                while (opModeIsActive()) {
                    //robot.leftFrontDrive.setPower(.5);



                }

            /*
            public void runOpMode() throws InterruptedException {
            updateDriveTrain();
            */
