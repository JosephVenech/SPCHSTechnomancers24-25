package org.firstinspires.ftc.teamcode.UserSandboxes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotFunctions.Robot;

import java.util.Map;


@Autonomous(name="River Auto", group="User Sandboxes")
public class RiverMain extends LinearOpMode {
    enum States {
        STARTING_POSITION,
        TRAVEL,
        LIFT_ARM,
        EXTEND_SLIDE,
        RELEASE_SAMPLE,
        CLOSE_SLIDE,
        LOWER_ARM
    }

    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor slideMotor = null;
    public DcMotor armMotor = null;
    public Servo wristServo = null;
    public Servo intakeServo = null;
    public double driveTrainSpeed = 0.4;
    int travelDistance = 2000;


    @Override
    public void runOpMode() throws InterruptedException {

        // Call function to handle hardware map
        Robot robot = new Robot(hardwareMap);
        Map<String, DcMotor> motors = robot.getDriveDictionary();
        Map<String, Servo> servos = robot.getServoDictionary();
        Map<String, String> misc = robot.getMiscDictionary();

        mapVariables(motors, servos, misc);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);




        waitForStart();

        runtime.reset();





        leftFrontDrive.setPower(driveTrainSpeed);
        rightFrontDrive.setPower(driveTrainSpeed);
        leftBackDrive.setPower(driveTrainSpeed);
        rightBackDrive.setPower(driveTrainSpeed);

        leftFrontDrive.setTargetPosition(travelDistance);
        rightFrontDrive.setTargetPosition(travelDistance);
        leftBackDrive.setTargetPosition(travelDistance);
        rightBackDrive.setTargetPosition(travelDistance);


        while (Math.abs(leftFrontDrive.getCurrentPosition()-travelDistance) > 10){

                telemetry.addData("drivetrain position", leftFrontDrive.getCurrentPosition());
                telemetry.update();
            }
        /*
        armMotor.setPower(armPositions.motorSpeed);
        armMotor.setTargetPosition(armPositions.sampleBasket);

        while (Math.abs(armMotor.getCurrentPosition()-armPositions.sampleBasket) > 10){

            telemetry.addData("arm position", armMotor.getCurrentPosition());
            telemetry.update();
        }

        slideMotor.setPower(slidePositions.motorSpeed);
        slideMotor.setTargetPosition(slidePositions.sampleBasket);

        while (Math.abs(slideMotor.getCurrentPosition()-slidePositions.sampleBasket) > 10){

            telemetry.addData("slide position", slideMotor.getCurrentPosition());
            telemetry.update();
        }

        intakeServo.setPosition(intakePositions.intakeReverse);
        sleep(500);

        slideMotor.setTargetPosition(slidePositions.startingPosition);
        while (Math.abs(slideMotor.getCurrentPosition()-slidePositions.startingPosition) > 10){

            telemetry.addData("slide position", slideMotor.getCurrentPosition());
            telemetry.update();
        }
        armMotor.setTargetPosition(armPositions.startingPosition);
        */
    }
    public void mapVariables(
                Map<java.lang.String, DcMotor> motors,
                Map<java.lang.String, Servo> servos,
                Map<java.lang.String, String> sensors
    ) {
            // Mapping Motors
            leftFrontDrive = motors.get("leftFrontDrive");
            leftBackDrive = motors.get("leftBackDrive");
            rightFrontDrive = motors.get("rightFrontDrive");
            rightBackDrive = motors.get("rightBackDrive");
            slideMotor = motors.get("slideMotor");
            armMotor = motors.get("armMotor");

            // Mapping Servos
            wristServo = servos.get("wristServo");
            intakeServo = servos.get("intakeServo");

        }
}
