package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.TouchSensor;

public class SlideFunctions {

    double SlidePosition(double input_1, double input_2, TouchSensor slideSafety){

        double slidePowerConst = 0.7;
        double slidePower;

        if (input_1 >= input_2) {
            slidePower = input_1 * slidePowerConst;
        }
        else {
            slidePower = -input_2 * slidePowerConst;
        }

        // Slide safety
        if (slideSafety.isPressed() && slidePower < 0){
            slidePower = 0;
        }
        return slidePower;
    }

}
