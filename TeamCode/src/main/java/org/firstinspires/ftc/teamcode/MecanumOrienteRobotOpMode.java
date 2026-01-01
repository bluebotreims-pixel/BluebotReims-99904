package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Mecanismes.ConduiteMecanum;

@TeleOp
public class MecanumOrienteRobotOpMode extends OpMode {

    ConduiteMecanum drive = new ConduiteMecanum();

    double transversal, lateral, rotation;

    @Override
    public void init() {
        drive.init(hardwareMap);
    }

    @Override
    public void loop() {
        transversal = gamepad1.left_stick_y;
        lateral = -gamepad1.left_stick_x;
        rotation = -gamepad1.right_stick_x;

        drive.deplacerSelonRobot(transversal, lateral, rotation);

    }
}
