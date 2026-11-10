package dev.auroralaboratories.trailweight.data

import org.json.JSONArray
import org.json.JSONObject

/**
 * Data class representing an exported item in a gear list.
 * @property name The name of the gear item.
 * @property weight The weight of the gear item.
 * @property category The category the gear item belongs to.
 * @property notes The notes associated with the gear item.
 */
data class ExportGearItem (
    val name: String,
    val weight: Double? = null,
    val category: String,
    val notes: String? = null
)

/**
 * Data class representing an exported gear list.
 * @property name The name of the gear list.
 * @property notes Any additional notes about the gear list.
 * @property createdAt The date and time the gear list was created for export.
 * @property items The list of items in the gear list.
 */
data class ExportGearList(
    val name: String,
    val notes: String? = null,
    val createdAt: Long,
    val items: List<ExportGearItem>
)

data class ExportData(
    val version: Int = CURRENT_VERSION,
    val lists: List<ExportGearList>
) {

    /**
     * Converts the export data to a JSON string.
     * @return The JSON string representation of the export data.
     */
    fun toJson(): String = JSONObject().apply {
        put("version", version)
        put("lists", JSONArray().also { listsArray ->
            lists.forEach { list ->
                listsArray.put(JSONObject().apply {
                    put("name", list.name)
                    put("notes", list.notes ?: JSONObject.NULL)
                    put("createdAt", list.createdAt)
                    put("items", JSONArray().also { itemsArray ->
                        list.items.forEach { item ->
                            itemsArray.put(JSONObject().apply {
                                put("name", item.name)
                                put("weight", item.weight ?: JSONObject.NULL)
                                put("category", item.category)
                                put("notes", item.notes ?: JSONObject.NULL)
                            })
                        }
                    })
                })
            }
        })
    }.toString()

    companion object {
        const val CURRENT_VERSION = 1

        /**
         * Creates an export data object from a JSON string.
         * @param json The JSON string to parse.
         * @return The export data object.
         */
        fun fromJson(json: String): ExportData {
            val root = JSONObject(json)
            val version = root.getInt("version")
            require(version <= CURRENT_VERSION) { "Unsupported export version $version" }
            val listsArray = root.getJSONArray("lists")
            return ExportData(
                version,
                (0 until listsArray.length()).map { li ->
                    val l = listsArray.getJSONObject(li)
                    val itemsArray = l.getJSONArray("items")
                    ExportGearList(
                        name = l.getString("name"),
                        notes = if (l.isNull("notes")) null else l.getString("notes"),
                        createdAt = l.optLong("createdAt", System.currentTimeMillis()),
                        items = (0 until itemsArray.length()).map { ii ->
                            val i = itemsArray.getJSONObject(ii)
                            ExportGearItem(
                                name = i.getString("name"),
                                weight = if (i.isNull("weight")) null else i.getDouble("weight"),
                                category = i.getString("category"),
                                notes = if (i.isNull("notes")) null else i.getString("notes")
                            )
                        }
                    )
                }
            )
        }
    }
}