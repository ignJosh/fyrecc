package cc.fyre.hub.cosmetic

import cc.fyre.hub.Hub
import cc.fyre.hub.cosmetic.categories.HiddenCosmeticCategory
import cc.fyre.hub.cosmetic.categories.ParticlesCosmeticCategory
import cc.fyre.hub.cosmetic.categories.WearablesCosmeticCategory
import cc.fyre.hub.cosmetic.menu.CosmeticCategoriesMenu
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

class Cosmetics {

    private val collection: MongoCollection<Document> = Hub.instance.mongo.database.getCollection("profiles")

    val categories: List<CosmeticCategory> = arrayListOf(
            WearablesCosmeticCategory(),
            ParticlesCosmeticCategory(),
            HiddenCosmeticCategory()
    )

    val profiles: MutableMap<UUID, CosmeticsProfile> = hashMapOf()

    init {
        categories.forEach { category ->
            category.getCosmetics().forEach { cosmetic ->
                Hub.instance.server.pluginManager.registerEvents(cosmetic, Hub.instance)
            }
        }
    }

    fun openMenu(player: Player) {
        if (player.hasPermission("hub.cosmetics")) {
            CosmeticCategoriesMenu().openMenu(player)
        } else {
            player.sendMessage("${ChatColor.RED}You don't own any cosmetics. Purchase a rank on our store ${ChatColor.BOLD}store.kihar.net ${ChatColor.RED}to get access to cosmetics.")
        }
    }

    fun isCosmeticEnabled(player: Player, cosmetic: Cosmetic): Boolean {
        val profile = profiles[player.uniqueId]
        return profile?.isCosmeticEnabled(cosmetic) ?: false
    }

    fun toggleCosmetic(player: Player, cosmetic: Cosmetic) {
        val profile = profiles[player.uniqueId]

        if (profile != null) {
            profile.toggleCosmetic(cosmetic)

            if (profile.isCosmeticEnabled(cosmetic)) {
                cosmetic.onEnable(player)
            } else {
                cosmetic.onDisable(player)
            }

            // disable other cosmetics in the same category
            for (category in Hub.instance.cosmetics.categories) {
                if (!category.getCosmetics().contains(cosmetic)) {
                    continue
                }

                for (enabledCheck in category.getCosmetics()) {
                    if (enabledCheck != cosmetic && profile.isCosmeticEnabled(enabledCheck)) {
                        profile.toggleCosmetic(enabledCheck)
                    }
                }
            }

            Hub.instance.server.scheduler.runTaskAsynchronously(Hub.instance) {
                saveProfile(player.uniqueId, profile)
            }
        }
    }

    fun loadProfile(uuid: UUID): CosmeticsProfile {
        val profile = CosmeticsProfile()
        val document = collection.find(Document("uuid", uuid.toString())).first()

        if (document != null) {
            categories.forEach { category ->
                category.getCosmetics().forEach { cosmetic ->
                    if (document.containsKey(cosmetic.getSerializedName())) {
                        profile.map[cosmetic.getSerializedName()] = document.getBoolean(cosmetic.getSerializedName())
                    }
                }
            }
        }

        profiles[uuid] = profile

        return profile
    }

    private fun saveProfile(uuid: UUID, profile: CosmeticsProfile) {
        val document = Document("uuid", uuid.toString())

        profile.map.forEach { (t, u) ->
            document[t] = u
        }

        collection.replaceOne(Document("uuid", uuid.toString()), document, ReplaceOptions().upsert(true))
    }

}