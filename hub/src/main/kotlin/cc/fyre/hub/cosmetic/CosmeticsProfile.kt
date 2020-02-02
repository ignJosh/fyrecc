package cc.fyre.hub.cosmetic

class CosmeticsProfile {

    val map: MutableMap<String, Boolean> = hashMapOf()

    fun isCosmeticEnabled(cosmetic: Cosmetic): Boolean {
        if (map.containsKey(cosmetic.getSerializedName())) {
            return map[cosmetic.getSerializedName()]!!
        }

        return false
    }

    fun toggleCosmetic(cosmetic: Cosmetic) {
        if (map.containsKey(cosmetic.getSerializedName())) {
            map[cosmetic.getSerializedName()] = !map[cosmetic.getSerializedName()]!!
        } else {
            map[cosmetic.getSerializedName()] = true
        }
    }

}