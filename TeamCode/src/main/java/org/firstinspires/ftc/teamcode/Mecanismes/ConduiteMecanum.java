package org.firstinspires.ftc.teamcode.Mecanismes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ConduiteMecanum {
    public DcMotor DevantGauche, ArriereGauche, DevantDroite, ArriereDroite;

    public void init(HardwareMap HwMap){
        DevantDroite = HwMap.get(DcMotor.class, "moteur 0");
        DevantGauche = HwMap.get(DcMotor.class, "moteur 1");
        ArriereGauche = HwMap.get(DcMotor.class, "moteur 2");
        ArriereDroite = HwMap.get(DcMotor.class, "moteur 3");


        ArriereDroite.setDirection(DcMotor.Direction.REVERSE);
        DevantGauche.setDirection(DcMotor.Direction.REVERSE);

        DevantGauche.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        DevantDroite.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ArriereGauche.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ArriereDroite.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


//IMU = Inertial Measurement Unit = unité de mesure inertielle; il a 3 capteurs :
// Accéléromètres : mesurent l’accélération sur les axes X, Y et Z.
//Gyroscopes : mesurent la vitesse de rotation autour de ces axes.
//(Parfois) Magnétomètres : mesurent le champ magnétique pour déterminer l’orientation par rapport au nord.

    }

    public void drive(double transversal, double lateral, double rotation){
        double DevantGauchePuissance = transversal + lateral + rotation;
        double DevantDroitePuissance = transversal - lateral - rotation;
        double ArriereGauchePuissance = transversal - lateral + rotation;
        double ArriereDroitePuissance = transversal + lateral - rotation;

        double Puissancemax = 1.0;

        Puissancemax = Math.max(Puissancemax, Math.abs(DevantGauchePuissance));
        Puissancemax = Math.max(Puissancemax, Math.abs(DevantDroitePuissance));
        Puissancemax = Math.max(Puissancemax, Math.abs(ArriereGauchePuissance));
        Puissancemax = Math.max(Puissancemax, Math.abs(ArriereDroitePuissance));

        DevantGauche.setPower(DevantGauchePuissance / Puissancemax);
        ArriereGauche.setPower(ArriereGauchePuissance / Puissancemax);
        DevantDroite.setPower(DevantDroitePuissance / Puissancemax);
        ArriereDroite.setPower(ArriereDroitePuissance / Puissancemax);
    }
    public void deplacerSelonRobot(double transversal, double lateral, double rotation){
        this.drive(transversal, lateral, rotation);
    }

}
