package net.evilblock.stark.engine.command.data.flag

import net.evilblock.stark.engine.command.data.Data

data class FlagData(val names: List<String>,
                    val description: String,
                    val defaultValue: Boolean,
                    val methodIndex: Int) : Data