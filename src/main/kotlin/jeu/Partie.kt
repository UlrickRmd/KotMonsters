package jeu

import dresseur.Entraineur
import monde.Zone
import monstre.EspeceMonstre
import monstre.IndividuMonstre


class Partie (
    var id:Int,
    var joueur: Entraineur,
    var zone: Zone
) {

    /**
     * Propose au joueur de choisir son monstre de départ parmi Springleaf, Flamkip et Aquamy.
     *
     * Affiche les détails des trois monstres, redemande un choix tant que la saisie n'est pas
     * comprise entre 1 et 3, puis demande au joueur de renommer le monstre choisi. Le starter
     * est ensuite ajouté à l'équipe du joueur et lui est associé comme entraîneur.
     */
    fun choixStarter() {
        val especeSpringleaf = EspeceMonstre(
            id = 1, nom = "Springleaf", type = "Graine",
            baseAttaque = 9, baseDefense = 11, baseVitesse = 10,
            baseAttaqueSpe = 12, baseDefenseSpe = 14, basePv = 60,
            modAttaque = 6.5, modDefense = 9.0, modVitesse = 8.0,
            modAttaqueSpe = 7.0, modDefenseSpe = 10.0, modPv = 34.0,
            description = "Petit monstre espiègle rond comme une graine, adore le soleil.",
            particularites = "Sa feuille sur la tête indique son humeur.",
            caractères = "Curieux, amical, timide"
        )
        val especeFlamkip = EspeceMonstre(
            id = 4, nom = "Flamkip", type = "Animal",
            baseAttaque = 12, baseDefense = 8, baseVitesse = 13,
            baseAttaqueSpe = 16, baseDefenseSpe = 7, basePv = 50,
            modAttaque = 10.0, modDefense = 5.5, modVitesse = 9.5,
            modAttaqueSpe = 9.5, modDefenseSpe = 6.5, modPv = 22.0,
            description = "Petit animal entouré de flammes, déteste le froid.",
            particularites = "Sa flamme change d'intensité selon son énergie.",
            caractères = "Impulsif, joueur, loyal"
        )
        val especeAquamy = EspeceMonstre(
            id = 7, nom = "Aquamy", type = "Meteo",
            baseAttaque = 10, baseDefense = 11, baseVitesse = 9,
            baseAttaqueSpe = 14, baseDefenseSpe = 14, basePv = 55,
            modAttaque = 9.0, modDefense = 10.0, modVitesse = 7.5,
            modAttaqueSpe = 12.0, modDefenseSpe = 12.0, modPv = 27.0,
            description = "Créature vaporeuse semblable à une nuage, produit des gouttes pures.",
            particularites = "Fait baisser la température en s'endormant.",
            caractères = "Calme, rêveur, mystérieux"
        )

        val monstre1 = IndividuMonstre(1, "Springleaf", especeSpringleaf)
        val monstre2 = IndividuMonstre(2, "Flamkip", especeFlamkip)
        val monstre3 = IndividuMonstre(3, "Aquamy", especeAquamy)

        var choixSelection: Int
        do {
            println("Détails de Springleaf :")
            monstre1.afficheDetail()
            println("Détails de Flamkip :")
            monstre2.afficheDetail()
            println("Détails de Aquamy :")
            monstre3.afficheDetail()
            println("Choisissez votre starter (1..3) :")
            choixSelection = readLine()?.toIntOrNull() ?: 0
        } while (choixSelection !in 1..3)

        val starter = when (choixSelection) {
            1 -> monstre1
            2 -> monstre2
            else -> monstre3
        }

        starter.renommer()
        joueur.equipeMonstre.add(starter)
        starter.entraineur = joueur
    }



    /**
     * Échange la position de deux monstres dans l'équipe du joueur.
     *
     * Affiche les positions disponibles, demande la position du monstre à déplacer puis sa
     * nouvelle position. La saisie est répétée jusqu'à ce que les deux positions soient
     * occupées et différentes. Si l'équipe contient moins de deux monstres, la méthode
     * affiche un message et ne la modifie pas.
     */
    fun modifierOrdreEquipe() {
        val equipe = joueur.equipeMonstre

        if (equipe.size < 2) {
            println("Il faut au moins deux monstres dans l'équipe pour modifier leur ordre.")
            return
        }

        println("Équipe :")
        equipe.forEachIndexed { index, monstre ->
            println("${index + 1} - ${monstre.nom}")
        }

        var positionActuelle: Int
        do {
            println("Entrez la position du monstre à déplacer (1..${equipe.size}) :")
            positionActuelle = readLine()?.toIntOrNull() ?: 0
            if (positionActuelle !in 1..equipe.size) {
                println("Cette position ne correspond à aucun monstre de l'équipe.")
            }
        } while (positionActuelle !in 1..equipe.size)

        var nouvellePosition: Int
        do {
            println("Entrez sa nouvelle position (1..${equipe.size}) :")
            nouvellePosition = readLine()?.toIntOrNull() ?: 0
            if (nouvellePosition !in 1..equipe.size) {
                println("Cette position ne correspond à aucun monstre de l'équipe.")
            } else if (nouvellePosition == positionActuelle) {
                println("Choisissez une position différente.")
            }
        } while (nouvellePosition !in 1..equipe.size || nouvellePosition == positionActuelle)

        val indexActuel = positionActuelle - 1
        val indexNouveau = nouvellePosition - 1
        val monstreTemporaire = equipe[indexActuel]
        equipe[indexActuel] = equipe[indexNouveau]
        equipe[indexNouveau] = monstreTemporaire

        println("L'ordre de l'équipe a été modifié.")
    }



    /**
     * Affiche l'équipe du joueur et permet de consulter les détails de ses monstres.
     *
     * Le joueur peut saisir le numéro d'un monstre pour afficher ses caractéristiques et son
     * art, saisir `m` pour modifier l'ordre de l'équipe, ou `q` pour quitter cette méthode.
     * La liste de l'équipe est réaffichée après chaque action.
     */
    fun examineEquipe() {
        var continuer = true

        while (continuer) {
            val equipe = joueur.equipeMonstre
            println("======= Votre équipe =======")

            if (equipe.isEmpty()) {
                println("Votre équipe est vide.")
            } else {
                equipe.forEachIndexed { index, monstre ->
                    println("${index + 1} - ${monstre.nom} | PV : ${monstre.pv} / ${monstre.pvMax}")
                }
            }

            println("Entrez le numéro d'un monstre pour voir ses détails, M pour modifier l'ordre, ou Q pour revenir au menu.")
            when (val choix = readLine()?.trim()?.lowercase()) {
                "q" -> continuer = false
                "m" -> modifierOrdreEquipe()
                null -> continuer = false
                else -> {
                    val index = choix.toIntOrNull()?.minus(1)
                    if (index != null && index in equipe.indices) {
                        equipe[index].afficheDetail()
                    } else {
                        println("Choix invalide.")
                    }
                }
            }
        }
    }



    /**
     * Lance le menu de jeu de la partie.
     *
     * Affiche la zone actuelle et propose de rencontrer un monstre sauvage, d'examiner l'équipe,
     * ou de se déplacer vers la zone suivante ou précédente si elle existe. Le menu est
     * réaffiché après chaque action.
     */
    fun jouer() {
        while (true) {
            println("Vous vous trouvez dans la zone : ${zone.nom}")
            println("1 => Rencontrer un monstre sauvage")
            println("2 => Examiner l'équipe de monstres")
            println("3 => Aller à la zone suivante")
            println("4 => Aller à la zone précédente")
            println("Choisissez une action :")

            when (readLine()?.toIntOrNull()) {
                1 -> zone.rencontreMonstre()
                2 -> examineEquipe()
                3 -> {
                    val zoneSuivante = zone.zoneSuivante
                    if (zoneSuivante != null) {
                        zone = zoneSuivante
                    } else {
                        println("Il n'y a pas de zone suivante.")
                    }
                }
                4 -> {
                    val zonePrecedante = zone.zonePrecedante
                    if (zonePrecedante != null) {
                        zone = zonePrecedante
                    } else {
                        println("Il n'y a pas de zone précédente.")
                    }
                }
                else -> println("Choix invalide.")
            }
        }
    }
}
