package net.evilblock.stark.profile.grant.menu

import net.evilblock.stark.Stark
import net.evilblock.stark.core.rank.Rank
import net.evilblock.stark.engine.menu.Button
import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.profile.grant.conversation.GrantCreationReasonPrompt
import net.evilblock.stark.util.ColorMap
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.lang.Exception

class GrantRankButton(private val rank: Rank, private val target: BukkitProfile) : Button() {

    override fun getName(player: Player): String? {
        var name = rank.gameColor + rank.displayName

        if (rank.prefix.isNotBlank()) {
            name = name + " " + rank.prefix
        }

        return name
    }

    override fun getDescription(player: Player): List<String>? {
        return listOf("&7Click to grant &r${target.getPlayerListName()}&7 the ${rank.gameColor + rank.displayName}&7 rank.")
    }

    override fun getMaterial(player: Player): Material? {
        return Material.INK_SACK
    }

    override fun getDamageValue(player: Player): Byte {
        return try {
            (ColorMap.dyeMap[ChatColor.getByChar(rank.gameColor.replace("&", ""))]?: 15).toByte()
        } catch (e: Exception) {
            return 15.toByte()
        }
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        player.closeInventory()

        val factory = ConversationFactory(Stark.instance)
                .withFirstPrompt(GrantCreationReasonPrompt(target, rank))
                .withLocalEcho(false)
                .thatExcludesNonPlayersWithMessage("Go away evil console!")

        player.beginConversation(factory.buildConversation(player))
    }

}