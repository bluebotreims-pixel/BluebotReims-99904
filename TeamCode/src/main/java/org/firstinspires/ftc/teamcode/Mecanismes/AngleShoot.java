package org.firstinspires.ftc.teamcode.Mecanismes;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class AngleShoot {

    public CRServo servoAngle;

    private static final double VITESSE = 1.0;

    public void init(HardwareMap hwMap) {
        servoAngle = hwMap.get(CRServo.class, "servoAngle");
        servoAngle.setDirection(CRServo.Direction.REVERSE);
    }

    public void monterHaut() {
        servoAngle.setPower(VITESSE);
    }
    public void descendreBas() {
        servoAngle.setPower(-VITESSE);
    }

    public void stop() {
        servoAngle.setPower(0);
    }
}