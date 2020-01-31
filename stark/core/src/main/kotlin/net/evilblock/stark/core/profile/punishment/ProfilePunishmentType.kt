package net.evilblock.stark.core.profile.punishment

import java.util.Arrays

enum class ProfilePunishmentType constructor(val action: String, val color: String, vararg kickMessages: String) {

    BLACKLIST("blacklisted", "&4", "&cYou've been blacklisted from the Kihar Network."),
    BAN("banned", "&c", "&cYou've been banned from the Kihar Network."),
    MUTE("muted", "&e"),
    WARN("warned", "&a");

    val kickMessages: List<String> = Arrays.asList(*kickMessages)

}
