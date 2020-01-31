package net.evilblock.stark.engine.command

import net.evilblock.stark.core.util.mojanguser.MojangUser
import net.evilblock.stark.engine.command.bukkit.ExtendedCommand
import net.evilblock.stark.engine.command.bukkit.ExtendedCommandMap
import net.evilblock.stark.engine.command.bukkit.ExtendedHelpTopic
import net.evilblock.stark.engine.command.data.method.MethodProcessor
import net.evilblock.stark.engine.command.data.parameter.ParameterType
import net.evilblock.stark.engine.command.data.parameter.impl.*
import net.evilblock.stark.engine.command.data.parameter.impl.filter.NormalFilter
import net.evilblock.stark.engine.command.data.parameter.impl.filter.StrictFilter
import net.evilblock.stark.engine.command.data.parameter.impl.offlineplayer.OfflinePlayerWrapper
import net.evilblock.stark.engine.command.data.parameter.impl.offlineplayer.OfflinePlayerWrapperParameterType
import net.evilblock.stark.engine.command.defaults.*
import net.evilblock.stark.engine.command.data.parameter.impl.*
import net.evilblock.stark.engine.command.defaults.*
import net.evilblock.stark.util.ClassUtils
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.SimpleCommandMap
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.HashMap

class CommandHandler {

    companion object {
        val rootNode: CommandNode = CommandNode()
    }

    val parameterTypeMap: MutableMap<Class<*>, ParameterType<*>> = HashMap()
    private val commandMap: CommandMap
    private val knownCommands: MutableMap<String, Command>

    init {
        val commandMapField = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        commandMapField.setAccessible(true)

        commandMap = commandMapField.get(Bukkit.getServer()) as CommandMap

        val knownCommandsField = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
        knownCommandsField.setAccessible(true)

        knownCommands = knownCommandsField.get(commandMap) as MutableMap<String, Command>
    }

    fun load() {
        registerParameterType(Boolean::class.java, BooleanParameterType())
        registerParameterType(Integer::class.java, IntegerParameterType())
        registerParameterType(Int::class.java, IntegerParameterType())
        registerParameterType(Double::class.java, DoubleParameterType())
        registerParameterType(Float::class.java, FloatParameterType())
        registerParameterType(String::class.java, StringParameterType())
        registerParameterType(GameMode::class.java, GameModeParameterType())
        registerParameterType(EntityType::class.java, EntityTypeParameterType())
        registerParameterType(Player::class.java, PlayerParameterType())
        registerParameterType(World::class.java, WorldParameterType())
        registerParameterType(ItemStack::class.java, ItemStackParameterType())
        registerParameterType(Enchantment::class.java, EnchantmentParameterType())
        registerParameterType(OfflinePlayer::class.java, OfflinePlayerParameterType())
        registerParameterType(OfflinePlayerWrapper::class.java, OfflinePlayerWrapperParameterType())
        registerParameterType(MojangUser::class.java, MojangUserParameterType())
        registerParameterType(UUID::class.java, UUIDParameterType())
        registerParameterType(NormalFilter::class.java, NormalFilter())
        registerParameterType(StrictFilter::class.java, StrictFilter())

        registerClass(BroadcastCommand::class.java)
        registerClass(BuildCommand::class.java)
        registerClass(ClearCommand::class.java)
        registerClass(CraftCommand::class.java)
        registerClass(EnchantCommand::class.java)
        registerClass(FeedCommand::class.java)
        registerClass(FlyCommand::class.java)
        registerClass(FreezeCommand::class.java)
        registerClass(GamemodeCommands::class.java)
        registerClass(HeadCommand::class.java)
        registerClass(HealCommand::class.java)
        registerClass(KickCommand::class.java)
        registerClass(KillCommand::class.java)
        registerClass(ListCommand::class.java)
        registerClass(MoreCommand::class.java)
        registerClass(RenameCommand::class.java)
        registerClass(RepairCommand::class.java)
        registerClass(SetSlotsCommand::class.java)
        registerClass(SetSpawnCommand::class.java)
        registerClass(SpawnerCommand::class.java)
        registerClass(SpeedCommand::class.java)
        registerClass(SudoCommands::class.java)
        registerClass(TeleportationCommands::class.java)
        registerClass(UptimeCommand::class.java)
        registerClass(WorldCommand::class.java)

        swapCommandMap()
    }

    fun registerParameterType(clazz: Class<*>, parameterType: ParameterType<*>) {
        parameterTypeMap[clazz] = parameterType
    }

    fun getParameterType(clazz: Class<*>): ParameterType<*>? {
        return parameterTypeMap[clazz]
    }

    private fun swapCommandMap() {
        val commandMapField = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        commandMapField.setAccessible(true)

        val oldCommandMap = commandMapField.get(Bukkit.getServer())
        val newCommandMap = ExtendedCommandMap(Bukkit.getServer())

        val knownCommandsField = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
        knownCommandsField.setAccessible(true)

        val modifiersField = Field::class.java.getDeclaredField("modifiers")
        modifiersField.setAccessible(true)
        modifiersField.setInt(knownCommandsField, knownCommandsField.modifiers and -0x11)

        knownCommandsField.set(newCommandMap, knownCommandsField.get(oldCommandMap))
        commandMapField.set(Bukkit.getServer(), newCommandMap)
    }

    fun registerClass(clazz: Class<*>) {
        for (method in clazz.methods) {
            registerMethod(method)
        }
    }

    private fun registerMethod(method: Method) {
        method.setAccessible(true)

        val nodes = MethodProcessor().process(method)
        if (nodes != null) {
            nodes.forEach { node ->
                val command = ExtendedCommand(node, JavaPlugin.getProvidingPlugin(method.declaringClass))

                register(command)

                node.children.values.forEach { child ->
                    registerHelpTopic(child, node.aliases)
                }
            }
        }
    }

    private fun register(command: ExtendedCommand) {
        val iterator = knownCommands.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.name.equals(command.name, true)) {
                entry.value.unregister(commandMap)
                iterator.remove()
            }
        }

        command.aliases.forEach { alias ->
            knownCommands[alias] = command
        }

        command.register(commandMap)
        knownCommands[command.name] = command
    }

    private fun registerHelpTopic(node: CommandNode, aliases: Set<String>?) {
        if (node.method != null) {
            Bukkit.getHelpMap().addTopic(ExtendedHelpTopic(node, aliases))
        }

        if (node.hasCommands()) {
            node.children.values.forEach { child ->
                registerHelpTopic(child, null)
            }
        }
    }

    fun registerPackage(plugin: Plugin, packageName: String) {
        ClassUtils.getClassesInPackage(plugin, packageName).forEach(this::registerClass)
    }

    fun registerAll(plugin: Plugin) {
        registerPackage(plugin, plugin::class.java.`package`.name)
    }

}