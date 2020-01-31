package net.evilblock.stark.server.chat

import java.util.regex.Pattern

class ChatFilterEntry(val id: String, regex: String) {

    val pattern = Pattern.compile(regex)

}