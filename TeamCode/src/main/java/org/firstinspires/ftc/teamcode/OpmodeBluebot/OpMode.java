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
        } else {
            lance.stop();
        }


        if (gamepad1.dpad_up) {
            angle.monterHaut();
        } else if (gamepad1.dpad_down) {
            angle.descendreBas();
        } else {
            angle.stop();
        }

        telemetry.addData("AngleShoot", gamepad1.dpad_up ? "HAUT" :
                gamepad1.dpad_down ? "BAS" : "Stop");
        telemetry.addData("Puissance servoAngle", angle.servoAngle.getPower());

        // Tourelle : R1 = droite, L1 = gauche
        if (gamepad1.right_bumper) {
            tourelle.tournerDroite();
        } else if (gamepad1.left_bumper) {
            tourelle.tournerGauche();
        } else {
            tourelle.stop();
        }

        // Telemetry
        telemetry.addData("Tourelle", gamepad1.right_bumper ? "→ Droite" :
                gamepad1.left_bumper ? "← Gauche" : "Stop");
        telemetry.update();
    }
}