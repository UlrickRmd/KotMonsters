package item

/**
 * Représente un objet générique dans le contexte du jeu.
 *
 * Cette classe est ouverte afin de servir de base à d'autres types d'objets
 * (par exemple [Badge]).
 *
 * @property id L'identifiant unique de l'objet.
 * @property nom Le nom de l'objet.
 * @property description La description de l'objet.
 */
open class Item(var id: Int,
                var nom: String,
                var description: String) {
}