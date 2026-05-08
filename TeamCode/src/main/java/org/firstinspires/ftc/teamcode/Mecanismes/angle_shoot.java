package org.firstinspires.ftc.teamcode.Mecanismes;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class angle_shoot {

    public Servo servoAngle;
    private double position = 0.5; // position centrale au départ
    private final double PAS = 0.02; // vitesse de rotation
    private final double MIN = 0.0;
    private final double MAX = 1.0;

    public void init(HardwareMap hwMap) {
        servoAngle = hwMap.get(Servo.class, "servoAngle");
        servoAngle.setPosition(position);
    }

    public void tournerDroite() {
        position = Math.min(MAX, position + PAS);
        servoAngle.setPosition(position);
    }

    public void tournerGauche() {
        position = Math.max(MIN, position - PAS);
        servoAngle.setPosition(position);
    }
}