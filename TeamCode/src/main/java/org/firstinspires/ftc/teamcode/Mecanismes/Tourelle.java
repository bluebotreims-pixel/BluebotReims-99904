package org.firstinspires.ftc.teamcode.Mecanismes;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import java.util.List;

import org.firstinspires.ftc.teamcode.AllianceConfig;

/**
 * =========================================================
 *  CLASSE TOURELLE - CRServo + Limelight 3A (AprilTag)
 * =========================================================
 *
 * ÉTAT ACTUEL : Limelight pas encore reçue
 *   → Contrôle uniquement avec R1/L1 (tournerDroite/Gauche/stop)
 *   → suivreCible() ne fait rien du tout (pas de crash)
 *
 * ╔══════════════════════════════════════════════════════╗
 * ║  QUAND TU REÇOIS LA LIMELIGHT :                      ║
 * ║  Change UNE seule ligne (cherche "INTERRUPTEUR") :   ║
 * ║    false  →  true                                    ║
 * ╚══════════════════════════════════════════════════════╝
 *
 * DIFFÉRENCE IMPORTANTE AVEC UN SERVO CLASSIQUE :
 *   Ton servo est un CRServo (rotation continue).
 *   → On ne lui envoie pas une position (0.0 à 1.0)
 *   → On lui envoie une PUISSANCE (-1.0 à +1.0)
 *   → Pour arrêter : setPower(0)
 *   → L'asservissement auto : puissance = erreur * kP
 *     (inspiré de la logique cleanDetection de Decode-2026)
 *
 * BRANCHEMENTS (fichier de configuration du robot) :
 *   CRServo   : "tourelle"   (nom actuel dans ton config)
 *   Limelight : "limelight"  (port USB du Control Hub)
 *
 * AprilTags Decode 2025-2026 :
 *   Alliance ROUGE → ID 24  (cage rouge)
 *   Alliance BLEUE → ID 20  (cage bleue)
 */
public class Tourelle {

    // -------------------------------------------------------
    //  ★ INTERRUPTEUR PRINCIPAL ★
    //  false = Limelight désactivée (R1/L1 uniquement)
    //  true  = Limelight activée   (R1/L1 + suivreCible())
    //
    //  ↓↓↓ CHANGE CETTE LIGNE QUAND TU REÇOIS LA CAMÉRA ↓↓↓
    // -------------------------------------------------------
    private static final boolean LIMELIGHT_BRANCHEE = false;
    // private static final boolean LIMELIGHT_BRANCHEE = true;


    // -------------------------------------------------------
    //  MATÉRIEL
    // -------------------------------------------------------

    /** Servo à rotation continue qui fait tourner la tourelle */
    public CRServo servoPignon;

    /** Caméra Limelight 3A (null si LIMELIGHT_BRANCHEE = false) */
    private Limelight3A limelight;


    // -------------------------------------------------------
    //  IDs APRILTAG DECODE 2025-2026
    //  (source : cleanDetection() dans Camera.java de Decode-2026)
    // -------------------------------------------------------

    /** AprilTag sur la cage de l'alliance BLEUE */
    public static final int APRILTAG_ID_BLEU  = 20;

    /** AprilTag sur la cage de l'alliance ROUGE */
    public static final int APRILTAG_ID_ROUGE = 24;


    // -------------------------------------------------------
    //  CONSTANTES — ajuste selon ton robot
    // -------------------------------------------------------

    /** Vitesse de rotation manuelle avec R1/L1 (entre 0.0 et 1.0) */
    private static final double VITESSE = 1.0;

    /**
     * Gain proportionnel pour l'asservissement automatique.
     * La puissance envoyée au servo = tx * kP
     *   tx =  20° → puissance = 20 * 0.03 = 0.6  (rapide)
     *   tx =   5° → puissance =  5 * 0.03 = 0.15 (lent, précis)
     *   tx =   0° → puissance =  0         (stop)
     *
     * Augmente kP si la tourelle réagit trop lentement.
     * Diminue kP si elle oscille autour de la cible.
     * Valeur de départ recommandée : 0.03
     */
    public static double kP = 0.03;

