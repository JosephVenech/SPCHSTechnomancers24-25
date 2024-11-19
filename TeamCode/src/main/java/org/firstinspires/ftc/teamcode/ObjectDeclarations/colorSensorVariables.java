package org.firstinspires.ftc.teamcode.ObjectDeclarations;

public class colorSensorVariables {
    public static float gain = 20;

    // Threshold values should be a bit below actual values to give margin of error

    // Red color value when red sample is in intake
    // When it is above this threshold it knows it is Red sample (yellow also triggers this)
    public static double redThreshold = 0.150;

    // Green color value when yellow sample is in intake
    // Since red value is high for both yellow and red samples, but green is only high for yellow
    // the green value is used to differentiate between yellow and red
    public static double yellowThreshold = 0.140;

    // Blue color value when blue sample is in intake
    // Only blue is above this threshold so it has to be blue sample
    public static double blueThreshold = 0.180;
}
