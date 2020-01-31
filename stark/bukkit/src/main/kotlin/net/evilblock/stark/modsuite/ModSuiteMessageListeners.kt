package net.evilblock.stark.modsuite

import nig.cock.nigger.message.handler.IncomingMessageHandler
import nig.cock.nigger.message.listener.MessageListener
import com.google.gson.JsonObject
import net.evilblock.stark.modsuite.options.ModOptionsHandler
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.util.*

class ModSuiteMessageListeners : MessageListener {

    @IncomingMessageHandler("STAFF_CHAT")
    fun onIncomingStaffChatMessage(data: JsonObject) {
        val serverName = data.get("serverName").asString
        val senderName = data.get("senderName").asString
        val message = data.get("message").asString

        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("stark.staff")) {
                val options = ModOptionsHandler.get(it)
                if (options.receivingStaffChat) {
                    it.sendMessage("${ChatColor.RED}[SC] [$serverName]${ChatColor.DARK_PURPLE}$senderName${ChatColor.LIGHT_PURPLE}: $message")
                }
            }
        }
    }

    @IncomingMessageHandler("REQUEST")
    fun onIncomingRequestMessage(data: JsonObject) {
        val serverName = data.get("serverName").asString
        val senderName = data.get("senderName").asString
        val reason = data.get("reason").asString

        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("stark.staff")) {
                val options = ModOptionsHandler.get(it)
                if (options.receivingRequests) {
                    it.sendMessage("${ChatColor.BLUE}${ChatColor.BOLD}[Request] ${ChatColor.GRAY}[$serverName] ${ChatColor.AQUA}$senderName ${ChatColor.GRAY}requested assistance")
                    it.sendMessage("${ChatColor.BLUE}${ChatColor.BOLD}     Reason: ${ChatColor.GRAY}$reason")
                }
            }
        }
    }

    @IncomingMessageHandler("REPORT")
    fun onIncomingReportMessage(data: JsonObject) {
        val serverName = data.get("serverName").asString
        val senderName = data.get("senderName").asString
        val reportedUuid = data.get("reportedUuid").asString
        val reportedName = data.get("reportedName").asString
        val reason = data.get("reason").asString
        val reportCount = ModRequestHandler.getReportCount(UUID.fromString(reportedUuid))

        Bukkit.getOnlinePlayers().forEach {
            if (it.hasPermission("stark.staff")) {
                val options = ModOptionsHandler.get(it)
                if (options.receivingRequests) {
                    it.sendMessage("${ChatColor.BLUE}${ChatColor.BOLD}[Report] ${ChatColor.GRAY}[$serverName] ${ChatColor.AQUA}$reportedName ${ChatColor.GRAY}($reportCount) reported by ${ChatColor.AQUA}$senderName")
                    it.sendMessage("${ChatColor.BLUE}${ChatColor.BOLD}     Reason: ${ChatColor.GRAY}$reason")
                }
            }
        }
    }

}