package ba.out.bring.odoo.mc1.core.utils

import android.os.Parcel
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import ba.out.bring.odoo.mc1.toJsonElement
import kotlinx.parcelize.Parceler

object JsonElementParceler : Parceler<JsonElement> {
    override fun create(parcel: Parcel): JsonElement = parcel.readString()?.toJsonElement() ?: JsonArray()

    override fun JsonElement.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toString())
    }
}