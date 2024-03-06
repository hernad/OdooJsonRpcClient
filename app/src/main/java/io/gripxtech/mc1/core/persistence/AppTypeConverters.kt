package ba.out.bring.odoo.mc1.core.persistence

import androidx.room.TypeConverter
import com.google.gson.JsonElement
import ba.out.bring.odoo.mc1.toJsonElement

class AppTypeConverters {

    @TypeConverter
    fun fromJsonElement(jsonElement: JsonElement): String = jsonElement.toString()

    @TypeConverter
    fun stringToJsonElement(string: String): JsonElement = string.toJsonElement()
}
