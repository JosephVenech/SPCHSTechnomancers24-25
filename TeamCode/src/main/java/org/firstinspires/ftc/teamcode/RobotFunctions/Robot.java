package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.HashMap;
import java.util.Map;

public class Robot {
    public DcMotor leftFrontDrive;
    public DcMotor leftBackDrive;
    public DcMotor rightFrontDrive;
    public DcMotor rightBackDrive;
    public DcMotor slideMotor;
    public DcMotor armMotor;
    public Servo wristServo;
    public Servo intakeServo;
    public TouchSensor slideSafety;

    public Map<String, DcMotor> driveDictionary = new HashMap<>();
    public Map<String, Servo> servoDictionary = new HashMap<>();
    public Map<String, TouchSensor> sensorDictionary = new HashMap<>();

    public Robot(HardwareMap hardwareMap) {
        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        // Motors
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        slideMotor = hardwareMap.get(DcMotor.class, "slide_motor");
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");

        // Servos
        wristServo = hardwareMap.get(Servo.class, "wrist_servo");
        intakeServo = hardwareMap.get(Servo.class, "intake_servo");

        wristServo.setPosition(0.3);
        intakeServo.setPosition(0);

        // Sensors
        slideSafety = hardwareMap.get(TouchSensor.class, "slide_safety");

        // Set wheels to move forward
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        slideMotor.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setDirection(DcMotor.Direction.REVERSE);

        // Motor encoders
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        driveDictionary.put("leftFrontDrive", leftFrontDrive);
        driveDictionary.put("leftBackDrive", leftBackDrive);
        driveDictionary.put("rightFrontDrive", rightFrontDrive);
        driveDictionary.put("rightBackDrive", rightBackDrive);
        driveDictionary.put("slideMotor", slideMotor);
        driveDictionary.put("armMotor", armMotor);

        servoDictionary.put("wristServo", wristServo);
        servoDictionary.put("intakeServo", intakeServo);

        sensorDictionary.put("slideSafety", slideSafety);
    }

    public Map<String, DcMotor> getDriveDictionary() {
        return driveDictionary;
    }
    public Map<String, Servo> getServoDictionary() {
        return servoDictionary;
    }
    public Map<String, TouchSensor> getSensorDictionary() {
        return sensorDictionary;
    }
}
