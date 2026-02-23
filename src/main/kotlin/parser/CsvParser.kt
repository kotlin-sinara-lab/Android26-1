package parser

import model.Player
import model.Position
import model.Team
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Парсер CSV-файла с данными об игроках.
 *
 * Формат строки (разделитель `;`):
 * Name;Team;City;Position;Nationality;Agency;Transfer cost;Participations;Goals;Assists;Yellow cards;Red cards
 */
object CsvParser {

    private const val DELIMITER = ';'

    /**
     * Читает игроков из [inputStream].
     * Первая строка считается заголовком и пропускается.
     */
    fun parsePlayers(inputStream: InputStream): List<Player> {
        return InputStreamReader(inputStream, Charsets.UTF_8).useLines { lines ->
            lines
                .drop(1)
                .filter { it.isNotBlank() }
                .map { parsePlayerLine(it) }
                .toList()
        }
    }

    private fun parsePlayerLine(line: String): Player {
        val parts = line.split(DELIMITER)
        require(parts.size >= 12) { "Некорректная строка CSV: $line" }

        val team = Team(name = parts[1].trim(), city = parts[2].trim())

        return Player(
            name          = parts[0].trim(),
            team          = team,
            position      = Position.valueOf(parts[3].trim()),
            nationality   = parts[4].trim(),
            agency        = parts[5].trim().takeIf { it.isNotEmpty() },
            transferCost  = parts[6].trim().toLong(),
            participations = parts[7].trim().toInt(),
            goals         = parts[8].trim().toInt(),
            assists       = parts[9].trim().toInt(),
            yellowCards   = parts[10].trim().toInt(),
            redCards      = parts[11].trim().toInt(),
        )
    }
}
