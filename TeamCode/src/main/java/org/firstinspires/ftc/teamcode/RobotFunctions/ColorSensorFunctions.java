package org.firstinspires.ftc.teamcode.RobotFunctions;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.ObjectDeclarations.colorSensorVariables;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ColorSensorFunctions {

    /*
    This function is used to interpret the color sensor values, taking inputs from the color sensor
    and then returning a string either Red, Yellow, Blue, Null

    This is specifically used to determine what sample is held in the intake or if it is empty
     */
    public String colorSensorGetColor (NormalizedColorSensor colorSensor, Boolean isBlueAlliance, Telemetry telemetry) {

        // Create variable for detected color and set default as null and empty
        String detectedColor = "Null";

        // Get the normalized colors from the sensor
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        // Compare to thresholds to detect color
        if (colors.blue > colorSensorVariables.blueThreshold) {
            detectedColor = "Blue";
        }
        else if (colors.green >= colorSensorVariables.yellowThreshold) {
            detectedColor = "Yellow";
        }
        else if (colors.red >= colorSensorVariables.redThreshold) {
            detectedColor = "Red";
        }

        // If wrong alliance sample color EJECT sample
        if ((detectedColor == "Red" && isBlueAlliance) || (detectedColor == "Blue" && !isBlueAlliance)) {
            detectedColor = "EJECT_SAMPLE";
        }

        telemetry.addData("Intake Sample Color", detectedColor);
        return detectedColor;
    }

}
