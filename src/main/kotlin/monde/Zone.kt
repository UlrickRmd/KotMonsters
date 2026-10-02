package monde

import dresseur.joueur
import jeu.CombatMonstre
import monstre.EspeceMonstre
import monstre.IndividuMonstre
import kotlin.random.Random

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
        val especeChoisie = especesMonstres.random()

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

        val premierPokemon = joueur.equipeMonstre.firstOrNull { it.pv > 0 }

        if (premierPokemon == null) {
            println("Aucun monstre disponible pour combattre.")
            return
        }

        val combat = CombatMonstre(premierPokemon, monstreSauvage)

        combat.lanceCombat()
    }


    lateinit var zonePrecedente: Zone
}
