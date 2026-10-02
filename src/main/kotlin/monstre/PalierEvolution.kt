package monstre

/**
 * Représente une évolution possible pour un monstre.
 *
 * Par exemple, Magicarp qui évolue au niveau 50.
 *
 * @property id Identifiant unique du palier d'évolution.
 * @property niveauRequis Niveau requis pour que le monstre puisse évoluer.
 * @property evolution Espèce de monstre obtenue lors de l'évolution.
 */
class PalierEvolution(
    var id: Int,
    var niveauRequis: Int,
    var evolution: EspeceMonstre
) {

    /**
     * Vérifie si un individu de monstre peut évoluer selon ce palier.
     *
     * @param individu L'individu monstre à évaluer.
     * @return `true` si le niveau de l'individu est supérieur ou égal au niveau requis, `false` sinon.
     */
    fun peutEvoluer(individu: IndividuMonstre): Boolean {
        return individu.niveau >= this.niveauRequis
    }
}