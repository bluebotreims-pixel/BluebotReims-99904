package org.firstinspires.ftc.teamcode.OpmodeBluebot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Mecanismes.ConduiteMecanum;
import org.firstinspires.ftc.teamcode.Mecanismes.Ramassage;
import org.firstinspires.ftc.teamcode.Mecanismes.Shoot;

@TeleOp
public class OpMode extends com.qualcomm.robotcore.eventloop.opmode.OpMode {

    ConduiteMecanum drive = new ConduiteMecanum();
    Ramassage ramasse = new Ramassage();
    Shoot lance = new Shoot();


    @Override
    public void init() {
        drive.init(hardwareMap);
        ramasse.init(hardwareMap);
        lance.init(hardwareMap);
    }

    @Override
    public void loop() {

        double transversal =-gamepad1.left_stick_y;
        double lateral =  gamepad1.left_stick_x;
        double rotation =  gamepad1.right_stick_x;

        drive.deplacerSelonRobot(transversal, lateral, rotation);

        if (gamepad1.b) {
            ramasse.ramasse(1);
        } else if (gamepad1.left_trigger > 0.1) {
            ramasse.ramasse(-1);
        } else {
            ramasse.stop();
        }

        if (gamepad1.right_trigger > 0.1) {
            lance.lance(1);
        }
    }
}
