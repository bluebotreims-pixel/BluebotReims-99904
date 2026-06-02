package org.firstinspires.ftc.teamcode.Mecanismes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shoot {

    public DcMotor Lanceur;
    private static final double PUISSANCE_MAX = 0.5;

    public void init(HardwareMap hwMap) {
        Lanceur = hwMap.get(DcMotor.class, "lanceur");
        Lanceur.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Lanceur.setDirection(DcMotor.Direction.REVERSE);
        Lanceur.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void lance(double Puissance) {
        Puissance = Math.max(-0.75, Math.min(1.0, Puissance));
        Lanceur.setPower(Puissance * PUISSANCE_MAX);
    }
    public void stop(){
        Lanceur.setPower(0);
    }

}
