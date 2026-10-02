package monde

import dresseur.joueur
import jeu.CombatMonstre
import monstre.EspeceMonstre
import monstre.IndividuMonstre
import kotlin.random.Random

/**
 * Représente une zone du monde dans le contexte du jeu.
 *
 * Une zone possède un niveau d'expérience de référence, une liste d'espèces de monstres
 * pouvant y apparaître, ainsi que des liens vers la zone suivante et la zone précédente.
 *
 * @property id L'identifiant unique de la zone.
 * @property nom Le nom de la zone.
 * @property expZone L'expérience de référence utilisée pour générer les monstres de la zone.
 * @property especesMonstres La liste des espèces de monstres présentes dans la zone.
 * @property zoneSuivante La zone suivante, ou `null` s'il n'y en a pas.
 * @property zonePrecedante La zone précédente, ou `null` s'il n'y en a pas.
 */
class Zone(
    var id: Int,
    var nom: String,
    var expZone: Int,
    var especesMonstres: MutableList<EspeceMonstre> = mutableListOf(),
    var zoneSuivante: Zone? = null,
    var zonePrecedante: Zone? = null
) {

    /**
     * Crée un monstre sauvage d'une espèce disponible dans cette zone.
     *
     * L'espèce est choisie au hasard et le monstre reçoit une expérience initiale basée sur
     * l'expérience de la zone, avec une variation aléatoire.
     *
     * @return Le monstre sauvage créé.
     */
    fun genereMonstre(): IndividuMonstre {
        // Choix aléatoire d'une espèce parmi celles de la zone
        val especeChoisie = especesMonstres.random()

        // Variation aléatoire de ±20 % sur l'expérience de la zone
        val variation = Random.nextDouble(0.8, 1.2)
        val experienceMonstre = expZone * variation

        return IndividuMonstre(
            id = especeChoisie.id,
            nom = especeChoisie.nom,
            espece = especeChoisie,
            entraineur = null,
            expInit = experienceMonstre
        )
    }

    /**
     * Génère un monstre sauvage et démarre un combat contre le premier monstre vivant du joueur.
     *
     * Si aucun monstre vivant n'est disponible dans l'équipe, affiche un message et ne démarre
     * pas de combat.
     */
    fun rencontreMonstre() {
        val monstreSauvage = genereMonstre()

        // Recherche du premier monstre de l'équipe qui a encore des PV
        val premierPokemon = joueur.equipeMonstre.firstOrNull { it.pv > 0 }

        // Aucun monstre en état de combattre : le combat n'est pas lancé
        if (premierPokemon == null) {
            println("Aucun monstre disponible pour combattre.")
            return
        }

        val combat = CombatMonstre(premierPokemon, monstreSauvage)

        combat.lanceCombat()
    }


    /**
     * La zone précédente (version non nulle, à initialiser avant utilisation).
     */
    lateinit var zonePrecedente: Zone
}