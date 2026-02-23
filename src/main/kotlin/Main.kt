import parser.CsvParser
import resolver.Resolver
import visualisation.ForwardGoalsChart

fun main() {
    val inputStream = checkNotNull(
        object {}.javaClass.classLoader.getResourceAsStream("fakePlayers.csv")
    ) { "Файл fakePlayers.csv не найден в ресурсах" }

    val players = CsvParser.parsePlayers(inputStream)
    println("Загружено игроков: ${players.size}")

    val resolver = Resolver(players)

    val countWithoutAgency = resolver.getCountWithoutAgency()
    println("\n1. Игроков без агентства: $countWithoutAgency")

    val (defenderName, defenderGoals) = resolver.getBestScorerDefender()
    println("2. Лучший бомбардир среди защитников: $defenderName ($defenderGoals голов)")

    val germanPosition = resolver.getTheExpensiveGermanPlayerPosition()
    println("3. Позиция самого дорогого немецкого игрока: $germanPosition")

    val rudestTeam = resolver.getTheRudestTeam()
    println("4. Команда с наибольшим средним числом удалений: ${rudestTeam.name} (${rudestTeam.city})")

    println("\nСтроим график зависимости голов от трансферной стоимости (нападающие)...")
    ForwardGoalsChart.show(players)
}