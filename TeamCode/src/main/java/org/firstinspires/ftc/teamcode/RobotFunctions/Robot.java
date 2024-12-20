package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.ObjectDeclarations.colorSensorVariables;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.driveTrainVariables;

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
    public String slideSafety;
    public String intakeColorSensor;

    public Map<String, DcMotor> driveDictionary = new HashMap<>();
    public Map<String, Servo> servoDictionary = new HashMap<>();
    public Map<String, String> miscDictionary = new HashMap<>();

    public Robot(HardwareMap hardwareMap) {
        appendDictionaries(hardwareMap);
        mapVariables();

        setServos();
        setMotors();
        setMisc(hardwareMap);
    }

    public void appendDictionaries(HardwareMap hardwareMap) {
        driveDictionary.put("leftFrontDrive", hardwareMap.get(DcMotor.class, "left_front_drive"));
        driveDictionary.put("leftBackDrive", hardwareMap.get(DcMotor.class, "left_back_drive"));
        driveDictionary.put("rightFrontDrive", hardwareMap.get(DcMotor.class, "right_front_drive"));
        driveDictionary.put("rightBackDrive", hardwareMap.get(DcMotor.class, "right_back_drive"));
        driveDictionary.put("slideMotor", hardwareMap.get(DcMotor.class, "slide_motor"));
        driveDictionary.put("armMotor", hardwareMap.get(DcMotor.class, "arm_motor"));

        servoDictionary.put("wristServo", hardwareMap.get(Servo.class, "wrist_servo"));
        servoDictionary.put("intakeServo", hardwareMap.get(Servo.class, "intake_servo"));

        miscDictionary.put("slideSafety", "slide_safety");
        miscDictionary.put("intakeColorSensor", "sensor_color");
    }

    public void mapVariables() {
        leftFrontDrive = driveDictionary.get("leftFrontDrive");
        leftBackDrive = driveDictionary.get("leftBackDrive");
        rightFrontDrive = driveDictionary.get("rightFrontDrive");
        rightBackDrive = driveDictionary.get("rightBackDrive");
        slideMotor = driveDictionary.get("slideMotor");
        armMotor = driveDictionary.get("armMotor");

        wristServo = servoDictionary.get("wristServo");
        intakeServo = servoDictionary.get("intakeServo");

        slideSafety = miscDictionary.get("slideSafety");
        intakeColorSensor = miscDictionary.get("intakeColorSensor");
    }

    public void setMotors() {
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
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveTrainVariables.driveTrainMotorPower[0] = leftFrontDrive;
        driveTrainVariables.driveTrainMotorPower[1] = rightFrontDrive;
        driveTrainVariables.driveTrainMotorPower[2] = leftBackDrive;
        driveTrainVariables.driveTrainMotorPower[3] = rightBackDrive;

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Zero Power Behaviour
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void setServos() {
        wristServo.setPosition(0.3);
        intakeServo.setPosition(0.5);
    }

    public void setMisc(HardwareMap hardwareMap) {
        NormalizedColorSensor temp_intakeColorSensor = hardwareMap.get(NormalizedColorSensor.class, miscDictionary.get("intakeColorSensor"));
        temp_intakeColorSensor.setGain(colorSensorVariables.gain);
    }

    public Map<String, DcMotor> getDriveDictionary() { return driveDictionary; }
    public Map<String, Servo> getServoDictionary() { return servoDictionary; }
    public Map<String, String> getMiscDictionary() { return miscDictionary; }
}
