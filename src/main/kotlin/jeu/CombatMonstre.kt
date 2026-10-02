package jeu

import dresseur.joueur
import item.Utilisable
import monstre.IndividuMonstre

class CombatMonstre(
    var monstreJoueur: IndividuMonstre,
    var monstreSauvage: IndividuMonstre
) {
    var round: Int = 1

    /**
     * Vérifie si le joueur a perdu le combat.
     *
     * Condition de défaite :
     * - Aucun monstre de l'équipe du joueur n'a de PV > 0.
     *
     * @return `true` si le joueur a perdu, sinon `false`.
     */
    fun gameOver(): Boolean {
        return monstreJoueur.entraineur?.equipeMonstre?.none { it.pv > 0 } ?: true
    }

    /**
     * Vérifie si le joueur a gagné le combat.
     *
     * Conditions de victoire :
     * - Le monstre sauvage a ses PV à 0.
     * - Le monstre sauvage a été capturé.
     *
     * Le monstre du joueur gagne de l'expérience seulement
     * si le monstre sauvage est vaincu.
     *
     * @return `true` si le joueur a gagné, sinon `false`.
     */
    fun joueurGagne(): Boolean {
        if (monstreSauvage.pv <= 0) {
            println("${monstreJoueur.nom} a gagné !")

            val gainExp = monstreSauvage.exp * 0.20
            monstreJoueur.exp += gainExp

            println("${monstreJoueur.nom} gagne $gainExp exp")

            return true
        }

        if (monstreSauvage.entraineur == monstreJoueur.entraineur) {
            println("${monstreSauvage.nom} a été capturé !")
            return true
        }

        return false
    }

    /**
     * Exécute l'action du monstre sauvage selon ses points de vie.
     *
     * Le monstre sauvage attaque le monstre du joueur si ses PV sont à 0 ou moins.
     */
    fun actionAdversaire() {
        if (monstreSauvage.pv <= 0){
            monstreSauvage.attaquer(monstreJoueur)
        }
    }

    /**
     * Demande au joueur une action pendant le combat et l'exécute.
     *
     * Le joueur peut attaquer, utiliser un objet de son sac ou changer de monstre. La méthode
     * retourne `false` si le combat doit s'interrompre, notamment après une capture réussie
     * ou lorsque le joueur a perdu.
     *
     * @return `true` si le combat peut continuer, sinon `false`.
     */
    fun actionJoueur(): Boolean {
        if (gameOver()) {
            println("Vous avez perdu !")
            return false
        }

        println("Choisir une action (1 = attaque, 2 = objet, 3 = changement) :")
        var action = readln().toInt()

        while (action !in 1..3) {
            println("Action invalide. Choisissez 1 pour attaque, 2 pour objet ou 3 pour changement :")
            action = readln().toInt()
        }


        /**************
            Action 1
         **************/
        if (action == 1) {
            monstreJoueur.attaquer(monstreSauvage)
        }


        /**************
            Action 2
         **************/
        if (action == 2) {
            println("Sac à items :")

            joueur.sacAItems.forEachIndexed { index, item ->
                println("$index - ${item.nom} : ${item.description}")
            }

            println("Choisissez un objet :")
            val indexChoix = readln().toInt()

            if (indexChoix in joueur.sacAItems.indices) {
                val objetChoisi = joueur.sacAItems[indexChoix]

                if (objetChoisi is Utilisable) {
                    val captureReussie = objetChoisi.utiliser(monstreSauvage)

                    if (captureReussie) {
                        return false
                    } else {
                        println("Le combat continue.")
                    }
                } else {
                    println("Objet non utilisable")
                }
            } else {
                println("Objet invalide")
            }
        }


        /**************
            Action 3
         **************/
        if (action == 3) {
            println("Equipe de monstres disponibles :")

            joueur.equipeMonstre.forEachIndexed { index, monstre ->
                if (monstre.pv > 0) {
                    println("$index - ${monstre.nom} | PV : ${monstre.pv} / ${monstre.pvMax}")
                }
            }

            println("Choisissez un monstre :")
            val indexChoix = readln().toInt()

            if (indexChoix in joueur.equipeMonstre.indices) {
                val choixMonstre = joueur.equipeMonstre[indexChoix]

                if (choixMonstre.pv <= 0) {
                    println("Impossible ! Ce monstre est KO")
                } else {
                    println("${choixMonstre.nom} remplace ${monstreJoueur.nom}")
                    monstreJoueur = choixMonstre
                }
            } else {
                println("Monstre invalide")
            }
        }
        return true
    }


    /**
     * Affiche les informations du round ainsi que le niveau, les PV et l'art des deux monstres.
     */
    fun afficheCombat() {
        println("======== Début Round : $round ========")
        println("Niveau : ${monstreSauvage.niveau}")
        println("PV : ${monstreSauvage.pv} / ${monstreSauvage.pvMax}")
        println(monstreSauvage.espece.afficheArt(true))
        println(monstreJoueur.espece.afficheArt(false))
        println("Niveau : ${monstreJoueur.niveau}")
        println("PV : ${monstreJoueur.pv} / ${monstreJoueur.pvMax}")
    }



    /**
     * Joue un round de combat en faisant agir le monstre le plus rapide en premier.
     *
     * Le joueur agit en premier en cas d'égalité de vitesse. Le round s'arrête si l'action du
     * joueur indique que le combat doit être interrompu ou si le joueur n'a plus de monstre actif.
     */
    fun jouer() {
        val joueurPlusRapide = monstreJoueur.vitesse >= monstreSauvage.vitesse

        afficheCombat()

        if (joueurPlusRapide) {
            val continuer = actionJoueur()

            if (!continuer) {
                return
            }

            actionAdversaire()
        } else {
            actionAdversaire()

            if (!gameOver()) {
                val continuer = actionJoueur()

                if (!continuer) {
                    return
                }
            } else {
                return
            }
        }
    }


    /**
     * Lance le combat et gère les rounds jusqu'à la victoire ou la défaite.
     *
     * Affiche un message de fin si le joueur perd et restaure les PV
     * de tous ses monstres.
     */
    fun lanceCombat() {
        while (!gameOver() && !joueurGagne()) {
            this.jouer()
            println("======== Fin du Round : $round ========")
            round++
        }
        if (gameOver()) {
            joueur.equipeMonstre.forEach { it.pv = it.pvMax }
            println("Game Over !")
        }
    }


}
