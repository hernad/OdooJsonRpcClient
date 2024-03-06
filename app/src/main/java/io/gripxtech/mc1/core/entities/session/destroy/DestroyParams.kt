package ba.out.bring.odoo.mc1.core.entities.session.destroy

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DestroyParams(

        @field:Expose
        @field:SerializedName("context")
        var context: JsonObject = JsonObject()
)
