package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {

    public DcMotorEx leftFrontDrive;
    public DcMotorEx leftBackDrive;
    public DcMotorEx rightFrontDrive;
    public DcMotorEx rightBackDrive;

    public Robot(HardwareMap hardwareMap) {


        leftFrontDrive = hardwareMap.get(DcMotorEx.class,"left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotorEx.class,"left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class,"right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotorEx.class,"right_back_drive");

        leftFrontDrive.setDirection(DcMotorEx.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorEx.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorEx.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorEx.Direction.FORWARD);








    }


}
