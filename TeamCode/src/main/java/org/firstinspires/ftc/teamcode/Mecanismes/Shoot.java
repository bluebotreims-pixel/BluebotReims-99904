package org.firstinspires.ftc.teamcode.Mecanismes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shoot {

    public DcMotor Lanceur;

    public void init(HardwareMap hwMap) {
        Lanceur = hwMap.get(DcMotor.class, "lanceur");
        Lanceur.setDirection(DcMotor.Direction.REVERSE);
        Lanceur.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Lanceur.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void lance(double Puissance) {
        Puissance = Math.max(-1.0, Math.min(1.0, Puissance));
        Lanceur.setPower(Puissance);
    }
    public void stop(){
        Lanceur.setPower(0);
    }

}
