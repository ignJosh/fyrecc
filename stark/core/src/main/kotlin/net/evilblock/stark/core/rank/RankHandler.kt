package net.evilblock.stark.core.rank

import net.evilblock.stark.core.StarkCore
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import java.lang.RuntimeException
import java.util.ArrayList
import java.util.HashMap

class RankHandler {

    private lateinit var collection: MongoCollection<Document>
    private val ranks = HashMap<String, Rank>()
    private val defaultRank = Rank("default", "Default", Integer.MAX_VALUE, ArrayList(), "", "", "", true, arrayListOf(), false)

    fun load() {
        collection = StarkCore.instance.mongo.database.getCollection("ranks")
        collection.createIndex(Document("id", 1))

        loadRanks()
        createRanks() //will fix error yes
    }

    fun getRanks(): List<Rank> {
        return ArrayList(ranks.values)
    }

    fun getMutableRankMap(): MutableMap<String, Rank> {
        return ranks
    }

    fun getById(id: String): Rank? {
        return ranks[id]
    }

    fun getByName(name: String): Rank? {
        for (rank in ranks.values) {
            if (rank.displayName.equals(name, ignoreCase = true)) {
                return rank
            }
        }

        return null
    }

    fun getDefaultRank(): Rank {
        for (rank in ranks.values) {
            if (rank.default) {
                return rank
            }
        }

        return defaultRank
    }

    fun loadRanks() {
        val freshRanks = ArrayList<Rank>()

        for (document in collection.find()) {
            try {
                freshRanks.add(RankSerializer.deserialize(document))
            } catch (e: Exception) {
                if (document.containsKey("id")) {
                    throw RuntimeException("Failed to load rank from document: " + document.getString("id"), e)
                } else {
                    throw RuntimeException("Failed to load rank from document: Couldn't identify rank ID", e)
                }
            }
        }

        for (rank in freshRanks) {
            var internalRank: Rank? = getById(rank.id)

            if (internalRank == null) {
                internalRank = rank
            }

            if (rank != internalRank) {
                internalRank.displayOrder = rank.displayOrder
                internalRank.displayName = rank.displayName
                internalRank.prefix = rank.prefix
                internalRank.playerListPrefix = rank.playerListPrefix
                internalRank.permissions = rank.permissions
            }

            ranks[rank.id] = internalRank
        }

    }

    fun createRanks() {
        if (getByName("Owner") == null) {
            var rank = Rank("Owner")
            rank.displayName = "Owner"
            rank.prefix = "&7[&4Owner&7]&4"
            rank.displayOrder = 0
            rank.playerListPrefix = "&4"
            rank.permissions = mutableListOf()

            saveRank(rank)
        }
        if (getByName("Manager") == null) {
            var rank = Rank("Manager")
            rank.displayName = "Manager"
            rank.prefix = "&7[&4Manager&7]&4"
            rank.displayOrder = 1
            rank.playerListPrefix = "&4"
            rank.permissions = mutableListOf()
            rank.permissions.add(0, "stark.permission")
            rank.permissions.add(1, "stark.permission1")
            //like this i think u can test it

            saveRank(rank)
        }
        //just keep doing that for all ranks
        loadRanks() // put whichever one has this class ye
    }

    fun pullRankUpdate(rankId: String) {
        val document = collection.find(Document("id", rankId)).first()

        if (document != null) {
            val freshRank = RankSerializer.deserialize(document)
            val originalRank = ranks.replace(freshRank.id, freshRank)

            if (originalRank != null) {
                for (profile in StarkCore.instance.getProfileHandler().profiles.values) {
                    var referenceSwitched = false

                    for (grant in profile.rankGrants) {
                        if (grant.rank == originalRank) {
                            grant.rank = freshRank
                            referenceSwitched = true
                        }
                    }

                    if (referenceSwitched) {
                        profile.apply()
                    }
                }
            }
        }
    }

    fun saveRank(rank: Rank) {
        collection.replaceOne(Document("id", rank.id), RankSerializer.serialize(rank), ReplaceOptions().upsert(true))
    }

    fun deleteRank(rank: Rank) {
        ranks.remove(rank.id)
        collection.deleteOne(Document("id", rank.id))
    }

    fun saveAllRanks() {
        this.ranks.values.forEach { this.saveRank(it) }
    }

}
