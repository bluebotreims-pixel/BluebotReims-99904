package org.firstinspires.ftc.teamcode.Mecanismes;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Tourelle {

    public CRServo servoPignon;

    // Vitesse de rotation de la tourelle
    private static final double VITESSE = 1.0;

    public void init(HardwareMap hwMap) {
        servoPignon = hwMap.get(CRServo.class, "tourelle");
        servoPignon.setDirection(CRServo.Direction.FORWARD);
    }

    // Rotation dans le sens horaire (R1)
    public void tournerDroite() {
        servoPignon.setPower(VITESSE);
    }

    // Rotation dans le sens anti-horaire (L1)
    public void tournerGauche() {
        servoPignon.setPower(-VITESSE);
    }

    public void stop() {
        servoPignon.setPower(0);
    }
}