    /**
     * Puissance minimale pour vaincre le frottement du CRServo.
     * En dessous de cette valeur, la tourelle ne bouge pas vraiment.
     * À calibrer selon ton servo (souvent entre 0.05 et 0.15).
     */
    public static final double PUISSANCE_MIN = 0.08;

    /**
     * Puissance maximale autorisée en mode automatique.
     * Évite que la tourelle parte trop vite et dépasse la cible.
     */
    public static final double PUISSANCE_MAX_AUTO = 0.7;

    /**
     * Zone morte en degrés : si |tx| < TOLERANCE, on arrête le servo.
     * Évite les micro-oscillations quand la cible est presque centrée.
     * 2° est un bon point de départ.
     */
    public static final double TOLERANCE_DEGRES = 2.0;

    /** Index du pipeline Limelight configuré pour AprilTag (Tag36h11) */
    public static final int PIPELINE_APRILTAG = 0;


    // -------------------------------------------------------
    //  ÉTAT INTERNE
    // -------------------------------------------------------

    /** Dernier angle tx reçu de la Limelight (degrés, + = droite) */
    private double dernierTx = 0.0;

    /** Vrai si le tag de notre alliance est détecté en ce moment */
    private boolean cibleDetectee = false;


    // -------------------------------------------------------
    //  INITIALISATION
    // -------------------------------------------------------

    /**
     * Initialise la tourelle.
     * À appeler dans init() de ton OpMode — identique à avant.
     */
    public void init(HardwareMap hwMap) {

        // CRServo → toujours initialisé
        servoPignon = hwMap.get(CRServo.class, "tourelle");
        servoPignon.setDirection(CRServo.Direction.FORWARD);
        servoPignon.setPower(0); // sécurité : servo à l'arrêt au démarrage

        // Limelight → seulement si LIMELIGHT_BRANCHEE = true
        if (LIMELIGHT_BRANCHEE) {
            limelight = hwMap.get(Limelight3A.class, "limelight");
            limelight.setPollRateHz(100);
            limelight.pipelineSwitch(PIPELINE_APRILTAG);
            limelight.start(); // obligatoire, sinon getLatestResult() retourne null
        }
        // Si LIMELIGHT_BRANCHEE = false, limelight reste null → pas de crash
    }

    /**
     * Arrête proprement la Limelight en fin d'OpMode.
     */
    public void arreter() {
        servoPignon.setPower(0);
        if (limelight != null) {
            limelight.stop();
        }
    }


    // -------------------------------------------------------
    //  CONTRÔLE MANUEL (R1 / L1) — identique à avant
    // -------------------------------------------------------

    /**
     * Rotation dans le sens horaire (R1 dans ton OpMode).
     */
    public void tournerDroite() {
        servoPignon.setPower(VITESSE);
    }

    /**
     * Rotation dans le sens anti-horaire (L1 dans ton OpMode).
     */
    public void tournerGauche() {
        servoPignon.setPower(-VITESSE);
    }

    /**
     * Arrête la tourelle (else dans ton OpMode).
     * IMPORTANT pour un CRServo : sans stop(), il continue de tourner !
     */
    public void stop() {
        servoPignon.setPower(0);
    }


    // -------------------------------------------------------
    //  ASSERVISSEMENT AUTOMATIQUE (Limelight → AprilTag)
    //  Inspiré de cleanDetection() de Decode-2026 (Camera.java)
    // -------------------------------------------------------

