package monstre

import dresseur.Entraineur
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Représente un individu monstre dans le contexte du jeu.
 *
 * Un individu est une instance concrète d'une [EspeceMonstre]. Il possède ses propres
 * caractéristiques (attaque, défense, vitesse, PV, etc.), générées à partir de celles de son espèce
 * avec une variation aléatoire, ainsi qu'un niveau, une expérience et un potentiel qui
 * influencent sa progression. Il peut appartenir à un [Entraineur] ou être sauvage.
 *
 * @property id L'identifiant unique de l'individu.
 * @property nom Le nom de l'individu.
 * @property espece L'espèce du monstre.
 * @property expInit L'expérience initiale du monstre à sa création.
 * @property entraineur L'entraîneur propriétaire du monstre, ou `null` s'il est sauvage.
 * @property niveau Le niveau actuel du monstre.
 * @property attaque La statistique d'attaque.
 * @property defense La statistique de défense.
 * @property défense Alias de [defense].
 * @property vitesse La statistique de vitesse.
 * @property attaqueSpe La statistique d'attaque spéciale.
 * @property defenseSpe La statistique de défense spéciale.
 * @property défenseSpe Alias de [defenseSpe].
 * @property pvMax Les points de vie maximum.
 * @property potentiel Le potentiel du monstre, qui influence ses gains de statistiques à chaque niveau.
 * @property exp L'expérience actuelle du monstre. Une augmentation peut déclencher une montée de niveau.
 */
