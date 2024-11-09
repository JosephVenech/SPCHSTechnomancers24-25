package org.firstinspires.ftc.teamcode.pedroPathing.Paths;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
@Config
@Autonomous(name = "Move Left")
/* public abstract class Test extends OpMode {
    private Follower follower;
    private Path left;

    @Override
    public void init() {
        follower = new Follower(hardwareMap);

        left = new Path(new BezierLine(new Point(0, 0, Point.CARTESIAN), new Point(0, 40, Point.CARTESIAN)));
        left.setConstantHeadingInterpolation(0);

        follower.followPath(left);
    }
} */
public abstract class Test extends OpMode {
    private Follower follower;
    private Path left;

    @Override
    public void init() {
        follower = new Follower(hardwareMap);

        left = new Path(new BezierLine(new Point(0, 0, Point.CARTESIAN), new Point(0, 40, Point.CARTESIAN)));
        left.setConstantHeadingInterpolation(90);

        follower.followPath(left);
    }
}