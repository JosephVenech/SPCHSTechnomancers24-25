package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotFunctions.IntakeFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.OldMecanumFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.Robot;
import org.firstinspires.ftc.teamcode.RobotFunctions.SlideFunctions;
import org.firstinspires.ftc.teamcode.RobotFunctions.LiftSystemFunctions;

import java.util.Map;

@TeleOp(name="Manual Control", group="Main")
public class ManualTeleOp extends LinearOpMode {
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFrontDrive = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor slideMotor = null;
    public DcMotor armMotor = null;
    public DcMotor leftLiftSystem = null;
    public DcMotor rightLiftSystem = null;
    public Servo wristAngleServo = null;
    public Servo intakeAngleServo = null;
    public Servo leftIntakeServo = null;
    public Servo rightIntakeServo = null;
    public TouchSensor slideSafety = null;
    public NormalizedColorSensor intakeColorSensor = null;

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap);
        Map<String, DcMotor> motors = robot.getDriveDictionary();
        Map<String, Servo> servos = robot.getServoDictionary();
        Map<String, String> misc = robot.getMiscDictionary();

        mapVariables(motors, servos, misc);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        OldMecanumFunctions driveTrain = new OldMecanumFunctions();
        SlideFunctions slideControl = new SlideFunctions();
        LiftSystemFunctions liftSystemFunctions = new LiftSystemFunctions();
        //IntakeFunctions intakeControl = new IntakeFunctions();

        if (opModeIsActive()) {
            while(opModeIsActive()){
                // Functions - Comments can be found in individual files
                driveTrain.fullDriveTrainControl(gamepad1, gamepad2, leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive, telemetry);
                slideControl.SlidePosition(gamepad1, gamepad2, slideMotor, slideSafety, telemetry);
                slideControl.ArmPosition(gamepad1, gamepad2, armMotor, telemetry);
                //liftSystemFunctions.LiftSystemControl(gamepad1, gamepad2, leftLiftSystem, rightLiftSystem, telemetry);
                //intakeControl.intakeAngle(gamepad1, gamepad2, wristServo, telemetry);
                //intakeControl.intakeSpin(gamepad1, gamepad2, intakeServo, telemetry);



                // Telemetry data
                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.update();
            }
        }
    }

    public void mapVariables(
            Map<java.lang.String, DcMotor> motors,
            Map<java.lang.String, Servo> servos,
            Map<java.lang.String, String> misc
    ) {
        // Mapping Motors
        leftFrontDrive = motors.get("leftFrontDrive");
        leftBackDrive = motors.get("leftBackDrive");
        rightFrontDrive = motors.get("rightFrontDrive");
        rightBackDrive = motors.get("rightBackDrive");
        slideMotor = motors.get("slideMotor");
        armMotor = motors.get("armMotor");

        // Mapping Servos
        //wristServo = servos.get("wristServo");
        //intakeServo = servos.get("intakeServo");

        // Mapping TouchSensors
        slideSafety = hardwareMap.get(TouchSensor.class, misc.get("slideSafety"));
        intakeColorSensor = hardwareMap.get(NormalizedColorSensor.class, misc.get("intakeColorSensor"));
    }
}





