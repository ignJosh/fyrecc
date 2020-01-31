package net.evilblock.stark.core.profile.lookup

import com.google.gson.JsonParser
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

object GeoLookup {

    private val client = OkHttpClient()
    private val parser = JsonParser()

    @JvmStatic
    fun lookup(ipAddress: String): GeoInfo? {
        try {
            val request = Request.Builder()
                    .url("https://ipinfo.io/$ipAddress/json")
                    .addHeader("Authorization","Bearer da6c7fbdd3a220")
                    .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful && response.code() == 200) {
                val json = parser.parse(response.body().string()).asJsonObject

                val ip = json.get("ip").asString

                val city = if (json.has("city")) {
                    json.get("city").asString
                } else {
                    null
                }

                val region = if (json.has("region")) {
                    json.get("region").asString
                } else {
                    null
                }

                val country = if (json.has("country")) {
                    json.get("country").asString
                } else {
                    null
                }

                val postal = if (json.has("postal")) {
                    json.get("postal").asString
                } else {
                    "None"
                }

                return GeoInfo(
                        ip,
                        city,
                        region,
                        country,
                        postal
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}