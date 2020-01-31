package net.evilblock.stark.profile.grant.conversation

import nig.cock.nigger.message.Message
import net.evilblock.stark.Stark
import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.core.profile.grant.ProfileGrant
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

class GrantRemovalPrompt(val profile: BukkitProfile, val grant: ProfileGrant) : StringPrompt() {

    override fun getPromptText(conversationContext: ConversationContext): String {
        return ChatColor.GREEN.toString() + "Please specify a valid reason."
    }

    override fun acceptInput(conversationContext: ConversationContext, s: String): Prompt? {
        grant.removedBy = (conversationContext.forWhom as Player).uniqueId
        grant.removedAt = System.currentTimeMillis()
        grant.removalReason = s

        Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
            Stark.instance.core.getProfileHandler().saveProfile(profile)
            Stark.instance.core.globalMessageChannel.sendMessage(Message("GRANT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "grant" to grant.uuid.toString())))
        }

        conversationContext.forWhom.sendRawMessage("${ChatColor.GOLD}Grant removed.")

        return Prompt.END_OF_CONVERSATION
    }

}
