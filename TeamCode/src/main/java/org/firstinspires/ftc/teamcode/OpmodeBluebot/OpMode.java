package org.firstinspires.ftc.teamcode.OpmodeBluebot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Mecanismes.AngleShoot;
import org.firstinspires.ftc.teamcode.Mecanismes.ConduiteMecanum;
import org.firstinspires.ftc.teamcode.Mecanismes.Ramassage;
import org.firstinspires.ftc.teamcode.Mecanismes.Shoot;
import org.firstinspires.ftc.teamcode.Mecanismes.Tourelle;

@TeleOp
public class OpMode extends com.qualcomm.robotcore.eventloop.opmode.OpMode {

    ConduiteMecanum drive = new ConduiteMecanum();
    Ramassage ramasse = new Ramassage();
    Shoot lance = new Shoot();
    Tourelle tourelle = new Tourelle();
    AngleShoot angle = new AngleShoot();



    @Override
    public void init() {
        drive.init(hardwareMap);
        ramasse.init(hardwareMap);
        lance.init(hardwareMap);
        tourelle.init(hardwareMap);
        angle.init(hardwareMap);
    }

    @Override
    public void loop() {

        double transversal = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double rotation = gamepad1.right_stick_x;

        drive.deplacerSelonRobot(transversal, lateral, rotation);

        // Ramassage
        if (gamepad1.b) {
            ramasse.ramasse(1);
        } else if (gamepad1.left_trigger > 0.1) {
            ramasse.ramasse(-1);
        } else {
            ramasse.stop();
        }

        // Shoot
        if (gamepad1.right_trigger > 0.1) {
            lance.lance(1);
        }


        if (gamepad1.dpad_up) {
            angle.tournerDroite();
        } else if (gamepad1.dpad_down) {
            angle.tournerGauche();
        }



        // Tourelle : R1 = gauche, L1 = droite
        if (gamepad1.right_bumper) {
            tourelle.tournerGauche();
        } else if (gamepad1.left_bumper) {
            tourelle.tournerDroite();
        } else {
            tourelle.stop();
        }

        // Telemetry
        telemetry.addData("Tourelle", gamepad1.right_bumper ? "→ Droite" :
                gamepad1.left_bumper ? "← Gauche" : "Stop");
        telemetry.update();
    }
}