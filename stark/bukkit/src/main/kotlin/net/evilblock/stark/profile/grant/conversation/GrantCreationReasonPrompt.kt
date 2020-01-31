package net.evilblock.stark.profile.grant.conversation

import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.core.rank.Rank
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt

class GrantCreationReasonPrompt(val target: BukkitProfile, val rank: Rank) : StringPrompt() {

    override fun getPromptText(conversationContext: ConversationContext): String {
        return ChatColor.GREEN.toString() + "Please specify a valid reason."
    }

    override fun acceptInput(conversationContext: ConversationContext, s: String): Prompt? {
        conversationContext.setSessionData("rank", rank)
        conversationContext.setSessionData("reason", s)
        return GrantCreationPeriodPrompt(target)
    }

}
