package org.firstinspires.ftc.teamcode.pedroPathing.Paths;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

@Autonomous(name = "Park")
public class Test extends OpMode {
    private Follower follower;
    private Path left;

    private Path moveLeft;

    private final Pose startPose = new Pose(9,36,Math.toRadians(270));
    private final Pose parkingPose = new Pose(9,20,Math.toRadians(270));

    public void buildPaths(){
        moveLeft = new Path(new BezierLine(new Point(startPose),new Point(parkingPose)));
        moveLeft.setLinearHeadingInterpolation(startPose.getHeading(),parkingPose.getHeading());
    }


    @Override
    public void init() {
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        buildPaths();


       /* left = new Path(new BezierLine(new Point(0, 0, Point.CARTESIAN), new Point(0, 10, Point.CARTESIAN)));

        //    left = follower.pathBuilder().addPath(new BezierLine(new Point(0, 0, Point.CARTESIAN), new Point(10, 0, Point.CARTESIAN)));
        left.setConstantHeadingInterpolation(0);



        follower.followPath(left); */
    }

    @Override
    public void loop(){
        follower.update();
    }
    public void start(){
        follower.setMaxPower(0.4);
        follower.followPath(moveLeft);

    }
}

