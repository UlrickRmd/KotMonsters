import monstre.IndividuMonstre
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class IndividuMonstreTest {
    @BeforeTest
    fun setUp() {
        especeFlamkip.palierEvolution = palierEvolutionFlamkip

    }

    @Test
    fun levelUp() {
        // Création d'u flamkip au niveau 5
        var monstre1 = IndividuMonstre(2, "flamkip", especeFlamkip, null, 1500.0)
        assertEquals( especeFlamkip,monstre1.espece,)
        assertEquals( 5,monstre1.niveau)
        monstre1.levelUp()
        assertEquals(6,monstre1.niveau )
        assertEquals(especeFlamkip,monstre1.espece )
        monstre1.levelUp()
        assertEquals(7,monstre1.niveau )
        assertEquals(especePyrokip,monstre1.espece )
    }

}