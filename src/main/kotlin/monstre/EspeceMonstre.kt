package monstre
import java.io.File


/**
 * Décrit une espèce de monstre et les caractéristiques communes à ses individus.
 *
 * Une espèce regroupe son identité, son type, ses statistiques de base, les modificateurs
 * utilisés lors des montées de niveau et des informations descriptives. Elle permet aussi
 * d'afficher l'art ASCII associé à l'espèce.
 *
 * @property id Identifiant de l'espèce.
 * @property nom Nom de l'espèce, utilisé notamment pour retrouver ses fichiers d'art.
 * @property type Type associé à l'espèce.
 * @property baseAttaque Valeur d'attaque de base de ses individus.
 * @property baseDefense Valeur de défense de base de ses individus.
 * @property baseVitesse Valeur de vitesse de base de ses individus.
 * @property baseAttaqueSpe Valeur d'attaque spéciale de base de ses individus.
 * @property baseDefenseSpe Valeur de défense spéciale de base de ses individus.
 * @property basePv Valeur de points de vie de base de ses individus.
 * @property modAttaque Modificateur d'attaque appliqué lors d'une montée de niveau.
 * @property modDefense Modificateur de défense appliqué lors d'une montée de niveau.
 * @property modVitesse Modificateur de vitesse appliqué lors d'une montée de niveau.
 * @property modAttaqueSpe Modificateur d'attaque spéciale appliqué lors d'une montée de niveau.
 * @property modDefenseSpe Modificateur de défense spéciale appliqué lors d'une montée de niveau.
 * @property modPv Modificateur de points de vie appliqué lors d'une montée de niveau.
 * @property description Description de l'espèce.
 * @property particularites Particularités de l'espèce.
 * @property caractères Traits de caractère associés à l'espèce.
 * @property palierEvolution Palier d'évolution de l'espèce, ou null si l'espèce n'évolue pas.
 */
class EspeceMonstre (var id : Int,
                     var nom: String,
                     var type: String,
                     val baseAttaque: Int,
                     val baseDefense: Int,
                     val baseVitesse: Int,
                     val baseAttaqueSpe: Int,
                     val baseDefenseSpe: Int,
                     val basePv: Int,
                     val modAttaque: Double,
                     val modDefense: Double,
                     val modVitesse: Double,
                     val modAttaqueSpe: Double,
                     val modDefenseSpe: Double,
                     val modPv: Double,
                     val description: String = "",
                     val particularites: String = "",
                     val caractères: String = "",
                     var palierEvolution: PalierEvolution? = null){



    /**
     * Affiche la représentation artistique ASCII du monstre.
     *
     * @param deFace Détermine si l'art affiché est de face (true) ou de dos (false).
     *               La valeur par défaut est true.
     * @return Une chaîne de caractères contenant l'art ASCII du monstre avec les codes couleur ANSI.
     *         L'art est lu à partir d'un fichier texte dans le dossier resources/art.
     */
    fun afficheArt(deFace: Boolean=true): String{
        val nomFichier = if(deFace) "front" else "back";
        val art=  File("src/main/resources/art/${this.nom.lowercase()}/$nomFichier.txt").readText()
        val safeArt = art.replace("/", "∕")
        return safeArt.replace("\\u001B", "\u001B")
    }

}
