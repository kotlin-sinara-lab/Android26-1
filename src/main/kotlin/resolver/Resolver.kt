package resolver

import model.Player
import model.Position
import model.Team

/**
 * Реализация [IResolver] для анализа данных чемпионата.
 *
 * @param players список всех игроков, загруженных из CSV.
 */
class Resolver(private val players: List<Player>) : IResolver {

    /**
     * 1. Количество игроков, интересы которых НЕ представляет агентство.
     */
    override fun getCountWithoutAgency(): Int =
        players.count { it.agency == null }

    /**
     * 2. Защитник с наибольшим числом голов.
     * Возвращает пару (имя, количество голов).
     * @throws NoSuchElementException если защитников нет.
     */
    override fun getBestScorerDefender(): Pair<String, Int> {
        val best = players
            .filter { it.position == Position.DEFENDER }
            .maxByOrNull { it.goals }
            ?: throw NoSuchElementException("Защитники не найдены")
        return best.name to best.goals
    }

    /**
     * 3. Русское название позиции самого дорогого немецкого игрока.
     * @throws NoSuchElementException если немецких игроков нет.
     */
    override fun getTheExpensiveGermanPlayerPosition(): String {
        val player = players
            .filter { it.nationality.equals("Germany", ignoreCase = true) }
            .maxByOrNull { it.transferCost }
            ?: throw NoSuchElementException("Немецкие игроки не найдены")
        return player.position.russianName
    }

    /**
     * 4. Команда с наибольшим средним числом красных карточек на одного игрока.
     * @throws NoSuchElementException если список игроков пуст.
     */
    override fun getTheRudestTeam(): Team {
        return players
            .groupBy { it.team }
            .maxByOrNull { (_, teamPlayers) ->
                teamPlayers.map { it.redCards }.average()
            }
            ?.key
            ?: throw NoSuchElementException("Список игроков пуст")
    }
}
