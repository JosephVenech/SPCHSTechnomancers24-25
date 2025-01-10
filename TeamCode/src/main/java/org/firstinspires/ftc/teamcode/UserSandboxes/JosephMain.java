package org.firstinspires.ftc.teamcode.UserSandboxes;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.ObjectDeclarations.armPositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.intakePositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.slidePositions;
import org.firstinspires.ftc.teamcode.RobotFunctions.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

import java.util.Map;


@Config
@Autonomous(name = "IP Far Blue")
public class JosephMain extends OpMode {
    private Follower follower;
    private Pose startPose = new Pose(8, 57, 0);
    private Path startForward;
    private Path leftBasket;
    private Path basketClose;
    private Path basketObservation;

    private DcMotor slideMotor = null;
    private DcMotor armMotor = null;
    public Servo intakeServo = null;

    @Override
    public void init() {
        follower = new Follower(hardwareMap);
        Robot robot = new Robot(hardwareMap);
        Map<String, DcMotor> motors = robot.getDriveDictionary();

        //Path to move forward from start
        startForward = new Path(
                new BezierLine
                        (new Point(startPose),
                                new Point(30, 57, 0)
                        )
        );
        startForward.setConstantHeadingInterpolation(0);

        //Path to move left to Basket
        leftBasket = new Path(
                new BezierLine(
                        new Point(30,57,0),
                        new Point(30,120,0)
                )
        );
        leftBasket.setConstantHeadingInterpolation(0);

        //Path to move close to basket
        basketClose = new Path(
                new BezierLine(
                        new Point(30,120,0),
                        new Point(20,125,0)
                )
        );
        basketClose.setConstantHeadingInterpolation(0);

        //Path to move back from basketclose to observation zone
        basketObservation = new Path(
                new BezierCurve(
                        new Point(20, 125, 0),
                        new Point(35,10,0),
                        new Point(10,15,0)
                )
        );
        basketObservation.setConstantHeadingInterpolation(0);





        //follower.followPath(left);
    }

    @Override
    public void loop(){

        follower.update();
        //Starts timer
        this.resetRuntime();
        //If Follower isn't already doing something does this
        if (!follower.isBusy()) {
            //When time is past 1 second does this

            //Sets robot when to move
            slideMotor.setTargetPosition(slidePositions.travelPosition);
            armMotor.setTargetPosition(armPositions.travelPosition);

            //Follows path to basket
            follower.followPath(startForward);
            follower.followPath(leftBasket);
            follower.followPath(basketClose);

            //Spits out Sample
            intakeServo.setPosition(intakePositions.leftIntakeReverse);
            //Parks in Observation
            follower.followPath(basketObservation);



        }

    }
}


