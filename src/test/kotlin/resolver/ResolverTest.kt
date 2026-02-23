package resolver

import model.Player
import model.Position
import model.Team
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ResolverTest {

    private val teamA = Team("Alpha FC", "Moscow")
    private val teamB = Team("Beta United", "Berlin")

    private fun makePlayer(
        name: String,
        team: Team = teamA,
        position: Position = Position.MIDFIELD,
        nationality: String = "Brazil",
        agency: String? = "Agency X",
        transferCost: Long = 1_000_000L,
        goals: Int = 0,
        redCards: Int = 0,
    ) = Player(
        name = name,
        team = team,
        position = position,
        nationality = nationality,
        agency = agency,
        transferCost = transferCost,
        participations = 10,
        goals = goals,
        assists = 0,
        yellowCards = 0,
        redCards = redCards,
    )

    @Test
    fun `getCountWithoutAgency returns correct count when mix of agency and no-agency`() {
        val players = listOf(
            makePlayer("A", agency = null),
            makePlayer("B", agency = "Some Agency"),
            makePlayer("C", agency = null),
        )
        assertEquals(2, Resolver(players).getCountWithoutAgency())
    }

    @Test
    fun `getCountWithoutAgency returns 0 when all players have agency`() {
        val players = listOf(
            makePlayer("A", agency = "Agency A"),
            makePlayer("B", agency = "Agency B"),
        )
        assertEquals(0, Resolver(players).getCountWithoutAgency())
    }

    @Test
    fun `getCountWithoutAgency returns total count when no player has agency`() {
        val players = listOf(
            makePlayer("A", agency = null),
            makePlayer("B", agency = null),
        )
        assertEquals(2, Resolver(players).getCountWithoutAgency())
    }

    @Test
    fun `getBestScorerDefender returns defender with most goals`() {
        val players = listOf(
            makePlayer("Ivan",  position = Position.DEFENDER, goals = 5),
            makePlayer("Petr",  position = Position.DEFENDER, goals = 12),
            makePlayer("Sergey",position = Position.FORWARD,  goals = 30),
        )
        val (name, goals) = Resolver(players).getBestScorerDefender()
        assertEquals("Petr", name)
        assertEquals(12, goals)
    }

    @Test
    fun `getBestScorerDefender throws when no defenders`() {
        val players = listOf(
            makePlayer("A", position = Position.FORWARD),
            makePlayer("B", position = Position.MIDFIELD),
        )
        assertThrows(NoSuchElementException::class.java) {
            Resolver(players).getBestScorerDefender()
        }
    }

    @Test
    fun `getBestScorerDefender works with single defender`() {
        val players = listOf(
            makePlayer("Solo", position = Position.DEFENDER, goals = 7),
        )
        val (name, goals) = Resolver(players).getBestScorerDefender()
        assertEquals("Solo", name)
        assertEquals(7, goals)
    }

    @Test
    fun `getTheExpensiveGermanPlayerPosition returns correct russian position name`() {
        val players = listOf(
            makePlayer("Hans",   nationality = "Germany", position = Position.GOALKEEPER, transferCost = 1_000L),
            makePlayer("Fritz",  nationality = "Germany", position = Position.DEFENDER,   transferCost = 5_000L),
            makePlayer("Carlos", nationality = "Brazil",  position = Position.FORWARD,    transferCost = 99_999L),
        )
        assertEquals("Защитник", Resolver(players).getTheExpensiveGermanPlayerPosition())
    }

    @Test
    fun `getTheExpensiveGermanPlayerPosition throws when no german players`() {
        val players = listOf(
            makePlayer("Carlos", nationality = "Brazil"),
        )
        assertThrows(NoSuchElementException::class.java) {
            Resolver(players).getTheExpensiveGermanPlayerPosition()
        }
    }

    @Test
    fun `getTheExpensiveGermanPlayerPosition is case insensitive`() {
        val players = listOf(
            makePlayer("Hans", nationality = "germany", position = Position.FORWARD, transferCost = 9_999L),
        )
        assertEquals("Нападающий", Resolver(players).getTheExpensiveGermanPlayerPosition())
    }

    @Test
    fun `getTheRudestTeam returns team with highest average red cards`() {
        val players = listOf(
            // teamA: средняя = (2 + 0) / 2 = 1.0
            makePlayer("A1", team = teamA, redCards = 2),
            makePlayer("A2", team = teamA, redCards = 0),
            // teamB: средняя = (3 + 3) / 2 = 3.0
            makePlayer("B1", team = teamB, redCards = 3),
            makePlayer("B2", team = teamB, redCards = 3),
        )
        assertEquals(teamB, Resolver(players).getTheRudestTeam())
    }

    @Test
    fun `getTheRudestTeam returns single team when only one team`() {
        val players = listOf(
            makePlayer("A1", team = teamA, redCards = 5),
        )
        assertEquals(teamA, Resolver(players).getTheRudestTeam())
    }

    @Test
    fun `getTheRudestTeam throws when player list is empty`() {
        assertThrows(NoSuchElementException::class.java) {
            Resolver(emptyList()).getTheRudestTeam()
        }
    }
}

