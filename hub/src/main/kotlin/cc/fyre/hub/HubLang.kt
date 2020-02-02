package cc.fyre.hub

import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor

object HubLang {

    /**
     * `&7&l» ` - Arrow used on the left side of item display names
     * Named left arrow due to its usage on the left side of items, despite the fact
     * the arrow is actually pointing to the right.
     */
    val LEFT_ARROW = ChatColor.BLUE.toString() + "» "

    val LEFT_ARROW_NAKED = "»"

    /**
     * ` &7&l«` - Arrow used on the right side of item display names
     * Named right arrow due to its usage on the right side of items, despite the fact
     * the arrow is actually pointing to the left.
     */
    val RIGHT_ARROW = ChatColor.BLUE.toString() + " «"

    val RIGHT_ARROW_NAKED = "«"

    /**
     * Solid line which almost entirely spans the (default) Minecraft
     * chat box. 53 is chosen for no reason other than its width being
     * almost equal to that of the chat box.
     */
    val LONG_LINE = ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 53)

}