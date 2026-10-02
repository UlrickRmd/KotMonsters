package item

import dresseur.Entraineur

class Badge(id: Int, nom: String, description: String, var champion: Entraineur? = null) :
    Item(id, nom, description) {

}
