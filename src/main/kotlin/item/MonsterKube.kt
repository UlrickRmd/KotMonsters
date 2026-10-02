package item

import dresseur.joueur
import monstre.IndividuMonstre
import kotlin.random.Random

/**
 * Représente un Monster Kube, un objet utilisable permettant de capturer des monstres sauvages.
 *
 * Un Monster Kube est un type d'objet (hérite de [Item]) qui implémente l'interface [Utilisable].
 * Sa efficacité dépend de sa chance de capture de base ainsi que des PV restants du monstre ciblé.
 *
 * @param id L'identifiant unique du Monster Kube.
 * @param nom Le nom du Monster Kube.
 * @param description La description du Monster Kube.
 * @property chanceCapture La chance de capture de base du Monster Kube (en pourcentage).
 */
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

        // Un monstre qui appartient déjà à un entraîneur ne peut pas être capturé
        if (cible.entraineur != null) {
            println("Le monstre ne peut pas être capturé.")
            return false
        }

        // Plus le monstre a de PV restants, plus la capture est difficile
        val ratioVie = cible.pv.toDouble() / cible.pvMax
        var chanceEffective = chanceCapture * (1.5 - ratioVie)
        // La chance de capture ne peut jamais descendre en dessous de 5 %
        chanceEffective = chanceEffective.coerceAtLeast(5.0)

        // Tirage d'un nombre aléatoire entre 0 et 100 pour déterminer la réussite de la capture
        val nbAleatoire = Random.nextDouble(0.0, 100.0)

        if (nbAleatoire < chanceEffective) {
            println("Le monstre est capturé !")
            println("Entrez un nouveau nom :")
            val nouveauNom = readlnOrNull()
            // Le nom n'est modifié que si le joueur a saisi un nom valide
            if (!nouveauNom.isNullOrBlank()) {
                cible.nom = nouveauNom
            }

            // Ajout à la boîte si l'équipe est pleine (6 monstres), sinon à l'équipe
            if (joueur.equipeMonstre.size >= 6) {
                joueur.boiteMonstre.add(cible)
            } else {
                joueur.equipeMonstre.add(cible)
            }

            // Le monstre capturé appartient désormais au joueur
            cible.entraineur = joueur
            return true
        } else {
            println("Presque ! Le Kube n'a pas pu capturer le monstre !")
            return false
        }
    }
}