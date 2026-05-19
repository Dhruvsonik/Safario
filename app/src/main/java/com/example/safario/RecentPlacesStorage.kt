import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
object RecentPlacesStorage {

    fun addPlace(context: Context, userEmail: String, place: Place) {

        val prefs = context.getSharedPreferences("recents_$userEmail", Context.MODE_PRIVATE)

        val existing = getPlaces(context, userEmail).toMutableList()

        existing.removeAll { it.name == place.name }
        existing.add(0, place)

        if (existing.size > 5) existing.removeAt(existing.size - 1)

        val json = JSONArray()
        existing.forEach {
            val obj = JSONObject()
            obj.put("name", it.name)
            obj.put("lat", it.lat)
            obj.put("lng", it.lng)
            json.put(obj)
        }

        prefs.edit().putString("places", json.toString()).apply()
    }

    fun getPlaces(context: Context, userEmail: String): List<Place> {

        val prefs = context.getSharedPreferences("recents_$userEmail", Context.MODE_PRIVATE)
        val jsonString = prefs.getString("places", "[]") ?: "[]"

        val list = mutableListOf<Place>()
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

            list.add(
                Place(
                    obj.getString("name"),
                    obj.getDouble("lat"),
                    obj.getDouble("lng")
                )
            )
        }

        return list
    }
}