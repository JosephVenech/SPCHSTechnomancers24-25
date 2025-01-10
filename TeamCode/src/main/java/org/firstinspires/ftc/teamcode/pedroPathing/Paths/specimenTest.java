package org.firstinspires.ftc.teamcode.pedroPathing.Paths;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.ObjectDeclarations.armPositions;
import org.firstinspires.ftc.teamcode.ObjectDeclarations.slidePositions;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.pedroPathing.util.Timer;

@Autonomous(name = "Specimen")
public class specimenTest extends OpMode {

    public DcMotor slideMotor = null;
    public DcMotor armMotor = null;
    private Timer pathTimer, actionTimer, opmodeTimer;

    private int pathState;
    private Follower follower;
    private Path score;

    //Blue Side Basket Starting Pose
    private final Pose startPose = new Pose(11,84,Math.toRadians(0));


    private final Pose preloadedPose = new Pose(37,84,Math.toRadians(0));

    public void buildPaths(){
        /** Preloaded Scoring Path */
        //Robot goes in a straight line forwards towards submersible
        score = new Path(new BezierLine(new Point(startPose),new Point(preloadedPose)));
        score.setLinearHeadingInterpolation(startPose.getHeading(), preloadedPose.getHeading());
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                /** Set the Robot's position for travel */
                slideMotor.setTargetPosition(slidePositions.travelPosition);
                slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideMotor.setPower(slidePositions.motorSpeed);
                slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                armMotor.setTargetPosition(slidePositions.travelPosition);
                armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armMotor.setPower(armPositions.motorSpeed);
                armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                //Follow path
                follower.followPath(score);

                /** Set the Robot's position for specimen */
                slideMotor.setTargetPosition(slidePositions.hangSpecimen);
                slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideMotor.setPower(slidePositions.motorSpeed);
                slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                armMotor.setTargetPosition(slidePositions.hangSpecimen);
                armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                armMotor.setPower(armPositions.motorSpeed);
                armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                //Set to nonexistent path to stop
                setPathState(-1);
                break;
        }
    }

    @Override
    public void init() {
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        buildPaths();

        telemetry.addLine("Place Robot in the center of the fourth tile.");

    }

    @Override
    public void loop(){
        follower.update();

        // Feedback to Driver Hub
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    public void start(){
        //Setting Desired Speed
        follower.setMaxPower(0.7);
        //Telling Robot to follow Path
        follower.followPath(score);

    }
}

