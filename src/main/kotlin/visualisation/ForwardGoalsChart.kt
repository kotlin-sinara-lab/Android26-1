package visualisation

import model.Player
import model.Position
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.feature.layout
import org.jetbrains.kotlinx.kandy.letsplot.layers.points
import org.jetbrains.kotlinx.kandy.util.color.Color

/**
 * Вариант 3: Зависимость количества забитых голов от трансферной стоимости для нападающих.
 *
 * График строится через Kandy (lets-plot backend) и сохраняется в PNG-файл.
 */
object ForwardGoalsChart {

    fun show(players: List<Player>) {
        val forwards = players.filter { it.position == Position.FORWARD }

        val costs = forwards.map { it.transferCost / 1_000_000.0 }   // в миллионах
        val goals = forwards.map { it.goals }

        val plot = plot {
            layout {
                title      = "Зависимость голов от трансферной стоимости (нападающие)"
                xAxisLabel = "Трансферная стоимость, млн"
                yAxisLabel = "Голы"
            }

            points {
                x(costs)
                y(goals)
                color = Color.BLUE
                alpha = 0.7
            }
        }

        val outputPath = "forwards_goals_vs_cost.png"
        plot.save(outputPath)
        println("График сохранен: lets-plot-images/$outputPath")
    }
}
