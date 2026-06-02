package org.firstinspires.ftc.teamcode;

/**
 * Stocke la couleur d'alliance choisie avant le match.
 * Variable static → reste en mémoire entre les OpModes
 * sans recharger le code.
 *
 * true  = ROUGE → cible AprilTag ID 24
 * false = BLEUE → cible AprilTag ID 20
 *
 * Défaut : ROUGE (au cas où on oublie de lancer "Choix Alliance")
 */
public class AllianceConfig {
    public static boolean estRouge = true;
}
