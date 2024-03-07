package ba.out.bring.odoo.mc1.core.entities.session.check

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckParams(

        @field:Expose
        @field:SerializedName("context")
        var context: JsonObject = JsonObject()
)