class IndividuMonstre(
    var id: Int,
    var nom: String,
    var espece: EspeceMonstre,
    entraineur: Entraineur? = null,
    var expInit: Double = 0.0
) {

    var entraineur = entraineur
        get() = field
        set(value) {
            field = value
        }

    var niveau: Int = 1
    var attaque: Int = espece.baseAttaque + (-2..2).random()
    var defense: Int = espece.baseDefense + (-2..2).random()
    var défense: Int
        get() = defense
        set(value) {
            defense = value
        }
    var vitesse: Int = espece.baseVitesse + (-2..2).random()
    var attaqueSpe: Int = espece.baseAttaqueSpe + (-2..2).random()
    var defenseSpe: Int = espece.baseDefenseSpe + (-2..2).random()
    var défenseSpe: Int
        get() = defenseSpe
        set(value) {
            defenseSpe = value
        }
    var pvMax: Int = espece.basePv + (-5..5).random()
    val potentiel: Double = Random.nextDouble(0.5, 2.0)
    var exp: Double = 0.0
        get() = field
        set(value) {
            field = value
            // Mémorise si le monstre était niveau 1 (pas de message à l'initialisation)
            val estNiveau1 = niveau == 1
            // Monte de niveau tant que l'expérience atteint le palier suivant
            while (field >= palierExp(niveau)) {
                levelUp()
                if (estNiveau1 == false) {
                    println("Le monstre $nom est maintenant niveau $niveau !")
                }
            }
        }

    /**
     * @property pv Points de vie actuels.
     * Ne peut pas être inférieur à 0 ni supérieur à [pvMax].
     */
    var pv: Int = pvMax
        set(nouveauPv) {
            // Borne les PV entre 0 et pvMax
            field = when {
                nouveauPv < 0 -> 0
                nouveauPv > pvMax -> pvMax
                else -> nouveauPv
            }
        }

    // Initialise l'expérience avec la valeur de départ (peut déclencher des montées de niveau)
    init {
        this.exp = expInit
    }

    /**
     * Calcule l'expérience totale nécessaire pour atteindre un niveau donné.
     *
     * @param niveau Niveau cible.
     * @return Expérience cumulée nécessaire pour atteindre ce niveau.
     */
    fun palierExp(niveau: Int): Double {
        return 100.0 * (niveau - 1).toDouble().pow(2.0)
    }

    /**
     * Augmente le niveau du monstre et recalcule ses caractéristiques.
     */
    fun levelUp() {
        niveau += 1
        // Vérifie si le monstre peut évoluer à ce niveau
        val palier = espece.palierEvolution
        if (palier != null && palier.peutEvoluer(this)) {
            evoluer()
        }
        // Gain de statistiques : modificateur de l'espèce * potentiel, avec variation aléatoire
        attaque += (espece.modAttaque * potentiel).roundToInt() + (-2..2).random()
        defense += (espece.modDefense * potentiel).roundToInt() + (-2..2).random()
        vitesse += (espece.modVitesse * potentiel).roundToInt() + (-2..2).random()
        attaqueSpe += (espece.modAttaqueSpe * potentiel).roundToInt() + (-2..2).random()
        defenseSpe += (espece.modDefenseSpe * potentiel).roundToInt() + (-2..2).random()

        // Les PV max augmentent, et les PV actuels augmentent du même montant
        val gainPvMax = (espece.modPv * potentiel).roundToInt() + (-5..5).random()
        pvMax += gainPvMax
        pv += gainPvMax
    }


    /**
     * Attaque un autre [IndividuMonstre] et inflige des dégâts.
     *
     * Les dégâts sont calculés de manière très simple pour le moment :
     * `dégâts = attaque - (défense / 2)` (minimum 1 dégât).
     *
     * @param cible Monstre cible de l'attaque.
     */
    fun attaquer(cible: IndividuMonstre) {
        val degatBrut = this.attaque
        var degatTotal = degatBrut - (this.defense / 2)
        // Les dégâts infligés sont d'au moins 1
        if (degatTotal < 1) {
            degatTotal = 1
        }
        // Calcul des dégâts réellement infligés (les PV sont bornés à 0)
        val pvAvant = cible.pv
        cible.pv -= degatTotal
        val pvApres = cible.pv
        println("$nom inflige ${pvAvant - pvApres} dégâts à ${cible.nom}")
    }

    /**
     * Demande au joueur de renommer le monstre.
     * Si l'utilisateur entre un texte vide, le nom n'est pas modifié.
     */
    fun renommer() {
        println("Entrez un nouveau nom pour $nom :")
        val nouveauNom = readLine() ?: return
        this.nom = nouveauNom
    }


    /**
     * Affiche les détails complets du monstre (art ASCII à gauche et caractéristiques à droite).
     *
     * @param deFace Indique si l'art ASCII doit être affiché de face (true) ou de dos (false).
     */
    fun afficheDetail(deFace: Boolean = true) {
        val art = espece.afficheArt(deFace)
        val artLines = art.lines()
        // Liste des caractéristiques à afficher à droite de l'art ASCII
        val details = listOf(
            "ID: $id",
            "Nom: $nom",
            "Espèce: ${espece.nom}",
            "Niveau: $niveau",
            "PV: $pv / $pvMax",
            "Attaque: $attaque",
            "Défense: $defense",
            "Attaque Spé: $attaqueSpe",
            "Défense Spé: $defenseSpe",
            "Vitesse: $vitesse",
            "Expérience: $exp / ${palierExp(niveau)}",
            "Potentiel: $potentiel",
            "Entraîneur: ${entraineur?.nom ?: "Sauvage"}"
        )
        // Largeur maximale de l'art et nombre de lignes à afficher pour aligner les deux colonnes
        val maxArtWidth = artLines.maxOfOrNull { it.length } ?: 0
        val maxLines = maxOf(artLines.size, details.size)

        for (i in 0 until maxLines) {
            val artLine = if (i < artLines.size) artLines[i] else ""
            val detailLine = if (i < details.size) details[i] else ""
            // Complète la ligne d'art avec des espaces pour aligner les détails
            val paddedArt = artLine.padEnd(maxArtWidth + 4)
            println(paddedArt + detailLine)
        }
    }


    /*****************************************************************
     *                          SPRINT 2                             *
     *****************************************************************/


    /**
     * Remplace l'espèce de l'individu par l'évolution définie dans son palier
     * et affiche un message indiquant l'évolution.
     */
    fun evoluer() {
        val palier = espece.palierEvolution
        if (palier != null) {
            this.espece = palier.evolution
            println("$nom évolue en ${this.espece.nom} !")
        }
    }
}