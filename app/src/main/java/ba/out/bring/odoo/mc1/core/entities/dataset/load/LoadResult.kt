package ba.out.bring.odoo.mc1.core.entities.dataset.load

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoadResult(

        @field:Expose
        @field:SerializedName("value")
        var value: JsonObject = JsonObject()
)
