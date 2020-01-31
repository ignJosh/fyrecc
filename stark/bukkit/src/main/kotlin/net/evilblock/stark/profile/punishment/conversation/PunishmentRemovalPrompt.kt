package net.evilblock.stark.profile.punishment.conversation

import nig.cock.nigger.message.Message
import net.evilblock.stark.Stark
import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.core.profile.punishment.ProfilePunishment
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

class PunishmentRemovalPrompt(val profile: BukkitProfile, val punishment: ProfilePunishment) : StringPrompt() {

    override fun getPromptText(conversationContext: ConversationContext): String {
        return ChatColor.GREEN.toString() + "Please specify a valid reason."
    }

    override fun acceptInput(conversationContext: ConversationContext, s: String): Prompt? {
        punishment.removedBy = (conversationContext.forWhom as Player).uniqueId
        punishment.removedAt = System.currentTimeMillis()
        punishment.removalReason = s

        Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
            Stark.instance.core.getProfileHandler().saveProfile(profile)
            Stark.instance.core.globalMessageChannel.sendMessage(Message("PUNISHMENT_UPDATE", mapOf("uuid" to profile.uuid.toString(), "punishment" to punishment.uuid.toString(), "silent" to true)))
        }

        conversationContext.forWhom.sendRawMessage("${ChatColor.GOLD}Punishment removed.")

        return Prompt.END_OF_CONVERSATION
    }

}
