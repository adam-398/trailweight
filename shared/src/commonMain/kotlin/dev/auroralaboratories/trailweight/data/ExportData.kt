package dev.auroralaboratories.trailweight.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.put
import kotlin.time.Clock

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
    fun toJson(): String = buildJsonObject {
        put("version", version)
        put("lists", buildJsonArray {
            lists.forEach { list ->
                add(buildJsonObject {
                    put("name", list.name)
                    put("notes", list.notes)
                    put("createdAt", list.createdAt)
                    put("items", buildJsonArray {
                        list.items.forEach { item ->
                            add(buildJsonObject {
                                put("name", item.name)
                                put("weight", item.weight)
                                put("category", item.category)
                                put("notes", item.notes)
                            })
                        }
                    })
                })
            }
        })
    }.toString()

    /**
     * Companion object containing constants and functions related to the export data format.
     * @property CURRENT_VERSION The current version of the export data format.
     */
    companion object {
        const val CURRENT_VERSION = 1

        /**
         * Creates an export data object from a JSON string.
         * @param json The JSON string to parse.
         * @return The export data object.
         */
        fun fromJson(json: String): ExportData {
            val root = Json.parseToJsonElement(json).jsonObject
            val version = root.getValue("version").jsonPrimitive.int
            require(version <= CURRENT_VERSION) { "Unsupported export version $version" }
            return ExportData(
                version,
                root.getValue("lists").jsonArray.map { listElement ->
                    val l = listElement.jsonObject
                    ExportGearList(
                        name = l.getValue("name").jsonPrimitive.content,
                        notes = l["notes"]?.jsonPrimitive?.contentOrNull,
                        createdAt = l["createdAt"]?.jsonPrimitive?.longOrNull
                            ?: Clock.System.now().toEpochMilliseconds(),
                        items = l.getValue("items").jsonArray.map { itemElement ->
                            val i = itemElement.jsonObject
                            ExportGearItem(
                                name = i.getValue("name").jsonPrimitive.content,
                                weight = i["weight"]?.jsonPrimitive?.doubleOrNull,
                                category = i.getValue("category").jsonPrimitive.content,
                                notes = i["notes"]?.jsonPrimitive?.contentOrNull
                            )
                        }
                    )
                }
            )
        }
    }
}