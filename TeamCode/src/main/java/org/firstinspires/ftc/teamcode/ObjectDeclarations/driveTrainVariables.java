package org.firstinspires.ftc.teamcode.ObjectDeclarations;

import com.qualcomm.robotcore.hardware.DcMotor;

public class driveTrainVariables {
    public static DcMotor[] driveTrainMotorPower = new DcMotor[4];
    public static final double driveTrainDefaultMaxPower = 0.8;
    public static double driveTrainMaxPower = driveTrainDefaultMaxPower;
}
