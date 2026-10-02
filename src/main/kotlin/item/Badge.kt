package item

import dresseur.Entraineur

/**
 * Représente un badge dans le contexte du jeu.
 *
 * Un badge est un type d'objet (hérite de [Item]) obtenu en battant un champion.
 * Il est associé à l'entraîneur champion qui le délivre.
 *
 * @param id L'identifiant unique du badge.
 * @param nom Le nom du badge.
 * @param description La description du badge.
 * @property champion L'entraîneur champion associé au badge, ou `null` s'il n'y en a pas.
 */
class Badge(id: Int, nom: String, description: String, var champion: Entraineur? = null) :
    Item(id, nom, description) {

}