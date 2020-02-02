package cc.fyre.hub.cosmetic.categories.hidden.emote.impl

import cc.fyre.hub.cosmetic.categories.hidden.emote.Emote
import cc.fyre.hub.util.ParticleMeta
import cc.fyre.hub.util.ParticleUtil
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.material.MaterialData

class HeartEmote : Emote {

    override fun getDisplayName(): String {
        return "Heart"
    }

    override fun getDescription(): List<String> {
        return arrayListOf("&6Perform a &c❤ &6emote.")
    }

    override fun getIcon(): MaterialData {
        return MaterialData(Material.RED_ROSE)
    }

    override fun playEffect(location: Location) {
        ParticleUtil.sendsParticleToAll(ParticleMeta(location, Effect.HEART))
    }

}