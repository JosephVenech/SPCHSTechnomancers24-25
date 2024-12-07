package org.firstinspires.ftc.teamcode.ObjectDeclarations;

public class driveTrainVariables {
    public static double[] driveTrainMotorPower = new double[4];
    public static double driveTrainAcceleration = 0.00;
    public static double driveTrainSpeedMultiplier;
    public static final double speed_increment = 0.01; // amount to ramp motor each CYCLE_MS cycle
    public static final int update_period = 30; // the rate at which drivetrain will accelerate (lower it is the faster drivetrain will accelerate)
    public static double last_update = 0.00; // milliseconds
    public static double current_time = 0.00; // milliseconds
    public static final double max_forward_power = 0.9;  // Maximum FWD power applied to motor
    public static final double max_reverse_power = -max_forward_power; // Maximum REV power applied to motor
}
