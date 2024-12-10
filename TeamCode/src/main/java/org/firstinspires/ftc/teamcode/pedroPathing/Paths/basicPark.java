package org.firstinspires.ftc.teamcode.pedroPathing.Paths;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

@Autonomous(name = "Park")
public class basicPark extends OpMode {
    private Follower follower;
    private Path goPark;
    private final Pose startPose = new Pose(4,36,Math.toRadians(270));
    private final Pose park = new Pose(4,12,Math.toRadians(270));

    public void buildPaths(){
        /** Parking Path */
        //Robot goes in a straight line down towards observation deck
        goPark = new Path(new BezierLine(new Point(startPose),new Point(park)));
        goPark.setLinearHeadingInterpolation(startPose.getHeading(), park.getHeading());
    }

    @Override
    public void init() {
        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        buildPaths();
    }

    @Override
    public void loop(){
        follower.update();
    }

    public void start(){
        //Setting Desired Speed
        follower.setMaxPower(0.7);
        //Telling Robot to follow Path
        follower.followPath(goPark);

    }
}

