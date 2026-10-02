package item

import dresseur.joueur
import monstre.IndividuMonstre
import kotlin.random.Random

class MonsterKube(
    id: Int,
    nom: String,
    description: String,
    var chanceCapture: Double,
) : Item(id, nom, description), Utilisable {
    /**
     * Tente de capturer le monstre ciblé avec ce Monster Kube.
     *
     * La chance de capture dépend des PV restants de la cible. En cas de réussite, le joueur
     * peut la renommer, puis elle est ajoutée à l'équipe ou à la boîte si l'équipe est pleine.
     * Un monstre déjà entraîné ne peut pas être capturé.
     *
     * @param cible Le monstre sauvage visé par le Monster Kube.
     * @return `true` si la capture réussit, sinon `false`.
     */
    override fun utiliser(cible: IndividuMonstre): Boolean {
        println("Vous lancez le Monster Kube !")

        if (cible.entraineur != null) {
            println("Le monstre ne peut pas être capturé.")
            return false
        }

        val ratioVie = cible.pv.toDouble() / cible.pvMax
        var chanceEffective = chanceCapture * (1.5 - ratioVie)
        chanceEffective = chanceEffective.coerceAtLeast(5.0)

        val nbAleatoire = Random.nextDouble(0.0, 100.0)

        if (nbAleatoire < chanceEffective) {
            println("Le monstre est capturé !")
            println("Entrez un nouveau nom :")
            val nouveauNom = readlnOrNull()
            if (!nouveauNom.isNullOrBlank()) {
                cible.nom = nouveauNom
            }

            if (joueur.equipeMonstre.size >= 6) {
                joueur.boiteMonstre.add(cible)
            } else {
                joueur.equipeMonstre.add(cible)
            }

            cible.entraineur = joueur
            return true
        } else {
            println("Presque ! Le Kube n'a pas pu capturer le monstre !")
            return false
        }
    }
}
