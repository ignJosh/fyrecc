package cc.fyre.hub.scoreboard

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.scoreboard.ScoreGetter
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

class HubScoreGetter : ScoreGetter {

    override fun getScores(scores: LinkedList<String>, player: Player) {
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)!!
        val rank = profile.getRank()

        scores.add("&a&7&m--------------------")
        scores.add("&fYour Rank:")

        if (rank.default) {
            scores.add("${ChatColor.GREEN}${rank.displayName}")
        } else {
            scores.add("${rank.gameColor}${rank.displayName}")
        }

        scores.add("")
        scores.add("&fOnline:")
        scores.add("&6${Stark.instance.core.servers.globalCount}&r")
        scores.add("")
        scores.add("&6kihar.net")
        scores.add("&b&7&m--------------------")
    }

}