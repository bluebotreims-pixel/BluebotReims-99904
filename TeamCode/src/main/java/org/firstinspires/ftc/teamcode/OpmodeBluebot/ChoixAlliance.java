package org.firstinspires.ftc.teamcode.OpmodeBluebot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AllianceConfig;

/**
 * =========================================================
 *  OPMODE "Choix Alliance" — À lancer AVANT chaque match
 * =========================================================
 *
 * PROCÉDURE :
 *   1. Lance cet OpMode sur le Driver Station
 *   2. Appuie START
 *   3. Appuie O (rond)  → ROUGE  /  X (croix) → BLEU
 *   4. Vérifie la couleur affichée à l'écran
 *   5. Stop → lance ton TeleOp normalement
 *
 * Le choix reste mémorisé jusqu'au redémarrage du Control Hub.
 */
@TeleOp(name = "Choix Alliance", group = "Config")
public class ChoixAlliance extends OpMode {

    @Override
    public void init() {
        telemetry.addLine("=== CHOIX DE L'ALLIANCE ===");
        telemetry.addData("O (rond) ", "→ ROUGE  (tag 24)");
        telemetry.addData("X (croix)", "→ BLEUE  (tag 20)");
        telemetry.addData("Couleur actuelle", AllianceConfig.estRouge ? "ROUGE" : "BLEUE");
        telemetry.update();
    }

    @Override
    public void loop() {

        if (gamepad1.circle || gamepad2.circle) {
            AllianceConfig.estRouge = true;
        } else if (gamepad1.cross || gamepad2.cross) {
            AllianceConfig.estRouge = false;
        }

        telemetry.addLine("=== CHOIX DE L'ALLIANCE ===");
        telemetry.addData("O (rond) ", "→ ROUGE  (tag 24)");
        telemetry.addData("X (croix)", "→ BLEUE  (tag 20)");
        telemetry.addData("✅ Alliance choisie", AllianceConfig.estRouge ? "ROUGE (tag 24)" : "BLEUE (tag 20)");
        telemetry.addLine("Stop → lance ton TeleOp.");
        telemetry.update();
    }
}