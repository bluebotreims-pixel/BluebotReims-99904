package org.firstinspires.ftc.teamcode.Mecanismes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Ramassage {

    public DcMotor moteurRamassage;

    public void init(HardwareMap hwMap) {
        moteurRamassage = hwMap.get(DcMotor.class, "ramassage");
        moteurRamassage.setDirection(DcMotor.Direction.REVERSE);
        moteurRamassage.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        moteurRamassage.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void ramasse(double Puissance) {
        Puissance = Math.max(-1.0, Math.min(1.0, Puissance));
        moteurRamassage.setPower(Puissance);
    }
    public void stop(){
        moteurRamassage.setPower(0);
    }
}
