package ba.out.bring.odoo.mc1.core.entities.dataset.execworkflow

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ba.out.bring.odoo.mc1.core.entities.odooError.OdooError

data class ExecWorkflow(

        @field:Expose
        @field:SerializedName("result")
        var result: JsonObject = JsonObject(),

        @field:Expose
        @field:SerializedName("error")
        var odooError: OdooError = OdooError()
) {
    val isSuccessful get() = !isOdooError
    val isOdooError get() = odooError.message.isNotEmpty()
    val errorCode get() = odooError.code
    val errorMessage get() = odooError.data.message
}