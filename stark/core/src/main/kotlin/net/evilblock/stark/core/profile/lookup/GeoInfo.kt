package net.evilblock.stark.core.profile.lookup

import org.bson.Document

class GeoInfo(val ip: String, val city: String?, val region: String?, val country: String?, val postal: String?) {

    fun toDocument(): Document {
        return Document()
                .append("ip", ip)
                .append("city", city)
                .append("region", region)
                .append("country", country)
                .append("postal", postal)
    }

}