    /**
     * Fait tourner la tourelle pour centrer le bon AprilTag.
     *
     * LOGIQUE (adaptée de Decode-2026) :
     *   1. Lit les tags détectés par la Limelight
     *   2. Filtre : cherche UNIQUEMENT l'ID de notre alliance
     *      (ID 24 si rouge, ID 20 si bleu — comme cleanDetection())
     *   3. Calcule la puissance proportionnelle à l'erreur tx :
     *      puissance = tx * kP
     *   4. Si |tx| < TOLERANCE → stop (cible centrée)
     *   5. Si tag non visible → stop (on ne bouge pas à l'aveugle)
     *
     * Si LIMELIGHT_BRANCHEE = false → ne fait rien du tout.
     *
     * À appeler dans loop() à la place du bloc R1/L1
     * quand le mode automatique est activé.
     */
    public void suivreCible() {

        // Sécurité : Limelight pas branchée → on sort sans crash
        if (!LIMELIGHT_BRANCHEE || limelight == null) {
            servoPignon.setPower(0);
            return;
        }

        LLResult resultat = limelight.getLatestResult();

        // Pas de résultat valide → on arrête la tourelle
        if (resultat == null || !resultat.isValid()) {
            cibleDetectee = false;
            servoPignon.setPower(0);
            return;
        }

        // Récupère tous les AprilTags visibles dans l'image
        List<LLResultTypes.FiducialResult> tags = resultat.getFiducialResults();

        if (tags == null || tags.isEmpty()) {
            cibleDetectee = false;
            servoPignon.setPower(0);
            return;
        }

        // --- Filtre par alliance (logique de cleanDetection de Decode-2026) ---
        // On cherche UNIQUEMENT le tag ID 24 (rouge) ou ID 20 (bleu)
        // selon ce qui a été choisi dans l'OpMode "Choix Alliance"
        int idTagCible = AllianceConfig.estRouge ? APRILTAG_ID_ROUGE : APRILTAG_ID_BLEU;

        LLResultTypes.FiducialResult tagCible = null;
        for (LLResultTypes.FiducialResult tag : tags) {
            if (tag.getFiducialId() == idTagCible) {
                tagCible = tag; // trouvé !
                break;
            }
        }

        // Le bon tag n'est pas dans l'image → on arrête
        if (tagCible == null) {
            cibleDetectee = false;
            servoPignon.setPower(0);
            return;
        }

        // Tag trouvé !
        cibleDetectee = true;

        // tx = angle horizontal entre le centre de l'image et le tag (degrés)
        // Positif = tag à droite → il faut tourner à droite → puissance positive
        // Négatif = tag à gauche → il faut tourner à gauche → puissance négative
        dernierTx = tagCible.getTargetXDegrees();

        // Zone morte : cible presque centrée → on s'arrête
        if (Math.abs(dernierTx) < TOLERANCE_DEGRES) {
            servoPignon.setPower(0);
            return;
        }

        // Calcul de la puissance proportionnelle à l'erreur
        double puissance = dernierTx * kP;

        // On s'assure d'avoir une puissance minimale pour vaincre le frottement
        // (si la puissance calculée est trop faible, le servo ne bouge pas du tout)
        if (Math.abs(puissance) < PUISSANCE_MIN) {
            puissance = Math.signum(puissance) * PUISSANCE_MIN;
        }

        // On limite la puissance max pour éviter les dépassements
        puissance = Range.clip(puissance, -PUISSANCE_MAX_AUTO, PUISSANCE_MAX_AUTO);

        // On envoie la commande au CRServo
        servoPignon.setPower(puissance);
    }


    // -------------------------------------------------------
    //  GETTERS (télémétrie / débogage)
    // -------------------------------------------------------

    /** @return vrai si le tag de notre alliance est visible */
    public boolean cibleVue() { return cibleDetectee; }

    /** @return dernier angle tx (+ = droite, - = gauche, en degrés) */
    public double getTx() { return dernierTx; }

    /** @return vrai si la Limelight est activée dans le code */
    public boolean limelightActive() { return LIMELIGHT_BRANCHEE; }

    /** @return vrai si le tag est centré (|tx| < TOLERANCE_DEGRES) */
    public boolean estCentree() {
        return cibleDetectee && Math.abs(dernierTx) < TOLERANCE_DEGRES;
    }

    /** @return l'ID du tag actuellement ciblé (20 ou 24) */
    public int getIdTagCible() {
        return AllianceConfig.estRouge ? APRILTAG_ID_ROUGE : APRILTAG_ID_BLEU;
    }
}